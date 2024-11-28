package com.aluracursos.desafio_literalura.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DataAuthors(
        @JsonAlias("name") String nameAuthor,
        @JsonAlias("birth_year") int yearOfBirth,
        @JsonAlias("death_year") int yearDeath
) {
}
