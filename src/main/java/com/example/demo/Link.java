package com.example.demo;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;

@Entity
@Data
@Table(name = "links")

public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "original",
            columnDefinition = "TEXT")
    @URL
    private String originalName;
    @Column(name = "new_name")
    private String newName;
    @Column(name = "delete_key")
    private String deleteKey;
    @Column(name = "counter")
    private Integer counter;
    @Column(name = "ip")
    private String ip;

    public Link() {
        this.counter=0;
    }
}
