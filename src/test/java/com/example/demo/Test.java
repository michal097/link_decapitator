package com.example.demo;


public class Test {
    public static void main(String[] args) {



        Thread th = new Thread(() -> {
            System.out.println("start");
            for (int i = 0; i < 5; i++) {
                System.out.println("elko");
            }
            System.out.println("end");
        });
        th.start();
        for(int i=0;i<5;i++){
            System.out.println("inne elko");
        }

    }
}
