package com.practice.library.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties("books")
public class BookGenreDto extends GenreDto {
    private long id;
    private String name;
}
