package com.practice.library.repository;

import com.practice.library.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    Optional<Author> findById(long id);

    Optional<Author> findByName(String name);

    List<Author> findAll();

    Optional<Author> insert(Author author);

    void update(Author author);

    void delete(long id);
}

