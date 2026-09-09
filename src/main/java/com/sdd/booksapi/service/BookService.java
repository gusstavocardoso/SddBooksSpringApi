package com.sdd.booksapi.service;

import com.sdd.booksapi.domain.Book;
import com.sdd.booksapi.dto.BookRequest;
import com.sdd.booksapi.dto.BookResponse;
import com.sdd.booksapi.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(BookResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado com ID: " + id));
        return BookResponse.fromEntity(book);
    }

    @Transactional
    public BookResponse createBook(BookRequest request) {
        if (bookRepository.existsByIsbn(request.isbn())) {
            throw new IllegalArgumentException("Já existe um livro com o ISBN: " + request.isbn());
        }
        
        Book book = new Book();
        book.setTitulo(request.titulo());
        book.setAutor(request.autor());
        book.setIsbn(request.isbn());
        book.setDataPublicacao(request.dataPublicacao());
        
        book = bookRepository.save(book);
        return BookResponse.fromEntity(book);
    }

    @Transactional
    public BookResponse updateBook(Long id, BookRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado com ID: " + id));
                
        if (!book.getIsbn().equals(request.isbn()) && bookRepository.existsByIsbn(request.isbn())) {
            throw new IllegalArgumentException("Já existe um livro com o ISBN: " + request.isbn());
        }

        book.setTitulo(request.titulo());
        book.setAutor(request.autor());
        book.setIsbn(request.isbn());
        book.setDataPublicacao(request.dataPublicacao());
        
        book = bookRepository.save(book);
        return BookResponse.fromEntity(book);
    }

    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new IllegalArgumentException("Livro não encontrado com ID: " + id);
        }
        bookRepository.deleteById(id);
    }
}
