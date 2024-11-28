package com.aluracursos.desafio_literalura.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "author")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private int yearOfBirth;
    private int yearDeath;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Book> books = new ArrayList<>();

    public Author(DataAuthors dataAuthors) {
        this.name = dataAuthors.nameAuthor();
        this.yearOfBirth = dataAuthors.yearOfBirth();
        this.yearDeath = dataAuthors.yearDeath();
    }

    public Author() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public int getYearDeath() {
        return yearDeath;
    }

    public void setYearDeath(int yearDeath) {
        this.yearDeath = yearDeath;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }


    @Override
    // Obtener solo el título de los libros
    public String toString() {
        StringBuilder booksTitle = new StringBuilder();
        for (Book book : books) {
            booksTitle.append(book.getTitle()).append(", ");
        }

        // Eliminar la última coma y espacio
        if (booksTitle.length() > 0) {
            booksTitle.setLength(booksTitle.length() - 2);
        }

        return  "--------------- AUTOR 👨‍🏫 ---------------" + "\n" +
                "Autor: " + name + "\n" +
                "Fecha de nacimiento: " + yearOfBirth + "\n" +
                "Fecha de fallecimiento: " + yearDeath + "\n" +
                "Libros: " + booksTitle + "\n";
    }
}
