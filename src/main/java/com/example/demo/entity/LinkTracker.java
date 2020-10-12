package com.example.demo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "link_tracker")
public class LinkTracker {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long linkTrackerIp;

    @Column(name = "country")
    private String country;
    @Column(name = "city")
    private String city;
    @Column(name = "countLinksByIp")
    long countLinksByIp;

    public LinkTracker(String country, String city, long countLinksByIp){
        this.country=country;
        this.city=city;
        this.countLinksByIp=countLinksByIp;
    }
    public LinkTracker(){

    }
}
