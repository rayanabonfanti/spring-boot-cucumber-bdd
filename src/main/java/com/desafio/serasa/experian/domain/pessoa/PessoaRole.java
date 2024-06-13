package com.desafio.serasa.experian.domain.pessoa;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PessoaRole {
    ADMIN("admin"),
    USER("user");

    @NotBlank(message = "role is required.")
    private final String role;
}
