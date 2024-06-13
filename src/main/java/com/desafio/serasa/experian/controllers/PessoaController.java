package com.desafio.serasa.experian.controllers;

import com.desafio.serasa.experian.domain.dtos.AtualizarPessoaRequestDto;
import com.desafio.serasa.experian.domain.dtos.PessoaResponseDTO;
import com.desafio.serasa.experian.domain.pessoa.Pessoa;
import com.desafio.serasa.experian.domain.dtos.PessoaFilterDTO;
import com.desafio.serasa.experian.domain.dtos.SalvarPessoaRequestDto;
import com.desafio.serasa.experian.exceptions.CustomException;
import com.desafio.serasa.experian.interfaces.PessoaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("user")
@Tag(name = "Pessoas", description = "API para manipular dados de pessoas do sistema.")
@Validated
public class PessoaController {

    private final PessoaService pessoaService;

    public PessoaController(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @GetMapping(value = "/healthcheck")
    public ResponseEntity<String> healthcheck() {
        return ResponseEntity.ok().body("OK");
    }

    @PostMapping("/salvar")
    @Operation(summary = "Salva os dados de pessoa.",
            description = "Endpoint responsável por salvar os dados de pessoa.")
    @ApiResponse(responseCode = "200", description = "Sucesso.")
    @ApiResponse(responseCode = "401", description = "Não autorizado.")
    @ApiResponse(responseCode = "400", description = "Parâmetro(s) obrigatório(s) foi(foram) informado(s) incorretamente.")
    @ApiResponse(responseCode = "404", description = "Pessoa não encontrada.")
    @ApiResponse(responseCode = "405", description = "Requisição mal formatada.")
    @ApiResponse(responseCode = "500", description = "Erro interno.")
    @ApiResponse(responseCode = "503", description = "Serviço Indisponível.")
    public ResponseEntity<PessoaResponseDTO> salvar(@Valid @RequestBody SalvarPessoaRequestDto data, BindingResult bindingResult) throws CustomException {
        return ResponseEntity.status(201).body(pessoaService.salvar(data));
    }

    @DeleteMapping("/deletar")
    @Operation(summary = "Deletar os dados logicamente de pessoa.",
            description = "Endpoint responsável por deletar os dados logicamente de pessoa.")
    @ApiResponse(responseCode = "200", description = "Sucesso.")
    @ApiResponse(responseCode = "401", description = "Não autorizado.")
    @ApiResponse(responseCode = "400", description = "Parâmetro(s) obrigatório(s) foi(foram) informado(s) incorretamente.")
    @ApiResponse(responseCode = "404", description = "Pessoa não encontrada.")
    @ApiResponse(responseCode = "405", description = "Requisição mal formatada.")
    @ApiResponse(responseCode = "500", description = "Erro interno.")
    @ApiResponse(responseCode = "503", description = "Serviço Indisponível.")
    public ResponseEntity<String> delete(@RequestParam Long id) throws CustomException {
        return ResponseEntity.ok().body(pessoaService.deletar(id));
    }

    @PatchMapping("/atualizar")
    @Operation(summary = "Atualizar os dados de pessoa.",
            description = "Endpoint responsável por atualizar os dados de pessoa.")
    @ApiResponse(responseCode = "200", description = "Sucesso.")
    @ApiResponse(responseCode = "401", description = "Não autorizado.")
    @ApiResponse(responseCode = "400", description = "Parâmetro(s) obrigatório(s) foi(foram) informado(s) incorretamente.")
    @ApiResponse(responseCode = "404", description = "Pessoa não encontrada.")
    @ApiResponse(responseCode = "405", description = "Requisição mal formatada.")
    @ApiResponse(responseCode = "500", description = "Erro interno.")
    @ApiResponse(responseCode = "503", description = "Serviço Indisponível.")
    public ResponseEntity<Pessoa> update(@RequestParam Long id, @Valid @RequestBody AtualizarPessoaRequestDto data) throws CustomException {
        return ResponseEntity.ok().body(pessoaService.update(id, data));
    }

    @GetMapping("/score")
    @Operation(summary = "Recuperar informação de score de uma pessoa.",
            description = "Endpoint responsável por recuperar informação de score de uma pessoa.")
    @ApiResponse(responseCode = "200", description = "Sucesso.")
    @ApiResponse(responseCode = "401", description = "Não autorizado.")
    @ApiResponse(responseCode = "400", description = "Parâmetro(s) obrigatório(s) foi(foram) informado(s) incorretamente.")
    @ApiResponse(responseCode = "404", description = "Pessoa não encontrada.")
    @ApiResponse(responseCode = "405", description = "Requisição mal formatada.")
    @ApiResponse(responseCode = "500", description = "Erro interno.")
    @ApiResponse(responseCode = "503", description = "Serviço Indisponível.")
    public ResponseEntity<String> getScoreStatus(@RequestParam Long id) throws CustomException {
        return ResponseEntity.ok().body(pessoaService.getScoreStatus(id));
    }

    @GetMapping("/pessoasPaginadas")
    @Operation(summary = "Recuperar dados de pessoas paginadas filtrando e ordenando por nome, idade ou cep.",
            description = "Endpoint responsável por recuperar dados de pessoas paginadas filtrando e ordenando por nome, idade ou cep.")
    @ApiResponse(responseCode = "200", description = "Sucesso.")
    @ApiResponse(responseCode = "401", description = "Não autorizado.")
    @ApiResponse(responseCode = "400", description = "Parâmetro(s) obrigatório(s) foi(foram) informado(s) incorretamente.")
    @ApiResponse(responseCode = "404", description = "Pessoa não encontrada.")
    @ApiResponse(responseCode = "405", description = "Requisição mal formatada.")
    @ApiResponse(responseCode = "500", description = "Erro interno.")
    @ApiResponse(responseCode = "503", description = "Serviço Indisponível.")
    public ResponseEntity<Page<Pessoa>> getPagedPeople(@Valid PessoaFilterDTO pessoaFilterDTO) {
        return ResponseEntity.ok().body(pessoaService.getPagedPeople(pessoaFilterDTO));
    }

}
