package com.hbmem.LoveLedger.domain;

import com.hbmem.LoveLedger.data.UserCredentialRepository;
import com.hbmem.LoveLedger.model.UserCredential;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserCredentialServiceTest {
    @Autowired
    UserCredentialService service;

    @MockitoBean
    UserCredentialRepository userCredentialRepository;

    @Test
    void shouldAdd() {
        UserCredential userCredential = makeUserCredential();

        UserCredential mockOut = makeUserCredential();
        mockOut.setId(4);

        when(userCredentialRepository.add(userCredential)).thenReturn(mockOut);

        Result<UserCredential> actual = service.signup(userCredential);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(mockOut, actual.getPayload());
    }

    @Test
    void shouldNotAddWhenInvalid() {
        // Should not add when user credential is null
        Result<UserCredential> actual = service.signup(null);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when email is null
        UserCredential userCredential = makeUserCredential();
        userCredential.setEmail(null);

        actual = service.signup(userCredential);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when email is empty
        userCredential = makeUserCredential();
        userCredential.setEmail("");

        actual = service.signup(userCredential);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when email is not valid format
        userCredential = makeUserCredential();
        userCredential.setEmail("apple.apple");

        actual = service.signup(userCredential);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when password is null
        userCredential = makeUserCredential();
        userCredential.setPassword(null);

        actual = service.signup(userCredential);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when password is empty
        userCredential = makeUserCredential();
        userCredential.setPassword("");

        actual = service.signup(userCredential);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when password is less than 8 characters
        userCredential = makeUserCredential();
        userCredential.setPassword("1234567");

        actual = service.signup(userCredential);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldUpdate() {
        UserCredential userCredential = makeUserCredential();
        userCredential.setId(3);

        when(userCredentialRepository.update(userCredential)).thenReturn(true);

        Result<UserCredential> actual = service.update(userCredential);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldNotUpdateWhenMissing() {
        UserCredential userCredential = makeUserCredential();
        userCredential.setId(99);

        when(userCredentialRepository.update(makeUserCredential())).thenReturn(false);

        Result<UserCredential> actual = service.update(userCredential);
        assertEquals(ResultType.NOT_FOUND, actual.getType());
    }

    @Test
    void shouldNotUpdateWhenInvalid() {
        // Should not update when user credential is null
        Result<UserCredential> actual = service.update(null);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not update when email is null
        UserCredential userCredential = makeUserCredential();
        userCredential.setId(4);
        userCredential.setEmail(null);

        actual = service.update(userCredential);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not update when email is empty
        userCredential = makeUserCredential();
        userCredential.setId(4);
        userCredential.setEmail("");

        actual = service.update(userCredential);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not update when email is not valid format
        userCredential = makeUserCredential();
        userCredential.setId(4);
        userCredential.setEmail("apple.apple");

        actual = service.update(userCredential);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not update when password is null
        userCredential = makeUserCredential();
        userCredential.setId(4);
        userCredential.setPassword(null);

        actual = service.update(userCredential);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not update when password is empty
        userCredential = makeUserCredential();
        userCredential.setId(4);
        userCredential.setPassword("");

        actual = service.update(userCredential);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not update when password is less than 8 characters
        userCredential = makeUserCredential();
        userCredential.setId(4);
        userCredential.setPassword("1234567");

        actual = service.update(userCredential);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    private UserCredential makeUserCredential() {
        UserCredential userCredential = new UserCredential();

        userCredential.setEmail("test@realemail.com");
        userCredential.setPassword("test@123");

        List<String> roles = new ArrayList<>();
        roles.add("BASIC");
        userCredential.setRoles(roles);

        return userCredential;
    }
}