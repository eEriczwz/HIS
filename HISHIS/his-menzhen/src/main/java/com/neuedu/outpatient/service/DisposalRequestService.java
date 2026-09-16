package com.neuedu.outpatient.service;

import com.neuedu.outpatient.entity.DisposalRequest;
import java.util.List;

public interface DisposalRequestService {
    int addDisposalRequest(DisposalRequest disposalRequest);
    List<DisposalRequest> getByRegisterId(Integer registerId);
    DisposalRequest getById(Integer id);
}
