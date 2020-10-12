package com.example.demo.service;

import com.example.demo.entity.Link;
import com.example.demo.repository.LinkRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class ReadWriteCSVService {

    public static String PATH = "src/main/resources/links.csv";
    private final LinkRepo linkRepo;
    private final LinkValidatorService linkValidatorService;

    @Autowired
    ReadWriteCSVService(LinkRepo linkRepo, LinkValidatorService linkValidatorService) {
        this.linkRepo = linkRepo;
        this.linkValidatorService = linkValidatorService;
    }

    private String str(MultipartFile file) {
        return "src/main/resources/" + File.separator + StringUtils.cleanPath(
                Objects.requireNonNull(file.getOriginalFilename()));
    }

    public List<String> multiUrls(MultipartFile file) throws IOException {
        return Files.lines(Paths.get(str(file))).collect(Collectors.toList());
    }

    public void uploadFile(MultipartFile multipartFile) throws Exception {

        Path copyLocation = Paths.get(str(multipartFile));
        Files.copy(multipartFile.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
    }

    public void makeAndWriteToCSV() {
        List<Link> links = linkRepo.findAllByIp(linkValidatorService.getActualIP());
        try (FileWriter fileWriter = new FileWriter(PATH)) {
            fileWriter.append("Original name");
            fileWriter.append(",");
            fileWriter.append("New name");
            fileWriter.append(",");
            fileWriter.append("Redirected counter");
            fileWriter.append('\n');

            for (Link l : links) {
                fileWriter.append(l.getOriginalName());
                fileWriter.append(",");
                fileWriter.append(l.getNewName());
                fileWriter.append(",");
                fileWriter.append(String.valueOf(l.getCounter()));
                fileWriter.append('\n');
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
