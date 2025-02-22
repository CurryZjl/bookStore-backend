package com.example.bookstore.mainService.constants;

public class RedisConstants {
    /* TTLs */
    public static final Long CACHE_NULL_TTL = 2L;
    public static final Long CACHE_BOOK_TTL = 30L;

    /* keys */
    public static final String CACHE_BOOK_KEY = "cache:book:";
}
