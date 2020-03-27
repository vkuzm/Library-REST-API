package com.practice.library.repository;

import com.practice.library.domain.Book;

import java.io.File;
import java.util.List;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

public interface BookRepository {
    Optional<Book> findById(long id);

    Optional<Book> findByName(String name);

    List<Book> findAll();

    Optional<Book> insert(Book book, MultipartFile image);

    Optional<Book> update(Book book, MultipartFile image);

    void delete(long id);
}
