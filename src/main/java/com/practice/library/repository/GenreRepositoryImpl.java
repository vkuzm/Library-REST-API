package com.practice.library.repository;

import com.practice.library.domain.Genre;
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
public class GenreRepositoryImpl implements GenreRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Genre> insert(Genre genre) {
        em.persist(genre);
        return Optional.of(genre);
    }

    @Override
    public void update(Genre genre) {
        em.merge(genre);
    }

    @Override
    public List<Genre> findAll() {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g",
                Genre.class);

        return query.getResultList();
    }

    @Override
    public Optional<Genre> findById(long id) {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g where g.id = :id",
                Genre.class);

        query.setParameter("id", id);

        return Optional.of(query.getSingleResult());
    }

    @Override
    public Optional<Genre> findByName(String name) {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g where g.name like :name",
                Genre.class);

        query.setParameter("name", name);

        return Optional.of(query.getSingleResult());
    }

    @Override
    public void delete(long id) {
        int result = em.createQuery("delete from Genre g where g.id = :id")
                .setParameter("id", id)
                .executeUpdate();

        if (result == 0) {
            throw new OptimisticLockException("Genre modified concurrently");
        }

    }
}
