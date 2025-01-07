package com.hbmem.LoveLedger.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hbmem.LoveLedger.model.Gender;
import com.hbmem.LoveLedger.model.UserCredential;
import com.hbmem.LoveLedger.model.UserProfile;
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
class UserProfileJdbcTemplateRepositoryTest {
    @Autowired
    UserProfileRepository repository;
    @Autowired
    UserCredentialRepository userCredentialRepository;
    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindById() {
        UserProfile userProfile = repository.findByUserId(1);
        assertNotNull(userProfile);
        assertEquals(1, userProfile.getId());
        assertEquals("Adam", userProfile.getFirstName());
        assertEquals("Smith", userProfile.getLastName());
        assertEquals(Gender.MALE, userProfile.getGender());
        assertEquals(LocalDate.of(1991,1,4), userProfile.getBirthday());

        List<String> likes = new ArrayList<>();
        likes.add("Football");
        likes.add("Traveling");
        likes.add("Video Games");
        assertEquals(likes, userProfile.getLikes());

        List<String> dislikes = new ArrayList<>();
        dislikes.add("Loud Music");
        dislikes.add("Waiting in Line");
        assertEquals(dislikes, userProfile.getDislikes());

        assertEquals("", userProfile.getPhotoUrl());
    }

    @Test
    void shouldAdd() throws JsonProcessingException {
        UserCredential userCredential = makeUserCredential();
        UserCredential actual1 = userCredentialRepository.add(userCredential);

        UserProfile userProfile = makeUserProfile();
        UserProfile actual2 = repository.add(userProfile);
        assertNotNull(actual2);
        assertEquals(4, actual2.getId());
    }

    @Test
    void shouldUpdate() {
        UserProfile userProfile = repository.findByUserId(2);
        assertNotNull(userProfile);

        userProfile.setFirstName("Testing");
        userProfile.setLastName("Tester");
        userProfile.setGender(Gender.NON_BINARY);

        boolean actual = repository.update(userProfile);
        assertTrue(actual);
    }

    @Test
    void shouldDelete() {
        boolean actual = repository.delete(3);
        assertTrue(actual);
    }

    private UserProfile makeUserProfile() {
        UserProfile userProfile = new UserProfile();

        userProfile.setFirstName("Tom");
        userProfile.setLastName("Tester");
        userProfile.setGender(Gender.MALE);
        userProfile.setBirthday(LocalDate.now().minusYears(20));
        userProfile.setLikes(Arrays.asList("Reading", "Gardening", "Yoga"));
        userProfile.setDislikes(Arrays.asList("Crowds", "Spicy Food"));
        userProfile.setPhotoUrl("");

        return userProfile;
    }

    private UserCredential makeUserCredential() {
        UserCredential userCredential = new UserCredential();

        userCredential.setEmail("test@email.com");
        userCredential.setPassword("test@1234");
        userCredential.setPhoneNumber("0987654321");

        List<String> roles = new ArrayList<>();
        roles.add("BASIC");
        userCredential.setRoles(roles);

        return userCredential;
    }
}