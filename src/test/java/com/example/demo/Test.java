package com.example.demo;

import java.util.UUID;

public class Test {
    public static void main(String [] args){
        String str = UUID.randomUUID().toString();
        System.out.println(str.length());
        System.out.println(str.substring(30));
    }
}
