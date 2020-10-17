package com.example.demo.controller;

import com.example.demo.entity.Link;
import com.example.demo.entity.LinkTracker;
import com.example.demo.entity.Stats;
import com.example.demo.repository.LinkStatsRepo;
import com.example.demo.service.*;
import com.example.demo.repository.LinkRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;


@RestController
public class ListRestController {

    private final LinkRepo linkRepo;
    private final LinkValidatorService linkValidatorService;
    private final LinkStatsService linkStatsService;
    private final PageableService pageableService;
    private final ReadWriteCSVService readWriteCSVService;
    private final LinkStatsRepo linkStatsRepo;

    @Autowired
    public ListRestController(LinkRepo linkRepo,
                              LinkValidatorService linkValidatorService,
                              LinkStatsService linkStatsService,
                              PageableService pageableService,
                              ReadWriteCSVService readWriteCSVService,
                              LinkStatsRepo linkStatsRepo
    ) {

        this.linkRepo = linkRepo;
        this.linkValidatorService = linkValidatorService;
        this.linkStatsService = linkStatsService;
        this.pageableService = pageableService;
        this.readWriteCSVService = readWriteCSVService;
        this.linkStatsRepo = linkStatsRepo;
    }


    @DeleteMapping("delete/{deleteKey}")
    @Transactional
    public @ResponseBody
    ResponseEntity<Link> deleteLink(@PathVariable("deleteKey") String deleteKey) {

        boolean deleteKeyIsPresent = linkRepo.findAll()
                .stream()
                .filter(ip -> ip.getIp().equals(linkValidatorService.getActualIP()))
                .anyMatch(k -> k.getDeleteKey().equals(deleteKey));

        if (deleteKeyIsPresent) {
            linkRepo.deleteByDeleteKey(deleteKey.trim());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @GetMapping("countAllUrls")
    public Stats countLinks() {

        if (linkStatsRepo.findById(1L).isPresent()) {
            Stats st = linkStatsRepo.findById(1L).get();
            st.setCountAllLinks(linkStatsService.countAllLinks());
            linkStatsRepo.save(st);
        } else {
            Stats stats = new Stats(1L,0L, 0L);
            linkStatsRepo.save(stats);
        }
        return linkStatsRepo.findById(1L).get();
    }


    @GetMapping("/checkIP")
    public ResponseEntity<List<LinkTracker>> stats(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "country") String country
    ) {

        List<LinkTracker> linkTracker = pageableService.linkTrackerList(pageNumber, pageSize, country);

        return new ResponseEntity<>(linkTracker, new HttpHeaders(), HttpStatus.OK);
    }


    @GetMapping("/allUrls")
    public ResponseEntity<List<Link>> pagedLinks(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "99") Integer pageSize,
            @RequestParam(defaultValue = "generationDate") String data
    ) {
        List<Link> links = pageableService.getAllLinksWithPagination(pageNumber, pageSize, data);
        return new ResponseEntity<>(links, new HttpHeaders(), HttpStatus.OK);
    }


    @GetMapping("/downloadText")
    public ResponseEntity<InputStreamResource> str() throws IOException {

        File file = new File(ReadWriteCSVService.PATH);

        readWriteCSVService.makeAndWriteToCSV();

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment;filename=" + file.getName())
                .contentType(MediaType.APPLICATION_PDF).contentLength(file.length())
                .body(resource);
    }
}

