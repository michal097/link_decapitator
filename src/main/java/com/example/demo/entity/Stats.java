package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Stats {
    private long countAllLinks;
    private long redirectedLinksCounter;

}
