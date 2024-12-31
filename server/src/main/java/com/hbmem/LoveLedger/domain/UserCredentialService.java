package com.hbmem.LoveLedger.domain;

import com.hbmem.LoveLedger.data.UserCredentialRepository;
import com.hbmem.LoveLedger.model.UserCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserCredentialService {
    @Autowired
    private UserCredentialRepository userCredentialRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public UserCredential findByUserId(int id) {
        return userCredentialRepository.findByUserId(id);
    }

    public Result<String> authenticate(UserCredential userCredential) {
        Result<String> result = new Result<>();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userCredential.getEmail(), userCredential.getPassword())
        );

        if (authentication.isAuthenticated()) {
            result.setPayload(jwtService.generateToken(userCredential.getEmail()));
        } else {
            result.addMessage("Not Authorized", ResultType.INVALID);
        }
        return result;
    }
    public Result<UserCredential> signup(UserCredential userCredential) {
        Result<UserCredential> result = validate(userCredential);

        if (!result.isSuccess()) {
            return result;
        }

        if (userCredential.getId() != 0) {
            result.addMessage("User Credential's id already exist within the database", ResultType.INVALID);
            return result;
        }

        userCredential.setPassword(encoder.encode(userCredential.getPassword()));
        userCredential.setRoles(List.of("BASIC"));
        result.setPayload(userCredentialRepository.add(userCredential));
        return result;
    }

    private Result<UserCredential> validate(UserCredential userCredential) {
        Result<UserCredential> result = new Result<>();

        if (userCredential == null) {
            result.addMessage("User Credential cannot be null", ResultType.INVALID);
            return result;
        }

        // Email must not be blank or null
        if (Validations.isNullOrBlank(userCredential.getEmail())) {
            result.addMessage("Email must not be empty", ResultType.INVALID);
        }

        // Password must not be blank or null
        if (Validations.isNullOrBlank(userCredential.getPassword())) {
            result.addMessage("Password must not be empty", ResultType.INVALID);
        }

        // Email must be a valid email
        if (Validations.validEmail(userCredential.getEmail())) {
            result.addMessage("Email is not a valid email", ResultType.INVALID);
        }

        // Password must be longer than 12
        if (userCredential.getPassword().length() < 12) {
            result.addMessage("Password must be longer than 12 characters", ResultType.INVALID);
        }

        return result;
    }
}
