package com.desafio.serasa.experian.integration.steps;

import com.desafio.serasa.experian.integration.steps.utils.FeatureUtils;
import io.cucumber.java.en.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Log4j2
public class AuthenticationSteps {

    @Autowired
    private TestRestTemplate restTemplate;
    private ResponseEntity<String> responseEntity;
    private String loginPayload;

    @Given("the user provides valid credentials")
    public void theUserProvidesValidCredentials() {
        loginPayload = "{\"login\": \"admin\", \"password\": \"admin\"}";
    }

    @When("the user tries to login")
    public void theUserTriesToLogin() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(loginPayload, headers);
        responseEntity = restTemplate.postForEntity(FeatureUtils.URL + FeatureUtils.pathAuthLogin, requestEntity, String.class);
    }

    @Then("the user should receive a valid JWT token")
    public void theUserShouldReceiveAValidJwtToken() {
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        String jwtToken = responseEntity.getBody();
        assertThat(jwtToken, notNullValue());
        assertThat(jwtToken.split("\\.").length, equalTo(3));
    }
}
