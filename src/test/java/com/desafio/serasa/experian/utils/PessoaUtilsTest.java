package com.desafio.serasa.experian.utils;

import com.desafio.serasa.experian.domain.dtos.EnderecoResponseApiDto;
import com.desafio.serasa.experian.domain.dtos.AtualizarPessoaRequestDto;
import com.desafio.serasa.experian.domain.pessoa.Pessoa;
import com.desafio.serasa.experian.domain.dtos.SalvarPessoaRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.profiles.active=test",
        "my-secret-key=my-secret-key",
        "password-admin=admin"
})
class PessoaUtilsTest {

    @MockBean
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObterEnderecoPorCEP() {
        String cep = "13035070";
        EnderecoResponseApiDto endereco = new EnderecoResponseApiDto("13035-070", "SP", "Vila Industrial", "Campinas", "Rua Caçapava");

        when(restTemplate.getForObject(anyString(), eq(EnderecoResponseApiDto.class))).thenReturn(endereco);
        EnderecoResponseApiDto result = PessoaUtils.obterEnderecoPorCEP(cep);

        assertEquals(endereco, result);
    }

    @Test
    void testObterDescricaoScore() {
        assertEquals("Insuficiente", PessoaUtils.obterDescricaoScore(150));
        assertEquals("Inaceitável", PessoaUtils.obterDescricaoScore(350));
        assertEquals("Aceitável", PessoaUtils.obterDescricaoScore(600));
        assertEquals("Recomendável", PessoaUtils.obterDescricaoScore(800));
        assertEquals("Score fora da faixa", PessoaUtils.obterDescricaoScore(1200));
    }

    @Test
    void testObterDescricaoScore_Insuficiente() {
        assertEquals("Insuficiente", PessoaUtils.obterDescricaoScore(0));
        assertEquals("Insuficiente", PessoaUtils.obterDescricaoScore(50));
        assertEquals("Insuficiente", PessoaUtils.obterDescricaoScore(200));
    }

    @Test
    void testObterDescricaoScore_Inaceitavel() {
        assertEquals("Inaceitável", PessoaUtils.obterDescricaoScore(201));
        assertEquals("Inaceitável", PessoaUtils.obterDescricaoScore(350));
        assertEquals("Inaceitável", PessoaUtils.obterDescricaoScore(500));
    }

    @Test
    void testObterDescricaoScore_Aceitavel() {
        assertEquals("Aceitável", PessoaUtils.obterDescricaoScore(501));
        assertEquals("Aceitável", PessoaUtils.obterDescricaoScore(600));
        assertEquals("Aceitável", PessoaUtils.obterDescricaoScore(700));
    }

    @Test
    void testObterDescricaoScore_Recomendavel() {
        assertEquals("Recomendável", PessoaUtils.obterDescricaoScore(701));
        assertEquals("Recomendável", PessoaUtils.obterDescricaoScore(800));
        assertEquals("Recomendável", PessoaUtils.obterDescricaoScore(1000));
    }

    @Test
    void testObterDescricaoScore_ForaDaFaixa() {
        assertEquals("Score fora da faixa", PessoaUtils.obterDescricaoScore(-1));
        assertEquals("Score fora da faixa", PessoaUtils.obterDescricaoScore(1200));
        assertEquals("Score fora da faixa", PessoaUtils.obterDescricaoScore(1500));
    }

    @Test
    void testCriarPessoa() {
        SalvarPessoaRequestDto data = new SalvarPessoaRequestDto();
        data.setLogin("teste");
        data.setCep("13035070");
        data.setScore(100);
        EnderecoResponseApiDto enderecoAPI = new EnderecoResponseApiDto();
        String encryptedPassword = "encryptedPassword";

        Pessoa result = PessoaUtils.criarPessoa(data, enderecoAPI, encryptedPassword);

        assertEquals(data.getLogin(), result.getLogin());
        assertEquals(data.getRole(), result.getRole());
        assertEquals(enderecoAPI.getCep(), result.getEndereco().getCep());
    }

    @Test
    void testAtualizarPessoaComDados() {
        Pessoa pessoa = new Pessoa();
        AtualizarPessoaRequestDto data = new AtualizarPessoaRequestDto();
        data.setCep("13035-070");
        EnderecoResponseApiDto enderecoAPI = new EnderecoResponseApiDto();
        enderecoAPI.setCep("13035-070");
        when(restTemplate.getForObject(anyString(), eq(EnderecoResponseApiDto.class))).thenReturn(enderecoAPI);

        PessoaUtils.atualizarPessoaComDados(pessoa, data);

        assertEquals(data.getNome(), pessoa.getNome());
        assertEquals(data.getIdade(), pessoa.getIdade());
        assertEquals(data.getTelefone(), pessoa.getTelefone());
        assertEquals(enderecoAPI.getCep(), pessoa.getEndereco().getCep());
    }

}
