package com.example.demo;

import com.example.demo.repository.LinkRepo;
import com.example.demo.service.LinkValidatorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
public class ValidateTest {

    @TestConfiguration
    static class LinkValidatorServiceConfig{

        @Bean
        LinkValidatorService linkValidatorService(){
            return new LinkValidatorService();
        }
    }

    @Autowired
    LinkValidatorService linkValidatorService;

    @MockBean
    LinkRepo linkRepo;

    @Test
   public void testValidate(){
        assertTrue(linkValidatorService.checkValid("https://facebook.pl"));
        assertFalse(linkValidatorService.checkValid("facebook.pl"));
    }

}


