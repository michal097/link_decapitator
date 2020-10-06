package com.example.demo.controller;

import com.example.demo.entity.Link;
import com.example.demo.entity.LinkTracker;
import com.example.demo.entity.Stats;
import com.example.demo.service.LinkStatsService;
import com.example.demo.service.LinkValidatorService;
import com.example.demo.repository.LinkRepo;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class ListRestController {

    private final LinkRepo linkRepo;
    private final LinkValidatorService linkValidatorService;
    private final LinkStatsService linkStatsService;

    @Autowired
    public ListRestController(LinkRepo linkRepo, LinkValidatorService linkValidatorService, LinkStatsService linkStatsService) {
        this.linkRepo = linkRepo;
        this.linkValidatorService = linkValidatorService;
        this.linkStatsService = linkStatsService;
    }

    @GetMapping("allUrls")
    public List<Link> allUrls() {
        List<Link> sortLinksByDate = linkRepo.findAllByIp(linkValidatorService.getActualIP());
        sortLinksByDate.sort(Comparator.comparing(Link::getGenerationDate).reversed());

        return sortLinksByDate;
    }

    @DeleteMapping("delete/{deleteKey}")
    @Transactional
    public ResponseEntity<Link> deleteLink(@PathVariable("deleteKey") String deleteKey) {

        boolean deleteKeyIsPresent = linkRepo.findAll()
                .stream()
                .filter(ip -> ip.getIp().equals(linkValidatorService.getActualIP()))
                .anyMatch(k -> k.getDeleteKey().equals(deleteKey));

        if (deleteKeyIsPresent) {
            linkRepo.deleteByDeleteKey(deleteKey);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @GetMapping("countAllUrls")
    public Stats countLinks() {
        return new Stats(linkStatsService.countAllLinks(),
                linkStatsService.countAllRedirectedURLs());
    }

    @GetMapping("checkIP")
    public List<LinkTracker> stats() throws IOException {

        List<String> allIps = linkRepo.findAll()
                .stream()
                .map(Link::getIp)
                .distinct()
                .collect(Collectors.toList());
        List<LinkTracker> assignData = new ArrayList<>();

        final String ipAPI = "http://ip-api.com/json/";

        for (String s : allIps) {
            long countApperances = linkRepo.findAll()
                    .stream()
                    .filter(ip -> ip.getIp().equals(s))
                    .count();
            JSONObject jsonObject = new JSONObject(IOUtils.toString(new URL(ipAPI + s), StandardCharsets.UTF_8));

            if (jsonObject.getString("status").equals("fail")) {
                System.out.println("This is not ip");
            } else {
                assignData.add(new LinkTracker(jsonObject.getString("country"),
                        jsonObject.getString("city"),
                        countApperances));
            }
        }
        return assignData;
    }
}
