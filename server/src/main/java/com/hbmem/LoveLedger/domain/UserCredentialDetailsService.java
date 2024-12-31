package com.hbmem.LoveLedger.domain;

import com.hbmem.LoveLedger.data.UserCredentialRepository;
import com.hbmem.LoveLedger.model.UserCredential;
import com.hbmem.LoveLedger.model.UserCredentialDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserCredentialDetailsService implements UserDetailsService {
    @Autowired
    private UserCredentialRepository userCredentialRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserCredential userCredential = userCredentialRepository.findByUsername(username);

        if (userCredential == null || userCredential.isDisabled()) {
            throw new UsernameNotFoundException(username + " not found.");
        }

        return new UserCredentialDetails(userCredential);
    }
}
