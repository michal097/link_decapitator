package com.example.demo;

import com.example.demo.entity.Link;
import com.example.demo.repository.LinkRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;


@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class TestData {

    @Autowired
    LinkRepo linkRepo;

    @Test
    public void findLinkByIdTest(){

        Link link = new Link();
        link.setOriginalName("asd");
        link.setNewName("asd");
        link.setCounter(1);
        link.setDeleteKey("asd");
        link.setGenerationDate(LocalDate.now());

        linkRepo.save(link);

        Link l = linkRepo.findByNewName("asd").orElseThrow(IllegalArgumentException::new);

        assertThat(l.getOriginalName()).isEqualTo("asd");
    }


}
