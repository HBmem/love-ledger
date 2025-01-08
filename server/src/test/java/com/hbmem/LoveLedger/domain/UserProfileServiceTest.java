package com.hbmem.LoveLedger.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hbmem.LoveLedger.data.UserCredentialRepository;
import com.hbmem.LoveLedger.data.UserProfileRepository;
import com.hbmem.LoveLedger.model.Gender;
import com.hbmem.LoveLedger.model.UserCredential;
import com.hbmem.LoveLedger.model.UserProfile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserProfileServiceTest {
    @Autowired
    UserProfileService service;

    @MockitoBean
    UserProfileRepository userProfileRepository;
    @MockitoBean
    UserCredentialRepository userCredentialRepository;

    @Test
    void shouldAdd() throws JsonProcessingException {
        UserProfile userProfile = makeUserProfile();
        UserProfile mockOut = makeUserProfile();
        mockOut.setId(4);

        when(userProfileRepository.add(userProfile)).thenReturn(mockOut);
        when(userCredentialRepository.findByUserId(userProfile.getId())).thenReturn(makeUserCredential());

        Result<UserProfile> actual = service.add(userProfile);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(mockOut, actual.getPayload());
    }

    @Test
    void shouldNotAddWhenInvalid() throws JsonProcessingException {
        UserProfile userProfile = makeUserProfile();

        // Should not add when id is not 0
        userProfile.setId(1);
        Result<UserProfile> actual = service.add(userProfile);
        assertEquals(ResultType.INVALID,actual.getType());

        // Should not add when first name is null
        userProfile = makeUserProfile();
        userProfile.setFirstName(null);
        actual = service.add(userProfile);
        assertEquals(ResultType.INVALID,actual.getType());

        // Should not add when first name is blank
        userProfile = makeUserProfile();
        userProfile.setFirstName(null);
        actual = service.add(userProfile);
        assertEquals(ResultType.INVALID,actual.getType());

        // Should not add when gender is null
        userProfile = makeUserProfile();
        userProfile.setGender(null);
        actual = service.add(userProfile);
        assertEquals(ResultType.INVALID,actual.getType());

        // Should not add when birthday is null
        userProfile = makeUserProfile();
        userProfile.setBirthday(null);
        actual = service.add(userProfile);
        assertEquals(ResultType.INVALID,actual.getType());

        // Should not add when birthday is younger than 18
        userProfile = makeUserProfile();
        userProfile.setBirthday(LocalDate.now().minusYears(16));
        actual = service.add(userProfile);
        assertEquals(ResultType.INVALID,actual.getType());

        // Should not add when UserProfileId does not exist
        userProfile = makeUserProfile();
        userProfile.setId(99);

        when(userCredentialRepository.findByUserId(userProfile.getId())).thenReturn(null);
        actual = service.add(userProfile);
        assertEquals(ResultType.INVALID,actual.getType());
    }

    @Test
    void shouldUpdate() {
        UserProfile userProfile = makeUserProfile();
        userProfile.setId(1);

        when(userProfileRepository.update(userProfile)).thenReturn(true);
        when(userCredentialRepository.findByUserId(userProfile.getId())).thenReturn(makeUserCredential());

        Result<UserProfile> actual = service.update(userProfile);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldNotUpdateWhenMissing() {
        UserProfile userProfile = makeUserProfile();
        userProfile.setId(99);

        when(userProfileRepository.update(userProfile)).thenReturn(true);
        when(userCredentialRepository.findByUserId(userProfile.getId())).thenReturn(makeUserCredential());

        Result<UserProfile> actual = service.update(userProfile);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldNotUpdate() {
        UserProfile userProfile = makeUserProfile();

        // Should not update when id is not 0
        userProfile.setId(1);
        Result<UserProfile> actual = service.update(userProfile);
        assertEquals(ResultType.INVALID,actual.getType());

        // Should not update when first name is null
        userProfile = makeUserProfile();
        userProfile.setFirstName(null);
        actual = service.update(userProfile);
        assertEquals(ResultType.INVALID,actual.getType());

        // Should not update when first name is blank
        userProfile = makeUserProfile();
        userProfile.setFirstName(null);
        actual = service.update(userProfile);
        assertEquals(ResultType.INVALID,actual.getType());

        // Should not update when gender is null
        userProfile = makeUserProfile();
        userProfile.setGender(null);
        actual = service.update(userProfile);
        assertEquals(ResultType.INVALID,actual.getType());

        // Should not update when birthday is null
        userProfile = makeUserProfile();
        userProfile.setBirthday(null);
        actual = service.update(userProfile);
        assertEquals(ResultType.INVALID,actual.getType());

        // Should not update when birthday is younger than 18
        userProfile = makeUserProfile();
        userProfile.setBirthday(LocalDate.now().minusYears(16));
        actual = service.update(userProfile);
        assertEquals(ResultType.INVALID,actual.getType());

        // Should not update when UserProfileId does not exist
        userProfile = makeUserProfile();
        userProfile.setId(99);

        when(userCredentialRepository.findByUserId(userProfile.getId())).thenReturn(null);
        actual = service.update(userProfile);
        assertEquals(ResultType.INVALID,actual.getType());
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

        userCredential.setId(4);
        userCredential.setEmail("test@email.com");
        userCredential.setPassword("test@1234");
        userCredential.setPhoneNumber("0987654321");

        List<String> roles = new ArrayList<>();
        roles.add("BASIC");
        userCredential.setRoles(roles);

        return userCredential;
    }
}