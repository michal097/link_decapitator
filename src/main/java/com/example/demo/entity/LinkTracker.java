package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LinkTracker {

    private String country;
    private String city;
    long countLinksByIp;
}
