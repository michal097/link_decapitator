package com.example.demo.service;

import com.example.demo.entity.Link;
import org.apache.commons.validator.UrlValidator;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class LinkValidatorService {

    private final UrlValidator urlValidator = new UrlValidator();

    public boolean checkValid(String url) {
        return urlValidator.isValid(url);
    }

    public String getActualIP(){
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest()
                .getRemoteAddr();
    }

    public void makeNewLink(Link link) {

        String uniqueString = UUID.randomUUID()
                .toString()
                .replaceAll("-", "");

        String validatorHTTPS = link.getOriginalName()
                .startsWith("http")
                ? link.getOriginalName()
                : "http://" + link.getOriginalName();

        link.setOriginalName(validatorHTTPS.trim());
        link.setNewName(randomlyChangeCase(uniqueString).substring(0, 6));
        link.setDeleteKey(randomlyChangeCase(uniqueString).substring(28));

        link.setIp(getActualIP());
    }

    public static String randomlyChangeCase(String str){

        return str.chars()
                .mapToObj(c -> (char) c)
                .map(c->c>97&&c%2==0?Character.toUpperCase(c)
                                    :Character.toLowerCase(c))
                .map(Object::toString)
                .collect(Collectors.joining()).trim();
    }
}
