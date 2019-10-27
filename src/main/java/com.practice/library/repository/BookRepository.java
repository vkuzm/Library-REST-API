package com.practice.library.repository;

import com.practice.library.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Optional<Book> findById(long id);

    Optional<Book> findByName(String name);

    List<Book> findAll();

    Optional<Book> insert(Book book);

    void update(Book book);

    void delete(long id);
}
