package com.example.bookstore.mainService.dao.daoImpl;


import com.example.bookstore.mainService.constants.RedisConstants;
import com.example.bookstore.mainService.dao.BookDao;
import com.example.bookstore.mainService.entity.Book;
import com.example.bookstore.mainService.entity.BookInfo;
import com.example.bookstore.mainService.repositories.BookInfoRepository;
import com.example.bookstore.mainService.repositories.BookRepository;
import com.example.bookstore.mainService.utils.CacheClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Repository
public class BookDaoImpl implements BookDao {
    private final BookRepository bookRepository;
    private final BookInfoRepository bookInfoRepository;
    private final CacheClient cacheClient;

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
}
