package love.loveledger.security;

import love.loveledger.data.UserRepository;
import love.loveledger.models.AppUser;
import love.loveledger.models.Gender;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        ensureAdmin();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findByUsername(username);

        if (user == null || user.isDisabled()) {
            throw new UsernameNotFoundException(username + " not found.");
        }

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName))
                .collect(Collectors.toList());

        return new User(user.getUsername(), user.getPassword(), authorities);
    }

    public AppUser add(AppUser user) {
        validate(user);
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.add(user);
    }
    
    private void validate(AppUser user) {
        if (user == null) {
            throw new ValidationException("user cannot be null");
        }

        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new ValidationException("username is required");
        }

        if (user.getUsername().length() > 50) {
            throw new ValidationException("username must be less than 50 characters");
        }
    }

    private void ensureAdmin() {
        AppUser user = userRepository.findByUsername("admin");

        if (user == null) {
            String randomPassword = UUID.randomUUID().toString();

            user = new AppUser();
            user.setUsername("admin");
            user.setPassword(randomPassword);
            user.getRoles().add("ADMIN");

            user.setEmail("admin@email.com");
            user.setGender(Gender.NON_BINARY);

            try {
                add(user);
                System.out.printf("%n%nRandom admin password: %s%n%n", randomPassword);
            } catch (ValidationException ex) {
                ex.printStackTrace();
            }
        }
    }
}
