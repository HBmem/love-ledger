package com.hbmem.LoveLedger.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hbmem.LoveLedger.model.LoveInterest;

import java.util.List;

public interface LoveInterestRepository {
    LoveInterest findLoveInterestById(int id);

    List<LoveInterest> findAllLoveInterestByUserId(int userId);

    LoveInterest add(LoveInterest loveInterest) throws JsonProcessingException;

    boolean update(LoveInterest loveInterest);

    boolean delete(int id);
}
