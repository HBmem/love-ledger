package com.hbmem.LoveLedger.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hbmem.LoveLedger.data.LoveInterestRepository;
import com.hbmem.LoveLedger.data.UserCredentialRepository;
import com.hbmem.LoveLedger.model.Gender;
import com.hbmem.LoveLedger.model.LoveInterest;
import com.hbmem.LoveLedger.model.UserCredential;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class LoveInterestServiceTest {
    @Autowired
    LoveInterestService service;

    @MockitoBean
    LoveInterestRepository loveInterestRepository;
    @MockitoBean
    UserCredentialRepository userCredentialRepository;

    @Test
    void shouldAddNickname() throws JsonProcessingException {
        LoveInterest loveInterest = makeLoveInterest();
        loveInterest.setFirstName(null);
        loveInterest.setLastName(null);

        LoveInterest mockOut = makeLoveInterest();
        mockOut.setFirstName(null);
        mockOut.setLastName(null);
        mockOut.setId(4);

        when(loveInterestRepository.add(loveInterest)).thenReturn(mockOut);
        when(userCredentialRepository.findByUserId(loveInterest.getUserId())).thenReturn(makeUser());

        Result<LoveInterest> actual = service.add(loveInterest);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(mockOut, actual.getPayload());
    }

    @Test
    void shouldAddFirstName() throws JsonProcessingException {
        LoveInterest loveInterest = makeLoveInterest();
        loveInterest.setNickname(null);

        LoveInterest mockOut = makeLoveInterest();
        mockOut.setNickname(null);
        mockOut.setId(4);

        when(loveInterestRepository.add(loveInterest)).thenReturn(mockOut);
        when(userCredentialRepository.findByUserId(loveInterest.getUserId())).thenReturn(makeUser());

        Result<LoveInterest> actual = service.add(loveInterest);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(mockOut, actual.getPayload());
    }

    @Test
    void shouldNotAddWhenInvalid() throws JsonProcessingException {
        LoveInterest loveInterest = makeLoveInterest();

        // Should not add when id is not 0
        loveInterest.setId(1);
        when(userCredentialRepository.findByUserId(loveInterest.getUserId())).thenReturn(makeUser());
        Result<LoveInterest> actual = service.add(loveInterest);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when nickname and firstname is null
        loveInterest = makeLoveInterest();
        loveInterest.setNickname(null);
        loveInterest.setFirstName(null);
        actual = service.add(loveInterest);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when nickname and firstname is blank
        loveInterest = makeLoveInterest();
        loveInterest.setNickname("");
        loveInterest.setFirstName("");
        actual = service.add(loveInterest);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when birthday is in the future
        loveInterest = makeLoveInterest();
        loveInterest.setBirthday(LocalDate.now().plusYears(1));
        actual = service.add(loveInterest);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when UserId does not exist
        loveInterest = makeLoveInterest();
        loveInterest.setUserId(99);

        when(userCredentialRepository.findByUserId(loveInterest.getUserId())).thenReturn(null);

        actual = service.add(loveInterest);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldUpdateNickname() {
        LoveInterest loveInterest = makeLoveInterest();
        loveInterest.setFirstName(null);
        loveInterest.setLastName(null);
        loveInterest.setId(1);

        when(loveInterestRepository.update(loveInterest)).thenReturn(true);
        when(userCredentialRepository.findByUserId(loveInterest.getUserId())).thenReturn(makeUser());

        Result<LoveInterest> actual = service.update(loveInterest);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldUpdateFirstName() {
        LoveInterest loveInterest = makeLoveInterest();
        loveInterest.setNickname(null);
        loveInterest.setId(1);

        when(loveInterestRepository.update(loveInterest)).thenReturn(true);
        when(userCredentialRepository.findByUserId(loveInterest.getUserId())).thenReturn(makeUser());

        Result<LoveInterest> actual = service.update(loveInterest);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldNotUpdateWhenMissing() {
        LoveInterest loveInterest = makeLoveInterest();
        loveInterest.setId(99);

        when(loveInterestRepository.update(loveInterest)).thenReturn(true);
        when(userCredentialRepository.findByUserId(loveInterest.getUserId())).thenReturn(makeUser());

        Result<LoveInterest> actual = service.update(loveInterest);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldNotUpdateWhenInvalid() {
        LoveInterest loveInterest = makeLoveInterest();

        // Should not update when id is 0
        loveInterest.setId(0);
        Result<LoveInterest> actual = service.update(loveInterest);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not update when nickname and firstname is null
        loveInterest = makeLoveInterest();
        loveInterest.setId(2);
        loveInterest.setNickname(null);
        loveInterest.setFirstName(null);
        actual = service.update(loveInterest);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not update when nickname and firstname is blank
        loveInterest = makeLoveInterest();
        loveInterest.setId(2);
        loveInterest.setNickname("");
        loveInterest.setFirstName("");
        actual = service.update(loveInterest);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not update when birthday is in the future
        loveInterest = makeLoveInterest();
        loveInterest.setId(2);
        loveInterest.setBirthday(LocalDate.now().plusYears(1));
        actual = service.update(loveInterest);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not update when UserId does not exist
        loveInterest = makeLoveInterest();
        loveInterest.setId(2);
        loveInterest.setUserId(99);

        when(userCredentialRepository.findByUserId(loveInterest.getUserId())).thenReturn(null);

        actual = service.update(loveInterest);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    private LoveInterest makeLoveInterest() {
        LoveInterest loveInterest = new LoveInterest();

        loveInterest.setNickname("Sweetheart");
        loveInterest.setFirstName("Emma");
        loveInterest.setLastName("Doe");
        loveInterest.setGender(Gender.FEMALE);
        loveInterest.setBirthday(LocalDate.of(1998, 12, 26));
        loveInterest.setLikes(Arrays.asList("Reading", "Gardening", "Yoga"));
        loveInterest.setDislikes(Arrays.asList("Crowds", "Spicy Food"));
        loveInterest.setPhotoUrl("");
        loveInterest.setUserId(1);

        return loveInterest;
    }

    private UserCredential makeUser() {
        UserCredential userCredential = new UserCredential();

        userCredential.setId(1);

        return userCredential;
    }
}