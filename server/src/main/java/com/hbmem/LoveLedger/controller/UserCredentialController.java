package com.hbmem.LoveLedger.controller;

import com.hbmem.LoveLedger.domain.Result;
import com.hbmem.LoveLedger.domain.UserCredentialService;
import com.hbmem.LoveLedger.model.UserCredential;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserCredentialController {
    private final UserCredentialService userCredentialService;

    public UserCredentialController(UserCredentialService userCredentialService) {
        this.userCredentialService = userCredentialService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserCredential> findByUserId(@PathVariable int id) {
        UserCredential userCredential = userCredentialService.findByUserId(id);

        if (userCredential == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(userCredential);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserCredential userCredential) {
        Result<String> result = userCredentialService.authenticate(userCredential);
        if (!result.isSuccess()) {
            return new ResponseEntity<>(result.getMessages(), HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(result.getPayload(), HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody UserCredential userCredential) {
        Result<UserCredential> result = userCredentialService.signup(userCredential);
        if (!result.isSuccess()) {
            return new ResponseEntity<>(result.getMessages(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
    }
}
