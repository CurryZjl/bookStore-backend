package com.example.bookstore.mainService.dao.daoImpl;


import com.example.bookstore.mainService.constants.RedisConstants;
import com.example.bookstore.mainService.dao.BookDao;
import com.example.bookstore.mainService.entity.Book;
import com.example.bookstore.mainService.entity.BookInfo;
import com.example.bookstore.mainService.entity.BookTag;
import com.example.bookstore.mainService.neo4jrepo.BookTagRepository;
import com.example.bookstore.mainService.repositories.BookInfoRepository;
import com.example.bookstore.mainService.repositories.BookRepository;
import com.example.bookstore.mainService.utils.CacheClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Repository
public class BookDaoImpl implements BookDao {
    private final BookRepository bookRepository;
    private final BookInfoRepository bookInfoRepository;
    private final CacheClient cacheClient;
    private final BookTagRepository bookTagRepository;

    @Override
    public Optional<Book> findBookByBid(long id) {
        try{
            return Optional.ofNullable(cacheClient.queryRedis(RedisConstants.CACHE_BOOK_KEY, id, Book.class)
                    .orElseGet(()->fetchAndCacheBook(id)));
        }catch (Exception e){
            log.warn(e.getMessage());
            return Optional.empty();
        }
    }

    private Book fetchAndCacheBook(Long id){
        try{
            Optional<Book> book = bookRepository.findBookByBidAndDeletedFalse(id);
            if(book.isEmpty()){
                return null;
            }
            Optional<BookInfo> bookInfo = bookInfoRepository.findById(id);
            bookInfo.ifPresent(info -> book.get().setBookInfo(info));
            cacheClient.setRedis(RedisConstants.CACHE_BOOK_KEY + book.get().getBid(), book.get(),RedisConstants.CACHE_BOOK_TTL, TimeUnit.MINUTES);
            return book.get();
        }
        catch (Exception e){
            log.warn(e.getMessage());
            return null;
        }
    }

    @Override
    public Page<Book> searchBooksByName(String name ,Pageable pageable) {
        Page<Book> books = bookRepository.findBooksByNameContainingIgnoreCaseAndDeletedFalse(name , pageable);
        books.getContent().forEach(book -> {
            Optional<BookInfo> bookInfo = bookInfoRepository.findById(book.getBid());
            bookInfo.ifPresent(book::setBookInfo);
        });
        return books;
    }

    @Override
    public Book deleteBookByBid(Long id) {
        Optional<Book> book = bookRepository.findBookByBidAndDeletedFalse(id);
        if(book.isEmpty())
            return null;
        else {
            book.get().setDeleted(true);
            cacheClient.safeDelete(RedisConstants.CACHE_BOOK_KEY + id);
            bookRepository.save(book.get());
            return book.get();
        }
    }

    @Transactional
    @Override
    public Book saveBook(Book book, Boolean isMongo) {
        Book book1 = bookRepository.save(book);
        if(isMongo){
            //有可能是新增一本书，主键要和book1保持一致
            book.getBookInfo().setId(book1.getBid());
            BookInfo bookInfo = bookInfoRepository.save(book.getBookInfo());
            book1.setBookInfo(bookInfo);
        }
        else{
            book1.setBookInfo(book.getBookInfo());
        }
        cacheClient.setRedis(RedisConstants.CACHE_BOOK_KEY + book1.getBid(), book1, RedisConstants.CACHE_BOOK_TTL, TimeUnit.MINUTES);
        return book1;
    }

    @Override
    public List<Book> findBooksByTag(String tagName) {
        List<BookTag> bookTags = bookTagRepository.findBookTagsByTagNameLike(tagName);
        List<Book> resultBooks = new ArrayList<>();

        HashMap<Long, Boolean> bidMap = new HashMap<>();
        for(BookTag bookTag : bookTags){
            int size = bookTag.getBookIds().size();
            for(int i = 0; i < size; i++){
                Long bid = bookTag.getBookIds().get(i);
                bidMap.put(bid,true);
            }
        }

        //再次查询2跳以内的数据id
        for(BookTag bookTag : bookTags){
            String keyN = bookTag.getTagName();
            List<BookTag> list1 = bookTagRepository.findNodesDistance1(keyN);
            List<BookTag> list2 = bookTagRepository.findNodesDistance2(keyN);
            for(BookTag tag : list1){
                int size = tag.getBookIds().size();
                for(int i = 0; i < size; i++){
                    Long bid = tag.getBookIds().get(i);
                    bidMap.put(bid,true);
                }
            }
            for(BookTag tag2 : list2){
                int size = tag2.getBookIds().size();
                for(int i = 0; i < size; i++){
                    Long bid = tag2.getBookIds().get(i);
                    bidMap.put(bid,true);
                }
            }
        }
        for(Long id: bidMap.keySet()){
            Optional<Book> book = this.findBookByBid(id);
            if(book.isPresent())
                resultBooks.add(book.get());
        }

        return resultBooks;
    }

        @Override
    public List<Book> testInsert() {
        bookTagRepository.deleteAll();
        BookTag bookTag1 = new BookTag("漫画0") ;
        BookTag bookTag2 = new BookTag("计算机") ;
        BookTag bookTag3 = new BookTag("数据结构") ;
        BookTag bookTag4 = new BookTag("小说") ;
        BookTag bookTag5 = new BookTag("漫画1") ;
        BookTag bookTag6 = new BookTag("漫画2") ;
        BookTag bookTag7 = new BookTag("漫画1.1") ;
        BookTag bookTag8 = new BookTag("漫画2.1") ;
        BookTag bookTag9 = new BookTag("漫画1.1.1") ;

        bookTag1.addBookID(1L);
        bookTag1.addBookID(2L);
        bookTag1.addBookID(4L);
        bookTag2.addBookID(21L);
        bookTag2.addBookID(9L);
        bookTag3.addBookID(10L);
        bookTag4.addBookID(19L);
        bookTag5.addBookID(5L);
        bookTag6.addBookID(6L);
        bookTag7.addBookID(7L);
        bookTag8.addBookID(8L);
        bookTag9.addBookID(3L);

        bookTag1.addChildTag(bookTag5);
        bookTag1.addChildTag(bookTag6);
        bookTag5.addChildTag(bookTag7);
        bookTag6.addChildTag(bookTag8);
        bookTag2.addChildTag(bookTag3);
        bookTag7.addChildTag(bookTag9);
        bookTagRepository.save(bookTag1);
        bookTagRepository.save(bookTag2);
        bookTagRepository.save(bookTag3);
        bookTagRepository.save(bookTag4);
        bookTagRepository.save(bookTag5);
        bookTagRepository.save(bookTag6);
        bookTagRepository.save(bookTag7);
        bookTagRepository.save(bookTag8);
        bookTagRepository.save(bookTag9);
        return this.findBooksByTag("漫画0");
    }
}
