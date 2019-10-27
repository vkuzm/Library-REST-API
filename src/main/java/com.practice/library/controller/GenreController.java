package com.practice.library.controller;

import com.practice.library.domain.Genre;
import com.practice.library.dto.GenreDto;
import com.practice.library.repository.GenreRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/genres")
public class GenreController {
    private final GenreRepository genreRepo;
    private final ModelMapper modelMapper;

    public GenreController(GenreRepository genreRepo, ModelMapper modelMapper) {
        this.genreRepo = genreRepo;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        List<Genre> genres = genreRepo.findAll();

        if (!genres.isEmpty()) {
            List<GenreDto> genresDto = convertGenresToDto(genres);

            return new ResponseEntity<>(genresDto, HttpStatus.OK);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        Optional<Genre> genre = genreRepo.findById(id);

        if (genre.isPresent()) {
            GenreDto genreDto = convertToDto(genre.get());

            return new ResponseEntity<>(genreDto, HttpStatus.OK);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<GenreDto> create(@RequestBody GenreDto genreDto) {
        Genre genre = convertToEntity(genreDto);
        Optional<Genre> createdGenre = genreRepo.insert(genre);

        if (createdGenre.isPresent()) {
            GenreDto genreDtoCreated = convertToDto(createdGenre.get());

            return new ResponseEntity<>(genreDtoCreated, HttpStatus.CREATED);
        }

        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenreDto> update(
            @PathVariable("id") Long id,
            @RequestBody GenreDto genreDto) {

        Optional<Genre> persistedGenre = genreRepo.findById(id);

        if (persistedGenre.isPresent()) {
            Genre genre = convertToEntity(genreDto);
            genreRepo.update(genre);

            return new ResponseEntity<>(null, HttpStatus.OK);
        }

        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        genreRepo.delete(id);
        return ResponseEntity.badRequest().build();
    }

    private GenreDto convertToDto(Genre genre) {
        return modelMapper.map(genre, GenreDto.class);
    }

    private Genre convertToEntity(GenreDto genreDto) {
        return modelMapper.map(genreDto, Genre.class);
    }

    private List<GenreDto> convertGenresToDto(List<Genre> genres) {
        return genres.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
