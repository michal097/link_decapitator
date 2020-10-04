package com.example.demo;

import org.apache.commons.validator.UrlValidator;

public class LinkValidator {
    static UrlValidator urlValidator = new UrlValidator();

    public static boolean checkValid(String url){
        return urlValidator.isValid(url);
    }
}
