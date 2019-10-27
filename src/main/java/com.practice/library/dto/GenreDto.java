package com.practice.library.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GenreDto {
    private long id;
    private String name;
    private List<BookDto> books = new ArrayList<>();
}