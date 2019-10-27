package com.practice.library.repository;

import com.practice.library.domain.Book;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class BookRepositoryImpl implements BookRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Book> findById(long id) {
        TypedQuery<Book> query = em.createQuery("select b from Book b where b.id = :id",
                Book.class);

        query.setParameter("id", id);

        return Optional.of(query.getSingleResult());
    }

    @Override
    public Optional<Book> findByName(String name) {
        TypedQuery<Book> query = em.createQuery("select b from Book b where b.name like :name",
                Book.class);

        query.setParameter("name", name);

        return Optional.of(query.getSingleResult());
    }

    @Override
    public List<Book> findAll() {
        TypedQuery<Book> query = em.createQuery("select b from Book b",
                Book.class);

        return query.getResultList();
    }

    @Override
    public Optional<Book> insert(Book book) {
        em.persist(book);
        return Optional.of(book);
    }

    @Override
    public void update(Book book) {
        em.merge(book);
    }

    @Override
    public void delete(long id) {
        int result = em.createQuery("delete from Book b where b.id = :id")
                .setParameter("id", id)
                .executeUpdate();

        if (result == 0) {
            throw new OptimisticLockException("Book modified concurrently");
        }
    }
}
