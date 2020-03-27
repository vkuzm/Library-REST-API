package com.practice.library.repository;

import com.practice.library.domain.Comment;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class CommentRepositoryImpl implements CommentRepository {

  @PersistenceContext
  private EntityManager em;

  @Override
  public Optional<Comment> insert(Comment comment) {
    em.persist(comment);
    return Optional.of(comment);
  }

  @Override
  public List<Comment> findByBookId(long bookId) {
    TypedQuery<Comment> query = em
        .createQuery("select c from Comment c, Book b where b.id = :bookId",
            Comment.class);

    query.setParameter("bookId", bookId);
    return query.getResultList();
  }

  @Override
  public void remove(long id) {
    int result = em.createQuery("delete from Comment c where c.id = :id")
        .setParameter("id", id)
        .executeUpdate();

    if (result == 0) {
      throw new OptimisticLockException("Comment modified concurrently");
    }
  }

}
