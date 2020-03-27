package com.practice.library.repository;

import com.practice.library.domain.Book;
import com.practice.library.service.AmazonS3Client;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Repository
@Transactional
public class BookRepositoryImpl implements BookRepository {

  @PersistenceContext
  private EntityManager em;

  private final AmazonS3Client amazonS3Client;

  public BookRepositoryImpl(AmazonS3Client amazonS3Client) {
    this.amazonS3Client = amazonS3Client;
  }

  @Override
  public Optional<Book> findById(long id) {
    TypedQuery<Book> query
        = em.createQuery("select b from Book b where b.id = :id", Book.class);
    query.setParameter("id", id);

    return Optional.of(query.getSingleResult());
  }

  @Override
  public Optional<Book> findByName(String name) {
    TypedQuery<Book> query
        = em.createQuery("select b from Book b where b.name like :name", Book.class);
    query.setParameter("name", name);

    return Optional.of(query.getSingleResult());
  }

  @Override
  public List<Book> findAll() {
    TypedQuery<Book> query = em.createQuery("select b from Book b", Book.class);
    return query.getResultList();
  }

  @Override
  public Optional<Book> insert(Book book, MultipartFile image) {
    if (image != null && !image.isEmpty()) {
      String imagePath = amazonS3Client.uploadFile(image);
      book.setImageName(image.getOriginalFilename());
      book.setImagePath(imagePath);
    }
    em.persist(book);
    return Optional.of(book);
  }

  @Override
  public Optional<Book> update(Book book, MultipartFile image) {
    if (image != null && !image.isEmpty()) {
      String imagePath = amazonS3Client.uploadFile(image);
      book.setImageName(image.getOriginalFilename());
      book.setImagePath(imagePath);
    }
    return Optional.of(em.merge(book));
  }

  @Override
  public void delete(long id) {
    Optional<Book> book = this.findById(id);

    int result = em.createQuery("delete from Book b where b.id = :id")
        .setParameter("id", id)
        .executeUpdate();

    if (result == 0) {
      throw new OptimisticLockException("Book modified concurrently");
    } else {
      if (book.isPresent() && book.get().getImageName().length() > 0) {
        amazonS3Client.deleteFileFromBucket(book.get().getImageName());
      }
    }
  }

}
