package com.example.demo;

import java.io.File;
import java.io.IOException;

public class Test {

    public static void main(String [] args) throws IOException {
        File f = new File("src/main/resources/text.txt");
        f.createNewFile();
        System.out.println(f.getAbsolutePath());

    }

}


