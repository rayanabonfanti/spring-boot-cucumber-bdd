package com.desafio.serasa.experian.services;

import com.desafio.serasa.experian.domain.dtos.AtualizarPessoaRequestDto;
import com.desafio.serasa.experian.domain.dtos.PessoaFilterDTO;
import com.desafio.serasa.experian.domain.dtos.PessoaResponseDTO;
import com.desafio.serasa.experian.domain.dtos.SalvarPessoaRequestDto;
import com.desafio.serasa.experian.domain.endereco.Endereco;
import com.desafio.serasa.experian.domain.pessoa.*;
import com.desafio.serasa.experian.exceptions.CustomException;
import com.desafio.serasa.experian.repositories.PessoaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PessoaServiceImplTest {

    @Mock
    private PessoaRepository pessoaRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private PessoaServiceImpl pessoaService;

    private static Endereco buildFictionalEndereco() {
        return Endereco.builder()
                .id(1L)
                .cep("13035070")
                .estado("SP")
                .cidade("Campinas")
                .bairro("Centro")
                .logradouro("Rua Principal")
                .build();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDeletar() throws CustomException {
        when(pessoaRepository.findById(anyLong())).thenReturn(Optional.of(criarPessoa()));
        when(pessoaRepository.save(any(Pessoa.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String result = pessoaService.deletar(1L);

        assertEquals("Pessoa excluída logicamente com sucesso", result);
        verify(pessoaRepository, times(1)).findById(1L);
        verify(pessoaRepository, times(1)).save(any(Pessoa.class));
    }

    @Test
    void testDeletarWhenPessoaNotFound() {
        when(pessoaRepository.findById(anyLong())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            pessoaService.deletar(1L);
        });

        assert HttpStatus.BAD_REQUEST.value() == exception.getErrorCode();
        assert "Objeto não encontrado.".equals(exception.getMessage());
    }

    @Test
    void testGetScoreStatusPessoaPresent() throws CustomException {
        Long id = 1L;
        Pessoa pessoa = criarPessoa();

        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoa));
        String result = pessoaService.getScoreStatus(id);

        assertEquals("Aceitável", result);
    }

    @Test
    void testUpdatePessoaPresent() throws CustomException {
        Long id = 1L;
        AtualizarPessoaRequestDto data = criarAtualizarPessoaRequestDto();
        Pessoa pessoa = criarPessoa();

        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoa));
        when(pessoaRepository.save(any(Pessoa.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pessoa result = pessoaService.update(id, data);

        assertNotNull(result);
        assertEquals(data.getNome(), result.getNome());
        assertEquals(data.getIdade(), result.getIdade());
        assertEquals(data.getTelefone(), result.getTelefone());
    }

    @Test
    void testGetScoreStatusPessoaNotPresent() {
        Long id = 1L;

        when(pessoaRepository.findById(id)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            pessoaService.getScoreStatus(id);
        });

        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getErrorCode());
        assertEquals("Objeto não encontrado.", exception.getMessage());
    }

    @Test
    void testUpdatePessoaNotPresent() {
        Long id = 1L;
        AtualizarPessoaRequestDto data = criarAtualizarPessoaRequestDto();

        when(pessoaRepository.findById(id)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            pessoaService.update(id, data);
        });

        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getErrorCode());
        assertEquals("Objeto não encontrado.", exception.getMessage());
    }

    @Test
    void testSalvarSuccessful() throws CustomException {
        SalvarPessoaRequestDto requestDto = criarSalvarPessoaRequestDto();

        when(pessoaRepository.findByLogin(requestDto.getLogin())).thenReturn(null);
        when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("hashedPassword");

        PessoaResponseDTO result = pessoaService.salvar(requestDto);

        assertNotNull(result);
        assertEquals(requestDto.getNome(), result.getNome());
    }

    @Test
    void testSalvarExistingLogin() {
        SalvarPessoaRequestDto requestDto = criarSalvarPessoaRequestDto();

        when(pessoaRepository.findByLogin(requestDto.getLogin())).thenReturn(new Pessoa());

        CustomException exception = assertThrows(CustomException.class, () -> {
            pessoaService.salvar(requestDto);
        });

        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getErrorCode());
        assertEquals("Objeto já existente.", exception.getMessage());
    }

    @Test
    void testGetPagedPeople() {
        PessoaFilterDTO pessoaFilterDTO = new PessoaFilterDTO();
        pessoaFilterDTO.setPage(0);
        pessoaFilterDTO.setSize(10);
        pessoaFilterDTO.setSortDirection("ASC");
        pessoaFilterDTO.setSortField("nome");
        pessoaFilterDTO.setNome("John");
        pessoaFilterDTO.setIdade(25);
        pessoaFilterDTO.setCep("12345-678");

        List<Pessoa> expectedPeople = Collections.singletonList(
                criarPessoa()
        );
        Page<Pessoa> expectedPage = new PageImpl<>(expectedPeople);

        when(pessoaRepository.findAll(any(Example.class), any(Pageable.class))).thenReturn(expectedPage);
        Page<Pessoa> result = pessoaService.getPagedPeople(pessoaFilterDTO);

        assertEquals(expectedPage, result);
    }

    private SalvarPessoaRequestDto criarSalvarPessoaRequestDto() {
        return new SalvarPessoaRequestDto("login", "password", PessoaRole.USER, "13035-070", "nome", 20, "11999780675", 345);
    }

    private AtualizarPessoaRequestDto criarAtualizarPessoaRequestDto() {
        return new AtualizarPessoaRequestDto("Novo Nome", 30, "11998887777", "54321-876");
    }

    private Pessoa criarPessoa() {
        return Pessoa.builder()
                .id(1L)
                .login("john.doe")
                .password("password123")
                .role(PessoaRole.USER)
                .nome("John Doe")
                .idade(30)
                .telefone("123456789")
                .score(600)
                .scoreDescricao("Aceitável")
                .endereco(buildFictionalEndereco())
                .deleted(false)
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .build();
    }

}
