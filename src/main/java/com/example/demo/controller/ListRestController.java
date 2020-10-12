package com.example.demo.controller;

import com.example.demo.entity.Link;
import com.example.demo.entity.LinkTracker;
import com.example.demo.entity.Stats;
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
    private final CheckIPService checkIPService;
    private final PageableService pageableService;
    private final ReadWriteCSVService readWriteCSVService;


    @Autowired
    public ListRestController(LinkRepo linkRepo,
                              LinkValidatorService linkValidatorService,
                              LinkStatsService linkStatsService,
                              CheckIPService checkIPService,
                              PageableService pageableService,
                              ReadWriteCSVService readWriteCSVService
    ) {

        this.linkRepo = linkRepo;
        this.linkValidatorService = linkValidatorService;
        this.linkStatsService = linkStatsService;
        this.checkIPService = checkIPService;
        this.pageableService = pageableService;
        this.readWriteCSVService = readWriteCSVService;


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
        return new Stats(linkStatsService.countAllLinks(),
                linkStatsService.countAllRedirectedURLs());
    }

  //  REST PAGINATION FOR IP CHECKER IN PROGRESS

    @GetMapping("checkIP")
    public ResponseEntity<List<LinkTracker>> stats(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "5") Integer pageSize
    ) throws IOException {
        final String ipAPI = "http://ip-api.com/json/";
        Map<String, Long> links = checkIPService.linkTracker(ipAPI);

        for (Map.Entry<String, Long> map : links.entrySet()) {
            String[] str = map.getKey().split(",");
           // pageableService.saveToLinkTrackerRepo(new LinkTracker(str[0], str[1], map.getValue()));
        }

        List<LinkTracker> linkTracker = pageableService.linkTrackerList(pageNumber,pageSize);

        return new ResponseEntity<>(linkTracker,new HttpHeaders(),HttpStatus.OK);
    }
  //  @GetMapping("checkIP")
    public List<LinkTracker> stats() throws IOException {
        final String ipAPI = "http://ip-api.com/json/";
        Map<String, Long> links = checkIPService.linkTracker(ipAPI);
        List<LinkTracker> linkTrackers = new ArrayList<>();

        for (Map.Entry<String, Long> map : links.entrySet()) {
            String[] str = map.getKey().split(",");
            linkTrackers.add(new LinkTracker(str[0], str[1], map.getValue()));
        }
        linkTrackers.sort(Comparator.comparing(LinkTracker::getCountry));
        return linkTrackers;
    }

    @GetMapping("/allUrls")
    public ResponseEntity<List<Link>> pagedLinks(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "5") Integer pageSize,
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

