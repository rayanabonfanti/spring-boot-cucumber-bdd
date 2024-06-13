package com.desafio.serasa.experian;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = SystemApplication.class)
class SystemApplicationTests {

    @Test
    void contextLoads() {
        SystemApplication.main(new String[]{});
        Assertions.assertTrue(true);
    }

}
