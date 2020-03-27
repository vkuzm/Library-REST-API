package com.practice.library;

import com.practice.library.domain.Author;
import com.practice.library.domain.Book;
import com.practice.library.domain.Genre;
import com.practice.library.repository.AuthorRepository;
import com.practice.library.repository.BookRepository;
import com.practice.library.repository.CommentRepository;
import com.practice.library.repository.GenreRepository;
import java.io.File;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Transactional
public class TestData implements CommandLineRunner {
    private final BookRepository bookRepo;
    private final AuthorRepository authorRepo;
    private final GenreRepository genreRepo;
    private final CommentRepository commentRepo;

    public TestData(BookRepository bookRepo, AuthorRepository authorRepo, GenreRepository genreRepo, CommentRepository commentRepo) {
        this.bookRepo = bookRepo;
        this.authorRepo = authorRepo;
        this.genreRepo = genreRepo;
        this.commentRepo = commentRepo;
    }

    @Override
    public void run(String... args) {
        if (authorRepo.findAll().isEmpty()) {
            addAuthor("Max Black");
            addAuthor("John Snow");
            addAuthor("White");
            addAuthor("Tony Stark");
            addAuthor("Unknown");
        }

        if (genreRepo.findAll().isEmpty()) {
            addGenre("Fiction");
            addGenre("History");
            addGenre("Biography");
            addGenre("Crime");
        }

        if (bookRepo.findAll().isEmpty()) {
            addBook("Dragons", "Max Black", "Fiction");
            addBook("Red", "John Snow", "History");
            addBook("Big Thinking", "White", "Biography");
            addBook("Iron Man", "Tony Stark", "Fiction");
            addBook("Okay", "Unknown", "Crime");
        }
    }

    private void addGenre(String name) {
        Genre genre = new Genre();
        genre.setName(name);

        genreRepo.insert(genre);
    }

    private void addAuthor(String name) {
        Author author = new Author();
        author.setName(name);

        authorRepo.insert(author);
    }

    private void addBook(String bookName, String authorName, String genreName) {
        Book book = new Book();
        Optional<Author> author = authorRepo.findByName(authorName);
        Optional<Genre> genre = genreRepo.findByName(genreName);

        author.get().addBook(book);
        genre.get().addBook(book);

        book.setName(bookName);
        book.setAuthor(author.get());
        book.setGenre(genre.get());

        bookRepo.insert(book, null);
    }
}
