package com.sdd.booksapi.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.sdd.booksapi.domain.Book;
import com.sdd.booksapi.repository.BookRepository;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class BookControllerIT extends AbstractIntegrationTest {

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
    }

    @Test
    void shouldCreateAndGetBook() {
        String requestBody = """
                {
                    "titulo": "Clean Code",
                    "autor": "Robert C. Martin",
                    "isbn": "9780132350884",
                    "dataPublicacao": "2008-08-01"
                }
                """;

        // Create Book
        Integer bookId = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/v1/books")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("titulo", equalTo("Clean Code"))
                .extract().path("id");

        // Get Book
        given()
                .when()
                .get("/api/v1/books/" + bookId)
                .then()
                .statusCode(200)
                .body("titulo", equalTo("Clean Code"));
    }

    @Test
    void shouldGetAllBooks() {
        Book book1 = new Book();
        book1.setTitulo("Book 1");
        book1.setAutor("Author 1");
        book1.setIsbn("111111");
        book1.setDataPublicacao(LocalDate.now());

        Book book2 = new Book();
        book2.setTitulo("Book 2");
        book2.setAutor("Author 2");
        book2.setIsbn("222222");
        book2.setDataPublicacao(LocalDate.now());
        
        bookRepository.save(book1);
        bookRepository.save(book2);

        given()
                .when()
                .get("/api/v1/books")
                .then()
                .statusCode(200)
                .body("$", hasSize(2))
                .body("[0].titulo", equalTo("Book 1"))
                .body("[1].titulo", equalTo("Book 2"));
    }

    @Test
    void shouldReturn404WhenGetNonExistentBookById() {
        given()
                .when()
                .get("/api/v1/books/999")
                .then()
                .statusCode(404);
    }

    @Test
    void shouldReturn400WhenCreateBookWithInvalidData() {
        String requestBody = """
                {
                    "titulo": "",
                    "autor": "Robert C. Martin",
                    "isbn": "9780132350884"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/v1/books")
                .then()
                .statusCode(400);
    }

    @Test
    void shouldUpdateBook() {
        Book book = new Book();
        book.setTitulo("Old Title");
        book.setAutor("Author");
        book.setIsbn("123456");
        book.setDataPublicacao(LocalDate.now());
        book = bookRepository.save(book);

        String updateRequest = """
                {
                    "titulo": "New Title",
                    "autor": "Author",
                    "isbn": "123456",
                    "dataPublicacao": "2024-01-01"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(updateRequest)
                .when()
                .put("/api/v1/books/" + book.getId())
                .then()
                .statusCode(200)
                .body("titulo", equalTo("New Title"));
    }

    @Test
    void shouldDeleteBook() {
        Book book = new Book();
        book.setTitulo("To be deleted");
        book.setAutor("Author");
        book.setIsbn("123456");
        book.setDataPublicacao(LocalDate.now());
        book = bookRepository.save(book);

        given()
                .when()
                .delete("/api/v1/books/" + book.getId())
                .then()
                .statusCode(204);

        // Verify it was deleted
        given()
                .when()
                .get("/api/v1/books/" + book.getId())
                .then()
                .statusCode(404);
    }

    @Test
    void shouldReturn404WhenDeleteNonExistentBook() {
        given()
                .when()
                .delete("/api/v1/books/999")
                .then()
                .statusCode(404);
    }
}
