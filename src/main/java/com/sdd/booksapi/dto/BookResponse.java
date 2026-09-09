package com.sdd.booksapi.dto;

import com.sdd.booksapi.domain.Book;
import java.time.LocalDate;

public record BookResponse(
    Long id,
    String titulo,
    String autor,
    String isbn,
    LocalDate dataPublicacao
) {
    public static BookResponse fromEntity(Book book) {
        return new BookResponse(
            book.getId(),
            book.getTitulo(),
            book.getAutor(),
            book.getIsbn(),
            book.getDataPublicacao()
        );
    }
}
