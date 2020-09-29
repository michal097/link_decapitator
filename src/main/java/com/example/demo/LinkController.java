package com.example.demo;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import java.util.UUID;

@Controller
public class LinkController {

    @Getter
    private LinkRepo linkRepo;
    @Autowired
    LinkController(LinkRepo linkRepo){
        this.linkRepo=linkRepo;
    }

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("link",new Link());
        return "home";

    }
    @PostMapping("/")
    public String saveAndMakeMagic(@ModelAttribute Link link, RedirectAttributes redirectAttributes){

        link.setOriginalName(link.getOriginalName());
        link.setNewName(UUID.randomUUID().toString().replaceAll("-","").substring(0,6));
        linkRepo.save(link);
        redirectAttributes.addFlashAttribute("mess","Short link: localhost:9999/" + link.getNewName());
        return "redirect:/";
    }

    @GetMapping("/{newName}")
    public RedirectView loadPageFromShort(@PathVariable String newName){
        RedirectView redirectView = new RedirectView();
        String url = linkRepo.findByNewName(newName).getOriginalName();
        redirectView.setUrl(url);
        return redirectView;
    }
}
