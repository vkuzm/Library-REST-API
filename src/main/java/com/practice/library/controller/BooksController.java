package com.practice.library.controller;

import com.practice.library.domain.Book;
import com.practice.library.domain.Comment;
import com.practice.library.dto.BookDto;
import com.practice.library.dto.CommentDto;
import com.practice.library.repository.BookRepository;
import com.practice.library.repository.CommentRepository;
import com.practice.library.service.CommentService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/books")
public class BooksController {

  private final BookRepository bookRepo;
  private final CommentRepository commentRepo;
  private final CommentService commentService;
  private final ModelMapper modelMapper;

  @Autowired
  public BooksController(BookRepository bookRepo, CommentRepository commentRepo,
      CommentService commentService, ModelMapper modelMapper) {
    this.bookRepo = bookRepo;
    this.commentRepo = commentRepo;
    this.commentService = commentService;
    this.modelMapper = modelMapper;
  }

  @GetMapping
  public ResponseEntity<List<BookDto>> findAll() {
    List<Book> books = bookRepo.findAll();

    if (!books.isEmpty()) {
      List<BookDto> booksDto = convertBooksToDto(books);
      return new ResponseEntity<>(booksDto, HttpStatus.OK);
    }
    return ResponseEntity.notFound().build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<BookDto> findById(@PathVariable("id") Long id) {
    Optional<Book> book = bookRepo.findById(id);

    if (book.isPresent()) {
      BookDto bookDto = convertToDto(book.get());
      return new ResponseEntity<>(bookDto, HttpStatus.OK);
    }
    return ResponseEntity.notFound().build();
  }

  @PostMapping
  public ResponseEntity<BookDto> create(
      @RequestPart("book") BookDto bookDto,
      @RequestPart("image") MultipartFile image) {
    Book book = convertToEntity(bookDto);
    Optional<Book> createdBook = bookRepo.insert(book, image);

    if (createdBook.isPresent()) {
      BookDto bookDtoCreated = convertToDto(createdBook.get());
      return new ResponseEntity<>(bookDtoCreated, HttpStatus.CREATED);
    }
    return ResponseEntity.badRequest().build();
  }

  @PutMapping
  public ResponseEntity<BookDto> update(
      @RequestPart("book") BookDto bookDto,
      @RequestPart("image") MultipartFile image) {
    Optional<Book> persistedBook = bookRepo.findById(bookDto.getId());

    if (persistedBook.isPresent()) {
      Book book = convertToEntity(bookDto);
      Optional<Book> updatedBook = bookRepo.update(book, image);

      if (updatedBook.isPresent()) {
        BookDto bookDtoUpdated = convertToDto(updatedBook.get());
        return ResponseEntity.ok(bookDtoUpdated);
      }
    }
    return ResponseEntity.badRequest().build();
  }

  /*
  @PostMapping("/{id}/addComment")
  public ResponseEntity<CommentDto> addComment(
      @PathVariable("id") Long id,
      @RequestBody CommentDto commentDto) {
    Optional<Book> persistedBook = bookRepo.findById(id);

    if (persistedBook.isPresent()) {
      Comment comment = convertToEntity(commentDto);
      comment.setBook(persistedBook.get());

      Optional<Comment> savedComment = commentRepo.insert(comment);
      if (savedComment.isPresent()) {
        CommentDto commentDtoCreated = convertToDto(savedComment.get());
        return new ResponseEntity<>(commentDtoCreated, HttpStatus.CREATED);
      }
    }
    return ResponseEntity.badRequest().build();
  }
  */

  @PostMapping("/{id}/addComment")
  public ResponseEntity<CommentDto> addComment(
      @PathVariable("id") Long bookId,
      @RequestBody CommentDto commentDto) {

    if (commentDto != null) {
      commentDto.setBookId(bookId);
      commentService.addComment(commentDto);
      return new ResponseEntity<>(commentDto, HttpStatus.CREATED);
    }
    return ResponseEntity.badRequest().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> delete(@PathVariable("id") Long id) {
    bookRepo.delete(id);
    return ResponseEntity.ok().build();
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