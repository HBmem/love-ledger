package com.hbmem.LoveLedger.data;

import com.hbmem.LoveLedger.model.Milestone;

import java.util.List;

public interface MilestoneRepository {
    Milestone findById(int id);

    List<Milestone> findAllMilestones();

    Milestone add(Milestone milestone);

    boolean update(Milestone milestone);

    boolean delete(int id);
}
