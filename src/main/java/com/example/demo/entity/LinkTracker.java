package com.example.demo.entity;


import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "link_tracker_table")
public class LinkTracker {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "linkTrackerId")
    private Long linkTrackerId;

    @Column(name = "country")
    private String country;
    @Column(name = "city")
    private String city;
    @Column(name = "countLinksByIp")
    int countLinksByIp;

    public LinkTracker(String country, String city, long countLinksByIp) {
        this.country = country;
        this.city = city;
        this.countLinksByIp = 1;
    }

    public LinkTracker() {
    }
}
