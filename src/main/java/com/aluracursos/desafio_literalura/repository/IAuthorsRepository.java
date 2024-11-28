package com.aluracursos.desafio_literalura.repository;

import com.aluracursos.desafio_literalura.models.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IAuthorsRepository extends JpaRepository<Author, Long> {
    Author findByNameIgnoreCase(String name);
    List<Author> findByYearOfBirthLessThanEqualAndYearDeathGreaterThanEqual(int initialYear, int yearFinal);
}
