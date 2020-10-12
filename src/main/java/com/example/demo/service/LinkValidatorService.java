package com.example.demo.service;

import com.example.demo.entity.Link;
import com.example.demo.entity.LinkTracker;
import com.example.demo.repository.LinkTrackerRepository;
import org.apache.commons.io.IOUtils;
import org.apache.commons.validator.UrlValidator;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.transaction.Transactional;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class LinkValidatorService {

    private final UrlValidator urlValidator = new UrlValidator();

    private final LinkTrackerRepository linkTrackerRepository;

    @Autowired
    public LinkValidatorService(LinkTrackerRepository linkTrackerRepository) {
        this.linkTrackerRepository = linkTrackerRepository;
    }

    public boolean checkValid(String url) {
        return urlValidator.isValid(url);
    }

    public String getActualIP() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest()
                .getRemoteAddr();
    }

    @Transactional
    public void makeNewLink(Link link) {


        String uniqueString = UUID.randomUUID()
                .toString()
                .replaceAll("-", "");

        String validatorHTTPS = link.getOriginalName()
                .startsWith("http")
                ? link.getOriginalName()
                : "http://" + link.getOriginalName();

        link.setOriginalName(validatorHTTPS.trim());
        link.setNewName(randomlyChangeCase(uniqueString).substring(0, 6));
        link.setDeleteKey(randomlyChangeCase(uniqueString).substring(28));
        link.setIp(getActualIP());

        makeLinkTracker(getActualIP());

    }

    public static String randomlyChangeCase(String str) {

        return str.chars()
                .mapToObj(c -> (char) c)
                .map(c -> c > 97 && c % 2 == 0 ? Character.toUpperCase(c)
                        : Character.toLowerCase(c))
                .map(Object::toString)
                .collect(Collectors.joining()).trim();
    }


    public void makeLinkTracker(String ip) {

        String ipAPI = "http://ip-api.com/json/";
        String country;
        String city;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(IOUtils.toString(new URL(ipAPI + ip), StandardCharsets.UTF_8));
            country = jsonObject.getString("country");
            city = jsonObject.getString("city");
        } catch (Exception e) {
            System.out.println("status: " + jsonObject.getString("status"));
            country = "other";
            city = "other";
        }


        LinkTracker linkTracker = new LinkTracker(country, city, 0);
        if (isPresentInRepo(city)) {
            LinkTracker getOneLink = linkTrackerRepository.findByCity(city);
            getOneLink.setCountLinksByIp(getOneLink.getCountLinksByIp() + 1);
            linkTrackerRepository.save(getOneLink);

        } else {
            linkTrackerRepository.save(linkTracker);

        }
    }

    public boolean isPresentInRepo(String city) {
        return linkTrackerRepository.findAll().stream().anyMatch(l -> l.getCity().equals(city));
    }

}
