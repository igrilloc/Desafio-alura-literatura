package com.aluracursos.desafio_literalura.models;

import jakarta.persistence.*;

import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @Column(name = "name_author")
    private String nameAuthor;

    @Column(name = "languages")
    private String languages;
    private double downloadCount;

    public Book() {}

    public Book(DataBooks dataBooks, Author author) {
        this.title = dataBooks.title();
        setLanguages(dataBooks.languages());
        this.downloadCount = dataBooks.downloadCount();
        this.nameAuthor = author.getName();
        this.author = author;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public List<String> getLanguages() {
        return Arrays.asList(languages.split(","));
    }

    public void setLanguages(List<String> languages) {
        this.languages = String.join(",", languages);
    }

    public double getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(double downloadCount) {
        this.downloadCount = downloadCount;
    }

    @Override
    public String toString() {
        return "--------------- LIBRO 📖 ---------------" + "\n" +
                "Título: " + title + "\n" +
                "Autor: " + nameAuthor + "\n" +
                "Idioma: " + languages + "\n" +
                "Número de descargas: " + downloadCount + "\n" +
                "------------------------------------" + "\n";
    }
}
