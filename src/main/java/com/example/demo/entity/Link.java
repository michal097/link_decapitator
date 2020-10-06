package com.example.demo.entity;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "links")
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "original", columnDefinition = "TEXT")
    private String originalName;
    @Column(name = "new_name")
    private String newName;
    @Column(name = "delete_key", unique = true)
    private String deleteKey;
    @Column(name = "counter")
    private Integer counter;
    @Column(name = "ip")
    private String ip;
    @Column(name = "generation_date")
    private LocalDate generationDate;
    public Link() {
        this.counter=0;
        this.generationDate = LocalDate.now();
    }

}
