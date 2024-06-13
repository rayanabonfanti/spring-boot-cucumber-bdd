package com.desafio.serasa.experian.domain.autenticadorDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginRequestDto {
    @NotBlank(message = "login is required.")
    private String login;
    @NotBlank(message = "senha is required.")
    private String password;
}
