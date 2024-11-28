package com.aluracursos.desafio_literalura.repository;

import com.aluracursos.desafio_literalura.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IBooksRepository extends JpaRepository<Book, Long> {
    Book findByTitle(String title);
    List<Book> findByLanguagesContaining(String languages);
}
