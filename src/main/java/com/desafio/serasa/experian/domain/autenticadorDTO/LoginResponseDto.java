package com.desafio.serasa.experian.domain.autenticadorDTO;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginResponseDto {
    private String token;
}
