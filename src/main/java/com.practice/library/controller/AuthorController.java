package com.practice.library.controller;

import com.practice.library.domain.Author;
import com.practice.library.dto.AuthorDto;
import com.practice.library.repository.AuthorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {
    private final AuthorRepository authorRepo;
    private final ModelMapper modelMapper;

    public AuthorController(AuthorRepository authorRepo, ModelMapper modelMapper) {
        this.authorRepo = authorRepo;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        List<Author> authors = authorRepo.findAll();

        if (!authors.isEmpty()) {
            List<AuthorDto> authorsDto = convertAuthorsToDto(authors);

            return new ResponseEntity<>(authorsDto, HttpStatus.OK);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        Optional<Author> author = authorRepo.findById(id);

        if (author.isPresent()) {
            AuthorDto authorDto = convertToDto(author.get());

            return new ResponseEntity<>(authorDto, HttpStatus.OK);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<AuthorDto> create(@RequestBody AuthorDto authorDto) {
        Author author = convertToEntity(authorDto);
        Optional<Author> createdAuthor = authorRepo.insert(author);

        if (createdAuthor.isPresent()) {
            AuthorDto authorDtoCreated = convertToDto(createdAuthor.get());

            return new ResponseEntity<>(authorDtoCreated, HttpStatus.CREATED);
        }

        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDto> update(
            @PathVariable("id") Long id,
            @RequestBody AuthorDto authorDto) {
        Optional<Author> persistedAuthor = authorRepo.findById(id);

        if (persistedAuthor.isPresent()) {
            Author author = convertToEntity(authorDto);
            authorRepo.update(author);

            return new ResponseEntity<>(null, HttpStatus.OK);
        }

        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        authorRepo.delete(id);
        return ResponseEntity.badRequest().build();
    }

    private AuthorDto convertToDto(Author author) {
        return modelMapper.map(author, AuthorDto.class);
    }

    private Author convertToEntity(AuthorDto authorDto) {
        return modelMapper.map(authorDto, Author.class);
    }

    private List<AuthorDto> convertAuthorsToDto(List<Author> authors) {
        return authors.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
