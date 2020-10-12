package com.example.demo.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stats")
public class Stats {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long statsId;
    private long countAllLinks;
    private long redirectedLinksCounter;

    public Stats(long countAllLinks, long redirectedLinksCounter) {
        this.countAllLinks = countAllLinks;
        this.redirectedLinksCounter = redirectedLinksCounter;
    }
}
