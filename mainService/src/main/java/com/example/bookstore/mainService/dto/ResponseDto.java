package com.example.bookstore.mainService.dto;

public record ResponseDto<T>(boolean valid, String message, T resource) {}
