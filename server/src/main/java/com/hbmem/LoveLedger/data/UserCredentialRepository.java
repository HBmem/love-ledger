package com.hbmem.LoveLedger.data;

import com.hbmem.LoveLedger.model.UserCredential;
import org.springframework.transaction.annotation.Transactional;

public interface UserCredentialRepository {
    @Transactional
    UserCredential findByUserId(int id);

    @Transactional
    UserCredential findByUsername(String username);

    @Transactional
    UserCredential add(UserCredential userCredential);

    @Transactional
    void update(UserCredential userCredential);
}
