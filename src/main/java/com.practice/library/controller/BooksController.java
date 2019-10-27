package com.practice.library.controller;

import com.practice.library.domain.Book;
import com.practice.library.domain.Comment;
import com.practice.library.dto.BookDto;
import com.practice.library.dto.CommentDto;
import com.practice.library.repository.BookRepository;
import com.practice.library.repository.CommentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
public class BooksController {
    private final BookRepository bookRepo;
    private final CommentRepository commentRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public BooksController(BookRepository bookRepo, CommentRepository commentRepo, ModelMapper modelMapper) {
        this.bookRepo = bookRepo;
        this.commentRepo = commentRepo;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        List<Book> books = bookRepo.findAll();

        if (!books.isEmpty()) {
            List<BookDto> booksDto = convertBooksToDto(books);

            return new ResponseEntity<>(booksDto, HttpStatus.OK);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        Optional<Book> book = bookRepo.findById(id);

        if (book.isPresent()) {
            BookDto bookDto = convertToDto(book.get());

            return new ResponseEntity<>(bookDto, HttpStatus.OK);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<BookDto> create(@RequestBody BookDto bookDto) {
        Book book = convertToEntity(bookDto);
        Optional<Book> createdBook = bookRepo.insert(book);

        if (createdBook.isPresent()) {
            BookDto bookDtoCreated = convertToDto(createdBook.get());

            return new ResponseEntity<>(bookDtoCreated, HttpStatus.CREATED);
        }

        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("id") Long id,
            @RequestBody BookDto bookDto) {
        Optional<Book> persistedBook = bookRepo.findById(id);

        if (persistedBook.isPresent()) {
            Book book = convertToEntity(bookDto);
            bookRepo.update(book);

            return new ResponseEntity<>(null, HttpStatus.OK);
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/{id}/addComment")
    public ResponseEntity<?> addComment(
            @PathVariable("id") Long id,
            @RequestBody CommentDto commentDto) {
        Optional<Book> persistedBook = bookRepo.findById(id);

        if (persistedBook.isPresent()) {
            Comment comment = convertToEntity(commentDto);
            comment.setBook(persistedBook.get());

            Optional<Comment> savedComment = commentRepo.insert(comment);
            if (savedComment.isPresent()) {
                CommentDto CommentDtoCreated = convertToDto(savedComment.get());

                return new ResponseEntity<>(CommentDtoCreated, HttpStatus.CREATED);
            }
        }

        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        bookRepo.delete(id);
        return ResponseEntity.badRequest().build();
    }

    private BookDto convertToDto(Book book) {
        return modelMapper.map(book, BookDto.class);
    }

    private Book convertToEntity(BookDto bookDto) {
        return modelMapper.map(bookDto, Book.class);
    }

    private CommentDto convertToDto(Comment comment) {
        return modelMapper.map(comment, CommentDto.class);
    }

    private Comment convertToEntity(CommentDto commentDto) {
        return modelMapper.map(commentDto, Comment.class);
    }

    private List<BookDto> convertBooksToDto(List<Book> books) {
        return books.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}