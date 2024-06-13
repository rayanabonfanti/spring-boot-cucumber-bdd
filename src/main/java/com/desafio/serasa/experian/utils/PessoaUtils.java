package com.desafio.serasa.experian.utils;

import com.desafio.serasa.experian.domain.dtos.PessoaResponseDTO;
import com.desafio.serasa.experian.domain.endereco.Endereco;
import com.desafio.serasa.experian.domain.dtos.EnderecoResponseApiDto;
import com.desafio.serasa.experian.domain.dtos.AtualizarPessoaRequestDto;
import com.desafio.serasa.experian.domain.pessoa.Pessoa;
import com.desafio.serasa.experian.domain.dtos.SalvarPessoaRequestDto;
import org.springframework.web.client.RestTemplate;

public class PessoaUtils {

    private PessoaUtils() {
    }

    public static EnderecoResponseApiDto obterEnderecoPorCEP(String cep) {
        String url = String.format("https://viacep.com.br/ws/%s/json/", cep);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, EnderecoResponseApiDto.class);
    }

    public static String obterDescricaoScore(Integer score) {
        if (score >= 0 && score <= 200) {
            return "Insuficiente";
        } else if (score >= 201 && score <= 500) {
            return "Inaceitável";
        } else if (score >= 501 && score <= 700) {
            return "Aceitável";
        } else if (score >= 701 && score <= 1000) {
            return "Recomendável";
        } else {
            return "Score fora da faixa";
        }
    }

    public static Pessoa criarPessoa(SalvarPessoaRequestDto data, EnderecoResponseApiDto enderecoAPI, String encryptedPassword) {
        Endereco newEndereco = Endereco.builder()
                .cep(enderecoAPI.getCep())
                .estado(enderecoAPI.getUf())
                .cidade(enderecoAPI.getLocalidade())
                .bairro(enderecoAPI.getBairro())
                .logradouro(enderecoAPI.getLogradouro())
                .build();

        return Pessoa.builder()
                .login(data.getLogin())
                .password(encryptedPassword)
                .role(data.getRole())
                .nome(data.getNome())
                .idade(data.getIdade())
                .telefone(data.getTelefone())
                .score(data.getScore())
                .scoreDescricao(obterDescricaoScore(data.getScore()))
                .endereco(newEndereco)
                .deleted(false)
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .build();
    }

    public static void atualizarPessoaComDados(Pessoa pessoa, AtualizarPessoaRequestDto data) {
        pessoa.setNome(data.getNome());
        pessoa.setIdade(data.getIdade());
        pessoa.setTelefone(data.getTelefone());

        EnderecoResponseApiDto enderecoAPI = obterEnderecoPorCEP(data.getCep());
        Endereco newEndereco = Endereco.builder()
                .cep(enderecoAPI.getCep())
                .estado(enderecoAPI.getUf())
                .cidade(enderecoAPI.getLocalidade())
                .bairro(enderecoAPI.getBairro())
                .logradouro(enderecoAPI.getLogradouro())
                .build();

        pessoa.setEndereco(newEndereco);
    }

    public static PessoaResponseDTO criarPessoaResponseDTO(Pessoa pessoa) {
        return new PessoaResponseDTO(
                pessoa.getNome(),
                pessoa.getIdade(),
                pessoa.getTelefone(),
                pessoa.getScore(),
                pessoa.getScoreDescricao(),
                pessoa.getEndereco()
        );
    }

}
