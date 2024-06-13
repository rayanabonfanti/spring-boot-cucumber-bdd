package com.desafio.serasa.experian.integration.steps;

import com.desafio.serasa.experian.domain.dtos.SalvarPessoaRequestDto;
import com.desafio.serasa.experian.domain.pessoa.PessoaRole;
import com.desafio.serasa.experian.integration.steps.utils.FeatureUtils;
import io.cucumber.java.en.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Log4j2
public class PersonRegistrationSteps {

    @Autowired
    private TestRestTemplate restTemplate;
    private ResponseEntity<String> responseEntity;
    private String jwtToken;

    @Given("the user has a valid JWT token")
    public void theUserHasAValidJwtToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String loginPayload = "{\"login\": \"admin\", \"password\": \"admin\"}";
        HttpEntity<String> requestEntity = new HttpEntity<>(loginPayload, headers);
        ResponseEntity<String> authResponse = restTemplate.postForEntity(FeatureUtils.URL + FeatureUtils.pathAuthLogin, requestEntity, String.class);
        assertThat(authResponse.getStatusCode(), equalTo(HttpStatus.OK));
        jwtToken = Objects.requireNonNull(authResponse.getBody()).replaceAll("^\\{\"token\":\"(.+?)\"}$", "$1");
        assertThat(jwtToken, notNullValue());
        assertThat(jwtToken.split("\\.").length, equalTo(3));
    }

    @When("the user sends a valid pessoa data")
    public void theUserSendsAValidPessoaData() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken);

        SalvarPessoaRequestDto pessoaRequest = SalvarPessoaRequestDto.builder()
                .login("newadmin")
                .password("newadmin")
                .role(PessoaRole.ADMIN)
                .cep("01001000")
                .nome("New Admin")
                .idade(25)
                .telefone("1234567890")
                .score(800)
                .build();

        HttpEntity<SalvarPessoaRequestDto> requestEntity = new HttpEntity<>(pessoaRequest, headers);
        responseEntity = restTemplate.postForEntity(FeatureUtils.URL + FeatureUtils.pathUserSave, requestEntity, String.class);
    }

    @Then("the user should receive a success response")
    public void theUserShouldReceiveASuccessResponse() {
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.CREATED));
    }
}
