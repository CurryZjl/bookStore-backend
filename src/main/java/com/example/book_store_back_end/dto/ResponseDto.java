package com.example.book_store_back_end.dto;

public record ResponseDto<T>(boolean valid, String message, T resource) {}
