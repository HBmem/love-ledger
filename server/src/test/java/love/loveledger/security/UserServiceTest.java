package love.loveledger.security;

import love.loveledger.data.UserRepository;
import love.loveledger.models.AppUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserServiceTest {
    @Autowired
    UserService service;

    @MockBean
    UserRepository userRepository;

    @Test
    void shouldLoadByUsername() {
        AppUser mockUser = makeUser();

        when(userRepository.findByUsername("JohnD")).thenReturn(mockUser);

        UserDetails userDetails = service.loadUserByUsername("JohnD");

        assertEquals("JohnD", userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void shouldThrowExceptionIfUserNotFound() {
        when(userRepository.findByUsername("invalidUser")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            service.loadUserByUsername("invalidUser");
        });
    }

    private AppUser makeUser() {
        AppUser appUser = new AppUser();

        appUser.setUsername("JohnD");
        appUser.setPassword("TEST");
        appUser.getRoles().add("USER");

        return appUser;
    }
}