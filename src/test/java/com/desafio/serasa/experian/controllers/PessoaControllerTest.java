package com.desafio.serasa.experian.controllers;

import com.desafio.serasa.experian.domain.dtos.AtualizarPessoaRequestDto;
import com.desafio.serasa.experian.domain.dtos.PessoaFilterDTO;
import com.desafio.serasa.experian.domain.dtos.PessoaResponseDTO;
import com.desafio.serasa.experian.domain.dtos.SalvarPessoaRequestDto;
import com.desafio.serasa.experian.domain.endereco.Endereco;
import com.desafio.serasa.experian.domain.pessoa.*;
import com.desafio.serasa.experian.exceptions.CustomException;
import com.desafio.serasa.experian.interfaces.PessoaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.profiles.active=test",
        "my-secret-key=my-secret-key",
        "password-admin=admin"
})
class PessoaControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private PessoaService pessoaService;

    @Test
    @WithUserDetails("admin")
    void shouldReturnBadRequestForMissingLogin() throws Exception {
        SalvarPessoaRequestDto requestDto = SalvarPessoaRequestDto.builder()
                .password("examplePassword")
                .role(PessoaRole.USER)
                .cep("01001-000")
                .nome("John Doe")
                .idade(25)
                .telefone("123456789")
                .score(800)
                .build();

        mockMvc.perform(post("/user/salvar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"errorCode\":400,\"message\":\"login is required.\"}"));
    }

    @Test
    @WithUserDetails("admin")
    void shouldReturnOkForSalvar() throws Exception {
        SalvarPessoaRequestDto requestDto = SalvarPessoaRequestDto.builder()
                .login("exampleUser")
                .password("examplePassword")
                .role(PessoaRole.USER)
                .cep("01001-000")
                .nome("John Doe")
                .idade(25)
                .telefone("123456789")
                .score(800)
                .build();

        PessoaResponseDTO savedPessoa = PessoaResponseDTO.builder()
                .nome("John Doe")
                .idade(25)
                .telefone("123456789")
                .score(800)
                .scoreDescricao("Score Description")
                .endereco(Endereco.builder()
                        .cep("01001-000")
                        .estado("SP")
                        .cidade("São Paulo")
                        .bairro("Sé")
                        .logradouro("Praça da Sé")
                        .build())
                .build();

        Mockito.when(pessoaService.salvar(Mockito.any(SalvarPessoaRequestDto.class))).thenReturn(savedPessoa);

        mockMvc.perform(post("/user/salvar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithUserDetails("admin")
    void shouldReturnInternalServerErrorForSalvar() throws Exception {
        SalvarPessoaRequestDto requestDto = SalvarPessoaRequestDto.builder()
                .login("exampleUser")
                .password("examplePassword")
                .role(PessoaRole.USER)
                .cep("01001-000")
                .nome("John Doe")
                .idade(25)
                .telefone("123456789")
                .score(800)
                .build();

        Mockito.when(pessoaService.salvar(Mockito.any(SalvarPessoaRequestDto.class)))
                .thenThrow(new RuntimeException("Erro interno."));

        mockMvc.perform(post("/user/salvar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isInternalServerError());
    }


    @Test
    void shouldReturnOKForHealthCheck() throws Exception {
        mockMvc.perform(get("/user/healthcheck"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @Test
    void shouldReturnNotFoundForInvalidURL() throws Exception {
        mockMvc.perform(get("/user/invalidurl"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("admin")
    void shouldReturnOkForDelete() throws Exception {
        Mockito.when(pessoaService.deletar(Mockito.anyLong())).thenReturn("Deletado com sucesso");

        mockMvc.perform(delete("/user/deletar")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Deletado com sucesso"));
    }

    @Test
    @WithUserDetails("admin")
    void shouldReturnNotFoundForDeleteNotFound() throws Exception {
        Mockito.when(pessoaService.deletar(Mockito.anyLong())).thenThrow(new CustomException(404, "Pessoa não encontrada."));

        mockMvc.perform(delete("/user/deletar")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("{\"errorCode\":404,\"message\":\"Pessoa nÃ£o encontrada.\"}"));
    }

    @Test
    @WithUserDetails("admin")
    void shouldReturnInternalServerErrorForDeleteError() throws Exception {
        Mockito.when(pessoaService.deletar(Mockito.anyLong())).thenThrow(new RuntimeException("Erro interno."));

        mockMvc.perform(delete("/user/deletar")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithUserDetails("admin")
    void shouldReturnInternalServerErrorForUpdateError() throws Exception {
        Mockito.when(pessoaService.update(Mockito.anyLong(), any(AtualizarPessoaRequestDto.class))).thenThrow(new RuntimeException("Erro interno."));

        mockMvc.perform(patch("/user/atualizar")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"exampleUser\",\"password\":\"examplePassword\",\"role\":\"USER\",\"cep\":\"01001-000\",\"nome\":\"John Doe\",\"idade\":25,\"telefone\":\"123456789\",\"score\":800}"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithUserDetails("admin")
    void shouldReturnOkForGetScoreStatus() throws Exception {
        Mockito.when(pessoaService.getScoreStatus(Mockito.anyLong())).thenReturn("Score Status");

        mockMvc.perform(get("/user/score")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Score Status"));
    }

    @Test
    @WithUserDetails("admin")
    void shouldReturnNotFoundForGetScoreStatusNotFound() throws Exception {
        Mockito.when(pessoaService.getScoreStatus(Mockito.anyLong())).thenThrow(new CustomException(404, "Pessoa não encontrada."));

        mockMvc.perform(get("/user/score")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("{\"errorCode\":404,\"message\":\"Pessoa nÃ£o encontrada.\"}"));
    }

    @Test
    @WithUserDetails("admin")
    void shouldReturnInternalServerErrorForGetScoreStatusError() throws Exception {
        Mockito.when(pessoaService.getScoreStatus(Mockito.anyLong())).thenThrow(new RuntimeException("Erro interno."));

        mockMvc.perform(get("/user/score")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithUserDetails("admin")
    void shouldReturnInternalServerErrorForGetPagedPeopleError() throws Exception {
        Mockito.when(pessoaService.getPagedPeople(Mockito.any(PessoaFilterDTO.class))).thenThrow(new RuntimeException("Erro interno."));

        mockMvc.perform(get("/user/pessoasPaginadas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithUserDetails("admin")
    void shouldReturnOkForUpdate() throws Exception {
        Pessoa updatedPessoa = Pessoa.builder().id(1L).login("exampleUser").build();

        Mockito.when(pessoaService.update(Mockito.anyLong(), any(AtualizarPessoaRequestDto.class))).thenReturn(updatedPessoa);

        AtualizarPessoaRequestDto requestDto = AtualizarPessoaRequestDto.builder()
                .nome("John Doe")
                .idade(25)
                .telefone("123456789")
                .cep("01001000")
                .build();

        mockMvc.perform(patch("/user/atualizar")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"login\":\"exampleUser\"}"));
    }

    @Test
    @WithUserDetails("admin")
    void shouldReturnNotFoundForUpdateNotFound() throws Exception {
        Mockito.when(pessoaService.update(Mockito.anyLong(), any(AtualizarPessoaRequestDto.class)))
                .thenThrow(new CustomException(404, "Pessoa não encontrada."));

        AtualizarPessoaRequestDto requestDto = AtualizarPessoaRequestDto.builder()
                .nome("John Doe")
                .idade(25)
                .telefone("123456789")
                .cep("01001000")
                .build();

        mockMvc.perform(patch("/user/atualizar")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"errorCode\":404,\"message\":\"Pessoa não encontrada.\"}"));
    }

    @Test
    @WithUserDetails("admin")
    void shouldReturnOkForGetPagedPeople() throws Exception {
        Pessoa pessoa = Pessoa.builder().id(1L).login("exampleUser").build();
        Page<Pessoa> page = new PageImpl<>(Collections.singletonList(pessoa));

        Mockito.when(pessoaService.getPagedPeople(Mockito.any(PessoaFilterDTO.class))).thenReturn(page);

        mockMvc.perform(get("/user/pessoasPaginadas")
                        .param("sortField", "nome")
                        .param("nome", "John Doe")
                        .param("idade", "25")
                        .param("cep", "01001-000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
