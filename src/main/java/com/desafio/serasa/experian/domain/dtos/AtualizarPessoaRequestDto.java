package com.desafio.serasa.experian.domain.dtos;

import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AtualizarPessoaRequestDto {
    private String nome;
    private Integer idade;
    private String telefone;
    @Pattern(regexp = "^\\d{8}$", message = "cep can only have numbers with size 8")
    private String cep;
}
