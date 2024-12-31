package com.hbmem.LoveLedger.data;

import com.hbmem.LoveLedger.model.UserCredential;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserCredentialJdbcTemplateRepositoryTest {
    @Autowired
    UserCredentialJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindEachById() {
        UserCredential user1 = repository.findByUserId(1);
        assertNotNull(user1);
        assertEquals(1, user1.getId());
        assertEquals("adam@email.com", user1.getEmail());
        assertEquals("$2y$12$09myHJmbBR6J4nbT4j8yluvOZwOFfWebVpVldcQhHfpS5xmWrbxH2", user1.getPassword());
        assertTrue(user1.hasRole("ADMIN"));

        UserCredential user2 = repository.findByUserId(2);
        assertNotNull(user2);
        assertEquals(2, user2.getId());
        assertEquals("brit@email.com", user2.getEmail());
        assertEquals("$2y$12$Uj3aXL0vXAFt.AFtyEw19uDs4Jv0jN3oRR0aet.PoC/aTcaRFIoke", user2.getPassword());
        assertTrue(user2.hasRole("PREMIUM"));

        UserCredential user3 = repository.findByUserId(3);
        assertNotNull(user3);
        assertEquals(3, user3.getId());
        assertEquals("carl@email.com", user3.getEmail());
        assertEquals("$2y$12$JGt..bPM5UYQG6l.yIngN.frjBICxIKAdsl.cdI3m08zOzoazUjdy", user3.getPassword());
        assertTrue(user3.hasRole("BASIC"));
    }

    @Test
    void shouldAddNewUserCredential() {
        UserCredential userCredential = makeUserCredential();
        UserCredential actual = repository.add(userCredential);
        assertNotNull(actual);
        assertEquals(4, actual.getId());
    }

    @Test
    void shouldUpdateExistingUser() {
        UserCredential userCredential = repository.findByUserId(2);
        userCredential.setVerified(true);
        assertTrue(repository.update(userCredential));
    }

    @Test
    void shouldDisableExitingUser() {
        UserCredential userCredential = repository.findByUserId(3);
        assertTrue(repository.disable(userCredential.getId()));
    }

    private UserCredential makeUserCredential() {
        UserCredential userCredential = new UserCredential();

        userCredential.setEmail("test@email.com");
        userCredential.setPassword("test@1234");

        List<String> roles = new ArrayList<>();
        roles.add("BASIC");
        userCredential.setRoles(roles);

        return userCredential;
    }
}