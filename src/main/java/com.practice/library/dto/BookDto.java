package com.practice.library.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BookDto {
    private long id;
    private String name;
    private BookAuthorDto author;
    private BookGenreDto genre;
    private List<CommentDto> comments = new ArrayList<>();
}
