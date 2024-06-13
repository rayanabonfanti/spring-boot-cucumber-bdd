package com.desafio.serasa.experian.controllers;

import com.desafio.serasa.experian.domain.autenticadorDTO.LoginRequestDto;
import com.desafio.serasa.experian.domain.autenticadorDTO.LoginResponseDto;
import com.desafio.serasa.experian.domain.pessoa.Pessoa;
import com.desafio.serasa.experian.security.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("auth")
@Tag(name = "Autenticação", description = "API para autenticar pessoas no sistema.")
@Validated
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    public AuthenticationController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @GetMapping(value = "/healthcheck")
    public ResponseEntity<String> healthcheck() {
        return ResponseEntity.ok().body("OK");
    }

    @PostMapping("/login")
    @Operation(summary = "Autentica os dados de pessoa.",
            description = "Endpoint responsável por autenticar os dados de pessoa.")
    @ApiResponse(responseCode = "200", description = "Sucesso.")
    @ApiResponse(responseCode = "401", description = "Não autorizado.")
    @ApiResponse(responseCode = "400", description = "Parâmetro(s) obrigatório(s) foi(foram) informado(s) incorretamente.")
    @ApiResponse(responseCode = "404", description = "Pessoa não autenticada.")
    @ApiResponse(responseCode = "405", description = "Requisição mal formatada.")
    @ApiResponse(responseCode = "500", description = "Erro interno.")
    @ApiResponse(responseCode = "503", description = "Serviço Indisponível.")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto data) {
        Authentication usernamePassword = new UsernamePasswordAuthenticationToken(data.getLogin(), data.getPassword());
        Authentication auth = this.authenticationManager.authenticate(usernamePassword);
        return ResponseEntity.ok().body(new LoginResponseDto(tokenService.generateToken((Pessoa) auth.getPrincipal())));
    }

}
