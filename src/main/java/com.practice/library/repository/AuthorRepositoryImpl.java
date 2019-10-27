package com.practice.library.repository;

import com.practice.library.domain.Author;
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
public class AuthorRepositoryImpl implements AuthorRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Author> insert(Author author) {
        em.persist(author);
        return Optional.of(author);
    }

    @Override
    public void update(Author author) {
        em.merge(author);
    }

    @Override
    public List<Author> findAll() {
        TypedQuery<Author> query = em.createQuery("select a from Author a",
                Author.class);

        return query.getResultList();
    }

    @Override
    public Optional<Author> findById(long id) {
        TypedQuery<Author> query = em.createQuery("select a from Author a where a.id = :id",
                Author.class);

        query.setParameter("id", id);

        return Optional.of(query.getSingleResult());
    }

    @Override
    public Optional<Author> findByName(String name) {
        TypedQuery<Author> query = em.createQuery("select a from Author a where a.name like :name",
                Author.class);

        query.setParameter("name", name);

        return Optional.of(query.getSingleResult());
    }

    @Override
    public void delete(long id) {
        int result = em.createQuery("delete from Author a where a.id = :id")
                .setParameter("id", id)
                .executeUpdate();

        if (result == 0) {
            throw new OptimisticLockException("Author modified concurrently");
        }

    }
}
