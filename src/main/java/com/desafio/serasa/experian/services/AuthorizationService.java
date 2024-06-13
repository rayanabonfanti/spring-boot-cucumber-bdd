package com.desafio.serasa.experian.services;

import com.desafio.serasa.experian.domain.pessoa.Pessoa;
import com.desafio.serasa.experian.exceptions.CustomException;
import com.desafio.serasa.experian.repositories.PessoaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class AuthorizationService implements UserDetailsService {

    private final PessoaRepository repository;

    public AuthorizationService(PessoaRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Pessoa pessoa = repository.findByLogin(username);

            if (Objects.isNull(pessoa)) {
                throw new CustomException(HttpStatus.BAD_REQUEST.value(), "User not found with username: " + username);
            }

            return pessoa;
        } catch (CustomException ex) {
            throw new UsernameNotFoundException(ex.getMessage(), ex);
        }
    }

}
