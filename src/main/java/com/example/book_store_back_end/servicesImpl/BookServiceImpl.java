package com.example.book_store_back_end.servicesImpl;

import com.example.book_store_back_end.dao.BookDao;
import com.example.book_store_back_end.dto.BookDto;
import com.example.book_store_back_end.dto.MessageDto;
import com.example.book_store_back_end.entity.Book;
import com.example.book_store_back_end.entity.Tag;
import com.example.book_store_back_end.repositories.BookRepository;
import com.example.book_store_back_end.repositories.TagRepository;
import com.example.book_store_back_end.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    private final BookDao bookDao;
    private final BookRepository bookRepository;
    private final TagRepository tagRepository;

    public BookServiceImpl(BookDao bookDao, BookRepository bookRepository, TagRepository tagRepository) {
        this.bookDao = bookDao;
        this.bookRepository = bookRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public Optional<BookDto> findBookByBid(long id) {
        Optional<Book> book = bookDao.findBookByBid(id);
        return book.map(BookServiceImpl::mapToBookDto);
    }

    @Transactional
    @Override
    public MessageDto updateBookStatus(long bid, long amount) {
        Optional<Book> book = bookDao.findBookByBid(bid);
        if(book.isPresent()){
            Book book1 = book.get();
            long status = book1.getStatus();
            long newStatus = status - amount;
            if(newStatus < 0){
                return MessageDto.builder()
                        .valid(false)
                        .message("本书籍缺货")
                        .build();
            }
            book1.setStatus(newStatus);
            bookRepository.save(book1);
            return MessageDto.builder()
                    .message("更新库存成功")
                    .valid(true)
                    .build();
        }
        return MessageDto.builder()
                .valid(false)
                .message("更新书籍库存错误")
                .build();
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
        Optional<Book> book = bookRepository.findBookByBidAndDeletedFalse(bookDto.getBid());
        if(book.isEmpty()){
            Book book1 = bookRepository.save(mapToBook(bookDto));
            return mapToBookDto(book1);
        } else{
            Book book1 = book.get();
            book1.setName(bookDto.getName());
            book1.setAuthor(bookDto.getAuthor());
            book1.setImagePath(bookDto.getImagePath());
            book1.setISBN(bookDto.getIsbn());
            book1.setStatus(bookDto.getStatus());
            try {
                Book book2 = bookRepository.save(book1);
                BookDto bookDto1 = mapToBookDto(book2);
                return bookDto1;
            }catch (Exception e){
                System.err.println(e.getMessage());
                return null;
            }
        }
    }

    @Transactional
    @Override
    public BookDto deleteBookByBid(long bid) {
        Optional<Book> book = bookRepository.findBookByBidAndDeletedFalse(bid);
        if(book.isEmpty())
            return null;
        else {
            book.get().setDeleted(true);
            bookRepository.save(book.get());
            return mapToBookDto(book.get());
        }
    }

    private static BookDto mapToBookDto(Book book){
        return BookDto.builder()
                .bid(book.getBid())
                .price(book.getPrice())
                .intro(book.getIntro())
                .name(book.getName())
                .author(book.getAuthor())
                .status(book.getStatus())
                .imagePath(book.getImagePath())
                .tag(book.getTag().getName())
                .isbn(book.getISBN())
                .build();
    }

    private Book mapToBook(BookDto bookDto){
        Tag tag = this.tagRepository.findTagByName(bookDto.getTag()).orElseGet(()->{
            Tag newTag = Tag.builder().name(bookDto.getTag()).build();
            return tagRepository.save(newTag);
        });

        Book book = Book.builder()
                .ISBN(bookDto.getIsbn())
                .name(bookDto.getName())
                .status(bookDto.getStatus())
                .imagePath(bookDto.getImagePath())
                .intro(bookDto.getIntro())
                .price(bookDto.getPrice())
                .author(bookDto.getAuthor())
                .tag(tag)
                .build();
        return book;
    }
 }
