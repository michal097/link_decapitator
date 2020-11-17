package com.example.demo.service;

import com.example.demo.entity.Link;
import com.example.demo.repository.LinkRepo;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Service
public class ReadWriteCSVService {

    public static String PATH = "src/main/resources/links.xlsx";
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


    public void writeDataToExcel() {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Links report");

        List<Link> links = linkRepo.findAllByIp(linkValidatorService.getActualIP());

        int counter = 2;

        Map<String, Object[]> data = new TreeMap<>();
        data.put("1", new Object[]{"Original name", "New name", "Redirected counter"});

        for (Link link : links) {
            data.put(String.valueOf(counter),
                    new Object[]{link.getOriginalName(), "s91.herokuapp.com/" + link.getNewName(), link.getCounter()});
            counter++;
        }

        Set<String> keySet = data.keySet();
        int rownum = 0;

        for (String key : keySet) {
            Row row = sheet.createRow(rownum++);
            Object[] objArray = data.get(key);

            int cellnum = 0;

            for (Object object : objArray) {

                Cell cell = row.createCell(cellnum++);
                if (object instanceof String)
                    cell.setCellValue((String) object);
                else if (object instanceof Integer)
                    cell.setCellValue((Integer) object);
            }
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(new File(PATH))) {
            workbook.write(fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
