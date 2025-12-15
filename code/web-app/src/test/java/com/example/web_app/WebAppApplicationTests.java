package com.example.web_app;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@org.springframework.context.annotation.Import(TestcontainersConfiguration.class)
class WebAppApplicationTests {

    @Test
    void contextLoads() {}
}
