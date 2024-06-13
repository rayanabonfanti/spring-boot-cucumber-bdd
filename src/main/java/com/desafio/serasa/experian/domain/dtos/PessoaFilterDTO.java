package com.desafio.serasa.experian.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PessoaFilterDTO {
    private Integer page = 0;
    private Integer size = 10;
    @NotBlank(message = "sortField is required.")
    private String sortField;
    private String sortDirection = "ASC";
    private String nome;
    private Integer idade;
    private String cep;
}
