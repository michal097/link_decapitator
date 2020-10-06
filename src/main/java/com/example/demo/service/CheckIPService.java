package com.example.demo.service;

import com.example.demo.entity.Link;
import com.example.demo.repository.LinkRepo;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CheckIPService {

    private final LinkRepo linkRepo;

    @Autowired
    CheckIPService(LinkRepo linkRepo) {
        this.linkRepo = linkRepo;
    }

    public Map<String, Long> linkTracker(String ipAPI) throws IOException {
        List<String> allIps = linkRepo.findAll()
                .stream()
                .map(Link::getIp)
                .distinct()
                .collect(Collectors.toList());

        Map<String, Long> linksMap = new HashMap<>();


        for (String s : allIps) {
            long countApperances = linkRepo.findAll()
                    .stream()
                    .filter(ip -> ip.getIp().equals(s))
                    .count();
            JSONObject jsonObject = new JSONObject(IOUtils.toString(new URL(ipAPI + s), StandardCharsets.UTF_8));

            String key = jsonObject.getString("country") + "," + jsonObject.getString("city");
            boolean isValidId = jsonObject.getString("status").equals("success");

            if (!linksMap.containsKey(key)&&isValidId) {
                linksMap.put(key, countApperances);
            } else if(isValidId) {
                linksMap.computeIfPresent(key, (k, v) -> v + countApperances);
            }
        }
        return linksMap;
    }
}
