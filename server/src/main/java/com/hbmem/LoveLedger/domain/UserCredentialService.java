package com.hbmem.LoveLedger.domain;

import com.hbmem.LoveLedger.data.UserCredentialRepository;
import com.hbmem.LoveLedger.model.UserCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
                new UsernamePasswordAuthenticationToken(userCredential.getUsername(), userCredential.getPassword())
        );

        if (authentication.isAuthenticated()) {
            result.setPayload(jwtService.generateToken(userCredential.getUsername()));
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
        result.setPayload(userCredentialRepository.add(userCredential));
        return result;
    }

    private Result<UserCredential> validate(UserCredential userCredential) {
        Result<UserCredential> result = new Result<>();

        if (userCredential == null) {
            result.addMessage("User Credential cannot be null", ResultType.INVALID);
            return result;
        }

        return result;
    }
}
