package com.example.demo.controller;

import com.example.demo.entity.Link;
import com.example.demo.entity.Stats;
import com.example.demo.repository.LinkStatsRepo;
import com.example.demo.service.LinkValidatorService;
import com.example.demo.repository.LinkRepo;
import com.example.demo.service.ReadWriteCSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.naming.NamingException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class LinkController {

    private final LinkRepo linkRepo;
    private final LinkValidatorService linkValidatorService;
    private final ReadWriteCSVService readWriteCSVService;
    private final LinkStatsRepo linkStatsRepo;

    @Autowired
    LinkController(LinkRepo linkRepo, LinkValidatorService linkValidatorService, ReadWriteCSVService readWriteCSVService, LinkStatsRepo linkStatsRepo) {
        this.linkRepo = linkRepo;
        this.linkValidatorService = linkValidatorService;
        this.readWriteCSVService = readWriteCSVService;
        this.linkStatsRepo = linkStatsRepo;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("link", new Link());
        return "home";

    }

    @PostMapping("/save")
    public String saveAndMakeMagic(@ModelAttribute Link link, RedirectAttributes redirectAttributes) {
        //VALIDATOR
        if (!link.getOriginalName().startsWith("http")) {
            link.setOriginalName("https://" + link.getOriginalName());
        }

        if (link.getOriginalName().trim().equals("https://")) {
            redirectAttributes.addFlashAttribute("err", "ENTER SOME URL!!!");
            return "redirect:/";
        } else if (!linkValidatorService.checkValid(link.getOriginalName())) {
            redirectAttributes.addFlashAttribute("err", "Entered URL is invalid");
            return "redirect:/";
        } else {
            linkValidatorService.makeNewLink(link);
            linkRepo.save(link);
            redirectAttributes.addFlashAttribute("mess", "s91.herokuapp.com/" + link.getNewName());
        }
        return "redirect:/";
    }

    @GetMapping("/{newName}")
    @Transactional
    public RedirectView loadPageFromShort(@PathVariable String newName) {

        RedirectView redirectView = new RedirectView();
        Optional<Link> optLink = linkRepo.findByNewName(newName);
        String url;
        if (optLink.isPresent()) {

            url = optLink.get().getOriginalName();
            linkRepo.increase(optLink.get().getId());
            Stats s = linkStatsRepo.findById(1L).get();
            s.setRedirectedLinksCounter(s.getRedirectedLinksCounter() + 1);
            linkStatsRepo.save(s);
        } else {
            url = "";
        }

        redirectView.setUrl(url);
        return redirectView;
    }

    @PostMapping("/uploadFile")
    public String saveMultiLinks(@RequestParam("file") MultipartFile file,
                                 RedirectAttributes redirectAttributes) {

        boolean isCSV = Objects.requireNonNull(file.getOriginalFilename()).endsWith("csv");
        boolean isTXT = file.getOriginalFilename().endsWith("txt");

        System.out.println("csv: " + isCSV + " txt: " + isTXT);
        try {
            if (!isCSV && !isTXT)
                throw new Exception();
            readWriteCSVService.uploadFile(file);

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("uplErr", "Invalid file structure");
            return "redirect:/";

        }

        try {
            List<String> urls = readWriteCSVService.multiUrls(file);

            for (String s : urls) {

                String str = s.startsWith("http://") ? s.trim() : "http://" + s.trim();

                if (linkValidatorService.checkValid(str)) {
                    Link link = new Link();
                    link.setOriginalName(str);
                    linkValidatorService.makeNewLink(link);
                    linkRepo.save(link);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("uplErr", "Bad URL's structure");
            return "redirect:/";
        }

        redirectAttributes.addFlashAttribute("uplSucc", "URL's has been decapitated successfully!");
        return "redirect:/";
    }

}
