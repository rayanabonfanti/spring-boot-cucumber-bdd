package com.desafio.serasa.experian.integration.steps.utils;

import com.desafio.serasa.experian.domain.autenticadorDTO.LoginRequestDto;

public class FeatureUtils {

    public static String URL = "http://localhost:9000";
    public static String pathAuthLogin = "/auth/login";
    public static String pathUserSave = "/user/salvar";

    public static LoginRequestDto getMockLoginRequestDto() {
        return LoginRequestDto.builder().login("admin").password("admin").build();
    }

}
