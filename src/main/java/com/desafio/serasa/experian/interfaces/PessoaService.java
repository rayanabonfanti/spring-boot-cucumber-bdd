package com.desafio.serasa.experian.interfaces;

import com.desafio.serasa.experian.domain.dtos.AtualizarPessoaRequestDto;
import com.desafio.serasa.experian.domain.dtos.PessoaResponseDTO;
import com.desafio.serasa.experian.domain.pessoa.Pessoa;
import com.desafio.serasa.experian.domain.dtos.PessoaFilterDTO;
import com.desafio.serasa.experian.domain.dtos.SalvarPessoaRequestDto;
import com.desafio.serasa.experian.exceptions.CustomException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public interface PessoaService {

    PessoaResponseDTO salvar(@Valid SalvarPessoaRequestDto data) throws CustomException;

    String deletar(Long id) throws CustomException;

    Pessoa update(Long id, @Valid AtualizarPessoaRequestDto data) throws CustomException;

    String getScoreStatus(Long id) throws CustomException;

    Page<Pessoa> getPagedPeople(@Valid PessoaFilterDTO pessoaFilterDTO);
}
