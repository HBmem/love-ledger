package com.hbmem.LoveLedger.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hbmem.LoveLedger.model.Gender;
import com.hbmem.LoveLedger.model.LoveInterest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class LoveInterestJdbcTemplateRepositoryTest {
    @Autowired
    LoveInterestJdbcTemplateRepository repository;
    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindById() {
        LoveInterest loveInterest = repository.findLoveInterestById(1);
        assertNotNull(loveInterest);
        assertEquals(1, loveInterest.getId());
        assertEquals("", loveInterest.getNickname());
        assertEquals("Evan", loveInterest.getFirstName());
        assertEquals("Williams", loveInterest.getLastName());
        assertEquals(Gender.MALE, loveInterest.getGender());
        assertEquals(LocalDate.of(1992, 3, 10), loveInterest.getBirthday());

        List<String> likes = new ArrayList<>();
        likes.add("Fishing");
        likes.add("Cooking");
        likes.add("Running");
        assertEquals(likes, loveInterest.getLikes());

        List<String> dislikes = new ArrayList<>();
        dislikes.add("Late Nights");
        dislikes.add("Cold Weather");
        assertEquals(dislikes, loveInterest.getDislikes());

        assertEquals("", loveInterest.getPhotoUrl());
        assertEquals(2, loveInterest.getUserId());
    }

    @Test
    void shouldFindAllLoveInterestByUserId() {
        List<LoveInterest> loveInterests = repository.findAllLoveInterestByUserId(1);
        assertNotNull(loveInterests);
        assertEquals(1, loveInterests.size());
    }

    @Test
    void shouldAdd() throws JsonProcessingException {
        LoveInterest loveInterest = makeLoveInterest();
        LoveInterest actual = repository.add(loveInterest);
        assertNotNull(actual);
        assertEquals(4, actual.getId());
    }

    @Test
    void shouldUpdate() {
        LoveInterest loveInterest = repository.findLoveInterestById(2);
        assertNotNull(loveInterest);

        loveInterest.setNickname("Butters");

        boolean actual = repository.update(loveInterest);
        assertTrue(actual);
    }

    @Test
    void shouldDelete() {
        boolean actual = repository.delete(3);
        assertTrue(actual);
    }

    private LoveInterest makeLoveInterest() {
        LoveInterest loveInterest = new LoveInterest();

        loveInterest.setId(1);
        loveInterest.setNickname("Sweetheart");
        loveInterest.setFirstName("Emma");
        loveInterest.setLastName("Doe");
        loveInterest.setGender(Gender.FEMALE);
        loveInterest.setBirthday(LocalDate.of(1998, 12, 26));
        loveInterest.setLikes(Arrays.asList("Reading", "Gardening", "Yoga"));
        loveInterest.setDislikes(Arrays.asList("Crowds", "Spicy Food"));
        loveInterest.setPhotoUrl("");
        loveInterest.setUserId(2);

        return loveInterest;
    }
}