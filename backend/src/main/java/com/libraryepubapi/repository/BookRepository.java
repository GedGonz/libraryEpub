package com.libraryepubapi.repository;

import com.libraryepubapi.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Override
    Page<Book> findAll(Pageable pageable);

    @Query(
            value = """
                    select distinct b from Book b
                    left join b.authors a
                    where lower(b.title) like lower(concat('%', :q, '%'))
                       or lower(a.name) like lower(concat('%', :q, '%'))
                    """,
            countQuery = """
                    select count(distinct b) from Book b
                    left join b.authors a
                    where lower(b.title) like lower(concat('%', :q, '%'))
                       or lower(a.name) like lower(concat('%', :q, '%'))
                    """
    )
    Page<Book> searchByTitleOrAuthorName(@Param("q") String q, Pageable pageable);

    @EntityGraph(attributePaths = {"authors", "labels"})
    Optional<Book> findWithAuthorsAndLabelsByBookId(Long bookId);

    @Query(
            value = """
                    select distinct b from Book b
                    where :prefix = '' or lower(b.title) like lower(concat(:prefix, '%'))
                    """,
            countQuery = """
                    select count(distinct b) from Book b
                    where :prefix = '' or lower(b.title) like lower(concat(:prefix, '%'))
                    """
    )
    Page<Book> findByTitlePrefix(@Param("prefix") String prefix, Pageable pageable);

    @Query(
            value = """
                    select distinct b from Book b
                    join b.authors a
                    where a.authorId = :authorId
                      and (:prefix = '' or lower(b.title) like lower(concat(:prefix, '%')))
                    """,
            countQuery = """
                    select count(distinct b) from Book b
                    join b.authors a
                    where a.authorId = :authorId
                      and (:prefix = '' or lower(b.title) like lower(concat(:prefix, '%')))
                    """
    )
    Page<Book> findByAuthorIdAndTitlePrefix(
            @Param("authorId") int authorId,
            @Param("prefix") String prefix,
            Pageable pageable
    );

    @Query(
            value = """
                    select distinct b from Book b
                    join b.labels l
                    where l.labelId = :labelId
                      and (:prefix = '' or lower(b.title) like lower(concat(:prefix, '%')))
                    """,
            countQuery = """
                    select count(distinct b) from Book b
                    join b.labels l
                    where l.labelId = :labelId
                      and (:prefix = '' or lower(b.title) like lower(concat(:prefix, '%')))
                    """
    )
    Page<Book> findByLabelIdAndTitlePrefix(
            @Param("labelId") int labelId,
            @Param("prefix") String prefix,
            Pageable pageable
    );
}

