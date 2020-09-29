package com.example.demo;

import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Table(name = "links")

public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "original")
    private String originalName;
    @Column(name = "n")
    private String newName;

    public Link(){

    }

}
