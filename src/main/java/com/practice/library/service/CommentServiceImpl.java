package com.practice.library.service;

import com.practice.library.domain.Book;
import com.practice.library.domain.Comment;
import com.practice.library.dto.CommentDto;
import com.practice.library.repository.BookRepository;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class CommentServiceImpl implements CommentService{

  @Value("${amazonProperties.sqsCommentQueue}")
  private String sqsCommentQueue;

  @PersistenceContext
  private EntityManager entityManager;

  private final AmazonSQSClient amazonSQSClient;
  private final BookRepository bookRepository;

  public CommentServiceImpl(AmazonSQSClient amazonSQSClient, BookRepository bookRepository) {
    this.amazonSQSClient = amazonSQSClient;
    this.bookRepository = bookRepository;
  }

  @Override
  public void addComment(CommentDto comment) {
    amazonSQSClient.sendMessage(sqsCommentQueue, comment);
  }

  @Scheduled(fixedDelayString = "${amazonSQSProperties.sqsFixedDelay}")
  public void getFromQueueAndPersist() {
    Optional<CommentDto> commentDto = amazonSQSClient.getMessage(sqsCommentQueue, CommentDto.class);
    if (commentDto.isPresent()) {
      Optional<Book> book = bookRepository.findById(commentDto.get().getBookId());

      if (book.isPresent()) {
        Comment comment = new Comment();
        comment.setAuthor(commentDto.get().getAuthor());
        comment.setComment(commentDto.get().getComment());
        comment.setBook(book.get());

        entityManager.persist(comment);
        log.info("Comment: " + comment.getAuthor() + " - " + comment.getComment() + " is persisted!");
      }
    }
  }
}
