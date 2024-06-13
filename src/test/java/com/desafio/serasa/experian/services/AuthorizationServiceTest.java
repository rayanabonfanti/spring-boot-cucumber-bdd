package com.desafio.serasa.experian.services;

import com.desafio.serasa.experian.domain.pessoa.Pessoa;
import com.desafio.serasa.experian.repositories.PessoaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthorizationServiceTest {

    @InjectMocks
    private AuthorizationService authorizationService;

    @Mock
    private PessoaRepository pessoaRepository;

    @Test
    void testLoadUserByUsername() {
        MockitoAnnotations.openMocks(this);

        Pessoa pessoa = new Pessoa();
        pessoa.setLogin("testUser");
        pessoa.setPassword("testPassword");

        when(pessoaRepository.findByLogin("testUser")).thenReturn(pessoa);
        UserDetails userDetails = authorizationService.loadUserByUsername("testUser");

        assertEquals("testUser", userDetails.getUsername());
        assertEquals("testPassword", userDetails.getPassword());

        verify(pessoaRepository, times(1)).findByLogin("testUser");
    }

    @Test
        void testLoadUserByUsernameUserNotFound() {
        MockitoAnnotations.openMocks(this);

        when(pessoaRepository.findByLogin("nonexistentUser")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            authorizationService.loadUserByUsername("nonexistentUser");
        });

        verify(pessoaRepository, times(1)).findByLogin("nonexistentUser");
    }

}
