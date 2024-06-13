package com.desafio.serasa.experian.domain.dtos;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EnderecoResponseApiDto {
    private String cep;
    private String uf;
    private String bairro;
    private String localidade;
    private String logradouro;
}
