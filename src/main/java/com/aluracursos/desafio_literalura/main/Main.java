package com.aluracursos.desafio_literalura.main;

import com.aluracursos.desafio_literalura.models.*;
import com.aluracursos.desafio_literalura.repository.IAuthorsRepository;
import com.aluracursos.desafio_literalura.repository.IBooksRepository;
import com.aluracursos.desafio_literalura.service.CallAPI;
import com.aluracursos.desafio_literalura.service.TransformData;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private Scanner scanner = new Scanner(System.in);
    private CallAPI callAPI = new CallAPI();
    private TransformData transformData = new TransformData();
    private final static String URL_BASE = "https://gutendex.com/books/?search=";

    private IAuthorsRepository iAuthorsRepository;
    private IBooksRepository iBooksRepository;

    public Main(IAuthorsRepository iAuthorsRepository, IBooksRepository iBooksRepository) {
        this.iAuthorsRepository = iAuthorsRepository;
        this.iBooksRepository = iBooksRepository;
    }

    public void showMenu() {

        var option = -1;
        System.out.println("Bienvenido! Por favor selecciona una opción: ");

        while (option != 0) {
            var menu = """
                    1 - | Buscar libros por título | 📕
                    2 - | Listar libros registrados | ✍️
                    3 - | Listar autores registrados | 👨‍🏫
                    4 - | Listar autores vivos en un determinado año | ⌛
                    5 - | Listar libros por idioma | ℹ️
                    6 - | Top 10 libros más descargados | 🔝
                    7 - | Obtener estadísiticas | 📊
                    0 - | Salir | 👋
                    """;

            System.out.println(menu);
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    displayGetBooks();
                    break;
                case 2:
                    displayRegisteredBooks();
                    break;
                case 3:
                    displayRegisteredAuthors();
                    break;
                case 4:
                    displayAuthorsByYear();
                    break;
                case 5:
                    displayListByLanguage();
                    break;
                case 6:
                    displayTopTenBooks();
                    break;
                case 7:
                    displayStatisticsApi();
                    break;
                    case 0:
                        System.out.println("Cerrando aplicación...");
                        break;

                default:
                    System.out.println("Opción no válida, intenta de nuevo");
            }

        }
    }

    private Data getDataBooks() {
        var nameBook = scanner.nextLine();
        var json = callAPI.getDataAPI(URL_BASE + nameBook.replace(" ", "+"));
        Data dataBook = transformData.getData(json, Data.class);
        return dataBook;
    }

    private Book createBook(DataBooks dataBooks, Author author) {
        if (author != null) {
            return new Book(dataBooks, author);
        } else {
            System.out.println("El autor es null, no se puede crear el libro");
            return null;
        }
    }

    private  void displayGetBooks() {

        System.out.println("Escribe el libro que deseas buscar: ");
        Data dataAllBooks = getDataBooks();

        if (!dataAllBooks.resultados().isEmpty()) {

            DataBooks dataBook = dataAllBooks.resultados().get(0);
            DataAuthors dataAuthor = dataBook.authors().get(0);
            Book book = null;
            Book iBooksRepositoryByTitle = iBooksRepository.findByTitle(dataBook.title());

            if (iBooksRepositoryByTitle != null) {
                System.out.println("Este libro ya se encuentra en la base de datos");
                System.out.println(iBooksRepositoryByTitle.toString());
            } else {

                Author iAuthorsRepositoryByNameIgnoreCase = iAuthorsRepository.findByNameIgnoreCase(dataBook.authors().get(0).nameAuthor());

                if (iAuthorsRepositoryByNameIgnoreCase != null) {
                    book = createBook(dataBook, iAuthorsRepositoryByNameIgnoreCase);
                    iBooksRepository.save(book);
                    System.out.println("----- LIBRO AGREGADO -----\n");
                    System.out.println(book);
                } else {
                    Author author = new Author(dataAuthor);
                    author = iAuthorsRepository.save(author);
                    book = createBook(dataBook, author);
                    iBooksRepository.save(book);
                    System.out.println("----- LIBRO AGREGADO -----\n");
                    System.out.println(book);
                }
            }

        } else {
            System.out.println("El libro no existe en la API de Gutendex, ingresa otro");
        }
    }

    private void displayRegisteredBooks() {

        List<Book> iBooksRepositoryAll = iBooksRepository.findAll();

        if (iBooksRepositoryAll.isEmpty()) {
            System.out.println("No hay libros registrados");
            return;
        }

        System.out.println("----- LOS LIBROS REGISTRADOS SON: -----\n");
        iBooksRepositoryAll.stream()
                .sorted(Comparator.comparing(Book::getTitle))
                .forEach(System.out::println);
    }

    private void displayRegisteredAuthors() {

        List<Author> iAuthorsRepositoryAll = iAuthorsRepository.findAll();

        if (iAuthorsRepositoryAll.isEmpty()) {
            System.out.println("No hay autores registrados");
            return;
        }

        System.out.println("----- LOS AUTORES REGISTRADOS SON: -----\n");
        iAuthorsRepositoryAll.stream()
                .sorted(Comparator.comparing(Author::getName))
                .forEach(System.out::println);
    }

    private void displayAuthorsByYear() {

        System.out.println("Escribe el año en el que deseas buscar: ");
        var year = scanner.nextInt();
        scanner.nextLine();

        if(year < 0) {
            System.out.println("El año no puede ser negativo, intenta de nuevo");
            return;
        }

        List<Author> authorByYear = iAuthorsRepository.findByYearOfBirthLessThanEqualAndYearDeathGreaterThanEqual(year, year);

        if (authorByYear.isEmpty()) {
            System.out.println("No hay autores registrados en ese año");
            return;
        }

        System.out.println("----- LOS AUTORES VIVOS REGISTRADOS EN EL AÑO " + year + " SON: -----\n");
        authorByYear.stream()
                .sorted(Comparator.comparing(Author::getName))
                .forEach(System.out::println);
    }

    private void displayListByLanguage() {

        System.out.println("Escribe el idioma por el que deseas buscar: ");
        String menu = """
                es - Español
                en - Inglés
                fr - Francés
                pt - Portugués
                """;

        System.out.println(menu);
        var languageOption = scanner.nextLine();

        if (!languageOption.equals("es") && !languageOption.equals("en") && !languageOption.equals("fr") && !languageOption.equals("pt")) {
            System.out.println("Idioma no válido, intenta de nuevo");
            return;
        }

        List<Book> iBooksRepositoryByLanguagesContaining = iBooksRepository.findByLanguagesContaining(languageOption);

        if (iBooksRepositoryByLanguagesContaining.isEmpty()) {
            System.out.println("No hay libros registrados en ese idioma");
            return;
        }

        System.out.println("----- LOS LIBROS REGISTRADOS EN EL IDIOMA SELECCIONADO SON: -----\n");
        iBooksRepositoryByLanguagesContaining.stream()
                .sorted(Comparator.comparing(Book::getTitle))
                .forEach(System.out::println);
    }

    private void displayTopTenBooks() {

        System.out.println("De donde quieres obtener los libros más descargados?");
        String menu = """
                1 - Gutendex
                2 - Base de datos
                """;

        System.out.println(menu);
        var option = scanner.nextInt();
        scanner.nextLine();

        if (option == 1) {
            System.out.println("----- LOS 10 LIBROS MÁS DESCARGADOS EN GUTENDEX SON: -----\n");

            var json = callAPI.getDataAPI(URL_BASE);
            Data dataTransform = transformData.getData(json, Data.class);

            List<Book> bookList = new ArrayList<>();

            for (DataBooks dataBooks : dataTransform.resultados()) {
                if (!dataBooks.authors().isEmpty()) {
                    Author author = new Author(dataBooks.authors().get(0));
                    Book book = new Book(dataBooks, author);
                    bookList.add(book);
                } else {
                    System.out.println("Advertencia: El libro '" + dataBooks.title() + "' no tiene autores registrados.");
                }
            }

            bookList.stream()
                    .sorted(Comparator.comparing(Book::getDownloadCount).reversed())
                    .limit(10)
                    .forEach(System.out::println);

        } else if (option == 2) {
            System.out.println("----- LOS 10 LIBROS MÁS DESCARGADOS EN LA BASE DE DATOS SON: -----\n");

            List<Book> iBooksRepositoryAll = iBooksRepository.findAll();

            if (iBooksRepositoryAll.isEmpty()) {
                System.out.println("No hay libros registrados");
                return;
            }

            iBooksRepositoryAll.stream()
                    .sorted(Comparator.comparing(Book::getDownloadCount).reversed())
                    .limit(10)
                    .forEach(System.out::println);

        } else {
            System.out.println("Opción no válida, intenta de nuevo");
        }
    }

    private void displayStatisticsApi() {

        System.out.println("De donde quieres obtener las estadísiticas: ");
        String menu = """
                1 - Gutendex
                2 - Base de datos
                """;

        System.out.println(menu);
        var option = scanner.nextInt();
        scanner.nextLine();

        if (option == 1) {

            System.out.println("----- ESTADÍSTICAS DE DESCARGAS EN GUTENDEX -----\n");
            var json = callAPI.getDataAPI(URL_BASE);
            Data dataTransform = transformData.getData(json, Data.class);
            DoubleSummaryStatistics summaryStatistics = dataTransform.resultados().stream()
                    .collect(Collectors.summarizingDouble(DataBooks::downloadCount));

            System.out.println("📈 Libro con más descargas: " + summaryStatistics.getMax());
            System.out.println("📉 Libro con menos descargas: " + summaryStatistics.getMin());
            System.out.println("📊 Promedio de descargas: " + summaryStatistics.getAverage());
            System.out.println("\n");

        } else if (option == 2) {

            System.out.println("----- ESTADÍSTICAS DE DESCARGAS EN LA BASE DE DATOS -----\n");
            List<Book> iBooksRepositoryAll = iBooksRepository.findAll();

            if (iBooksRepositoryAll.isEmpty()) {
                System.out.println("No hay libros registrados");
                return;
            }

            DoubleSummaryStatistics summaryStatistics = iBooksRepositoryAll.stream()
                    .collect(Collectors.summarizingDouble(Book::getDownloadCount));
            System.out.println("📈 Libro con más descargas: " + summaryStatistics.getMax());
            System.out.println("📉 Libro con menos descargas: " + summaryStatistics.getMin());
            System.out.println("📊 Promedio de descargas: " + summaryStatistics.getAverage());
            System.out.println("\n");

        } else {
            System.out.println("Opción no válida, intenta de nuevo");
        }
    }
}
