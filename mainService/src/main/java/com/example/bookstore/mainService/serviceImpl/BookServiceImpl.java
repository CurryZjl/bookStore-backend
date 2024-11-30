package com.example.bookstore.mainService.serviceImpl;

import com.example.bookstore.mainService.dao.BookDao;
import com.example.bookstore.mainService.dto.BookDto;
import com.example.bookstore.mainService.entity.Book;
import com.example.bookstore.mainService.entity.BookInfo;
import com.example.bookstore.mainService.entity.Tag;
import com.example.bookstore.mainService.repositories.TagRepository;
import com.example.bookstore.mainService.services.BookService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookDao bookDao;
    private final TagRepository tagRepository;

    @Override
    public Optional<BookDto> findBookByBid(long id) {
        Optional<Book> book = bookDao.findBookByBid(id);
        return book.map(BookServiceImpl::mapToBookDto);
    }

    @Transactional
    @Override
    public Long updateBookStatus(long bid, long amount) throws RuntimeException {
        Optional<Book> book = bookDao.findBookByBid(bid);
        if(book.isPresent()){
            Book book1 = book.get();
            long status = book1.getStatus();
            long newStatus = status - amount;
            if(newStatus < 0){
                throw new RuntimeException("书籍bid:" + bid + "缺货");
            }
            book1.setStatus(newStatus);
            Book newBook = bookDao.saveBook(book1, false);
            return newBook.getStatus();
        }
        throw new RuntimeException("更新书籍库存错误");
    }

    @Override
    public Page<BookDto> searchBooksByName(String name , Pageable pageable) {
        Page<Book> books = bookDao.searchBooksByName(name, pageable);
        List<BookDto> bookDtos = books.getContent().stream()
                .map(BookServiceImpl::mapToBookDto)
                .collect(Collectors.toList());
        return new PageImpl<>(bookDtos, pageable, books.getTotalElements());
    }

    @Transactional
    @Override
    public BookDto saveBook(BookDto bookDto) {
        try{
            Optional<Book> book = bookDao.findBookByBid(bookDto.getBid());
            if(book.isEmpty()){
                //新增一本书
                Book book1 = bookDao.saveBook(mapToBook(bookDto), true);
                return mapToBookDto(book1);
            } else{
                Book book1 = book.get();
                book1.setName(bookDto.getName());
                book1.setAuthor(bookDto.getAuthor());
                book1.getBookInfo().setImage(bookDto.getImagePath());
                book1.setISBN(bookDto.getIsbn());
                book1.setStatus(bookDto.getStatus());
                return mapToBookDto(bookDao.saveBook(book1, true));
            }
        }
        catch (Exception e){
            log.warn(e.getMessage());
            return null;
        }
    }

    @Transactional
    @Override
    public BookDto deleteBookByBid(long bid) {
            return mapToBookDto(bookDao.deleteBookByBid(bid));
    }

//    @Override
//    public List<BookDto> searchBooksByTagName(String tagName) {
//        List<Book> books = bookDao.findBooksByTag(tagName);
//        return books.stream().map(BookServiceImpl::mapToBookDto).collect(Collectors.toList());
//    }

//    @Override
//    public List<BookDto> testInsert() {
//        List<Book> books = bookDao.testInsert();
//        return books.stream().map(BookServiceImpl::mapToBookDto).collect(Collectors.toList());
//    }

    private static BookDto mapToBookDto(Book book){
        BookDto bookDto = BookDto.builder()
                .bid(book.getBid())
                .price(book.getPrice())
                .name(book.getName())
                .author(book.getAuthor())
                .status(book.getStatus())
                .isbn(book.getISBN())
                .build();
        if(book.getTag() != null){
            bookDto.setTag(book.getTag().getName());
        }
        if(book.getBookInfo() != null){
            bookDto.setImagePath(book.getBookInfo().getImage());
            bookDto.setIntro(book.getBookInfo().getIntro());
        }
        return bookDto;
    }

    private Book mapToBook(BookDto bookDto){
        Tag tag = this.tagRepository.findTagByName(bookDto.getTag()).orElseGet(()->{
            Tag newTag = Tag.builder().name(bookDto.getTag()).build();
            return tagRepository.save(newTag);
        });

        BookInfo bookInfo = BookInfo.builder()
                .image(bookDto.getImagePath())
                .intro(bookDto.getIntro())
                .build();

        Book book = Book.builder()
                .ISBN(bookDto.getIsbn())
                .name(bookDto.getName())
                .status(bookDto.getStatus())
                .bookInfo(bookInfo)
                .price(bookDto.getPrice())
                .author(bookDto.getAuthor())
                .tag(tag)
                .build();
        return book;
    }
 }
