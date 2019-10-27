package com.practice.library.repository;

import com.practice.library.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {
    Optional<Genre> findById(long id);

    Optional<Genre> findByName(String name);

    List<Genre> findAll();

    Optional<Genre> insert(Genre genre);

    void update(Genre genre);

    void delete(long id);
}
