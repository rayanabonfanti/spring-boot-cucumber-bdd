package com.desafio.serasa.experian.domain.dtos;

import com.desafio.serasa.experian.domain.endereco.Endereco;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PessoaResponseDTO {
    private String nome;
    private Integer idade;
    private String telefone;
    private Integer score;
    private String scoreDescricao;
    private Endereco endereco;
}
