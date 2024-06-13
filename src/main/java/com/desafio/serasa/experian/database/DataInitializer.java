package com.desafio.serasa.experian.database;

import com.desafio.serasa.experian.domain.endereco.Endereco;
import com.desafio.serasa.experian.domain.pessoa.Pessoa;
import com.desafio.serasa.experian.domain.pessoa.PessoaRole;
import com.desafio.serasa.experian.repositories.PessoaRepository;
import com.desafio.serasa.experian.utils.PessoaUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final PessoaRepository pessoaRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Value("${password.admin}")
    private String senhaAdmin;

    public DataInitializer(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Endereco enderecoAdmin = Endereco.builder()
                .cep("01001000")
                .estado("SP")
                .cidade("São Paulo")
                .bairro("Centro")
                .logradouro("Rua Principal")
                .build();

        Pessoa pessoaRoot = Pessoa.builder()
                .login("admin")
                .password(passwordEncoder.encode(senhaAdmin))
                .role(PessoaRole.ADMIN)
                .nome("admin")
                .idade(20)
                .telefone("11999780675")
                .score(520)
                .scoreDescricao(PessoaUtils.obterDescricaoScore(520))
                .endereco(enderecoAdmin)
                .deleted(false)
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .build();

        pessoaRepository.save(pessoaRoot);

        log.info("***ADMIN**** " + pessoaRoot);
    }
}
