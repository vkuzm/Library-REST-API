package com.practice.library.repository;

import com.practice.library.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Optional<Comment> insert(Comment comment);

    List<Comment> findByBookId(long bookId);
}
