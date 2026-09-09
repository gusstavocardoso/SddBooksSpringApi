package com.sdd.booksapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record BookRequest(
    @NotBlank(message = "O título é obrigatório") String titulo,
    @NotBlank(message = "O autor é obrigatório") String autor,
    @NotBlank(message = "O ISBN é obrigatório") String isbn,
    @NotNull(message = "A data de publicação é obrigatória") LocalDate dataPublicacao
) {}
