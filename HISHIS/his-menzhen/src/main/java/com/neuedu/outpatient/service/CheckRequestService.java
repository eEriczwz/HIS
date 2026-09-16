package com.neuedu.outpatient.service;

import com.neuedu.outpatient.entity.CheckRequest;
import java.util.List;

public interface CheckRequestService {
    int addCheckRequest(CheckRequest checkRequest);
    List<CheckRequest> getByRegisterId(Integer registerId);
    CheckRequest getById(Integer id);
}
