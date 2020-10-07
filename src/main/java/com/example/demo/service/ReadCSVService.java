package com.example.demo.service;

import org.springframework.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class ReadCSVService {


    private String str(MultipartFile file) {
        return "src/main/resources/" + File.separator + StringUtils.cleanPath(
                Objects.requireNonNull(file.getOriginalFilename()));
    }

    public List<String> multiUrls(MultipartFile file) throws IOException {
        return Files.lines(Paths.get(str(file))).collect(Collectors.toList());
    }

    public void uploadFile(MultipartFile multipartFile) throws Exception{

        Path copyLocation = Paths.get(str(multipartFile));
        Files.copy(multipartFile.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
    }
}
