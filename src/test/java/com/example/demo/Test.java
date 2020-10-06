package com.example.demo;

import java.util.stream.Collectors;

public class Test {

    public static String randomlyChangeCase(String str){

        return str.chars()
                .mapToObj(c -> (char) c)
                .map(c->c>97&&c%2==0?Character
                        .toUpperCase(c):Character.toLowerCase(c))
                .map(Object::toString)
                .collect(Collectors.joining());
    }

    public static void main(String[] args) {

        System.out.println(randomlyChangeCase("abcp"));
    }
}
