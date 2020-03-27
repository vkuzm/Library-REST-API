package com.practice.library.dto;

import lombok.Data;

@Data
public class CommentDto {
    private long id;
    private String author;
    private String comment;
    private long bookId;
}
