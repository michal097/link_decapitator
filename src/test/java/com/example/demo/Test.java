package com.example.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Test {
    public static void main(String[] args) throws IOException {
        List<String> str = Files.lines(Paths.get("D:\\q.csv")).collect(Collectors.toList());
        System.out.println(str);
    }
}
