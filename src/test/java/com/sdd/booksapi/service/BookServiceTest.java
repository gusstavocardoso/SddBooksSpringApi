package com.sdd.booksapi.service;

import com.sdd.booksapi.domain.Book;
import com.sdd.booksapi.dto.BookRequest;
import com.sdd.booksapi.dto.BookResponse;
import com.sdd.booksapi.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book book;
    private BookRequest bookRequest;

    @BeforeEach
    void setUp() {
        book = new Book(1L, "O Senhor dos Anéis", "J.R.R. Tolkien", "1234567890", LocalDate.of(1954, 7, 29));
        bookRequest = new BookRequest("O Senhor dos Anéis", "J.R.R. Tolkien", "1234567890", LocalDate.of(1954, 7, 29));
    }

    @Test
    void createBook_Success() {
        when(bookRepository.existsByIsbn(anyString())).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookResponse response = bookService.createBook(bookRequest);

        assertNotNull(response);
        assertEquals(book.getTitulo(), response.titulo());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void createBook_IsbnAlreadyExists_ThrowsException() {
        when(bookRepository.existsByIsbn(anyString())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> bookService.createBook(bookRequest));
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void getBookById_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookResponse response = bookService.getBookById(1L);

        assertNotNull(response);
        assertEquals(book.getId(), response.id());
    }

    @Test
    void getBookById_NotFound_ThrowsException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> bookService.getBookById(1L));
    }

    @Test
    void getAllBooks_Success() {
        when(bookRepository.findAll()).thenReturn(java.util.List.of(book));
        
        var response = bookService.getAllBooks();
        
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(book.getTitulo(), response.get(0).titulo());
    }

    @Test
    void updateBook_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookResponse response = bookService.updateBook(1L, bookRequest);

        assertNotNull(response);
        assertEquals(book.getTitulo(), response.titulo());
    }

    @Test
    void updateBook_NotFound_ThrowsException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> bookService.updateBook(1L, bookRequest));
    }

    @Test
    void updateBook_IsbnAlreadyExists_ThrowsException() {
        Book existingBook = new Book(1L, "O Senhor dos Anéis", "J.R.R. Tolkien", "99999999", LocalDate.of(1954, 7, 29));
        
        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(bookRepository.existsByIsbn(bookRequest.isbn())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> bookService.updateBook(1L, bookRequest));
    }

    @Test
    void deleteBook_Success() {
        when(bookRepository.existsById(1L)).thenReturn(true);
        bookService.deleteBook(1L);
        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteBook_NotFound_ThrowsException() {
        when(bookRepository.existsById(1L)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> bookService.deleteBook(1L));
        verify(bookRepository, never()).deleteById(anyLong());
    }
}
