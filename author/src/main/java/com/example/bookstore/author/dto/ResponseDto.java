package com.example.bookstore.author.dto;

public record ResponseDto<T>(boolean valid, String message, T resource) {}
