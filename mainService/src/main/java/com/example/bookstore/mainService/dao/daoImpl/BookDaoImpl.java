package com.example.bookstore.mainService.dao.daoImpl;


import com.example.bookstore.mainService.constants.RedisConstants;
import com.example.bookstore.mainService.dao.BookDao;
import com.example.bookstore.mainService.entity.Book;
import com.example.bookstore.mainService.repositories.BookRepository;
import com.example.bookstore.mainService.utils.CacheClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Repository
public class BookDaoImpl implements BookDao {
    private final BookRepository bookRepository;
    private final CacheClient cacheClient;

    @Override
    public Optional<Book> findBookByBid(long id) {
        //return bookRepository.findBookByBidAndDeletedFalse(id);
        try{
            return cacheClient.queryRedis(RedisConstants.CACHE_BOOK_KEY, id, Book.class, bookRepository::findBookByBidAndDeletedFalse, RedisConstants.CACHE_BOOK_TTL, TimeUnit.MINUTES);
        }catch (Exception e){
            log.warn(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Page<Book> searchBooksByName(String name ,Pageable pageable) {
        return bookRepository.findBooksByNameContainingIgnoreCaseAndDeletedFalse(name , pageable);
    }
}
