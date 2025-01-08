package com.hbmem.LoveLedger.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hbmem.LoveLedger.model.UserProfile;

public interface UserProfileRepository {
    UserProfile findByUserProfileId(int id);

    UserProfile add(UserProfile userProfile) throws JsonProcessingException;

    boolean update(UserProfile userProfile);

    boolean delete(int id);
}
