package com.example.bookstore.mainService.repositories;

import com.example.bookstore.mainService.entity.BookInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookInfoRepository extends MongoRepository<BookInfo, Long> {
}
