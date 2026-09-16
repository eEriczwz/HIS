package com.neuedu.outpatient.service;

import com.neuedu.outpatient.entity.Register;
import java.util.List;

public interface RegisterService {
    List<Register> getWaitPatient(Integer employeeId);
    int changeVisitState(Integer id,Integer visitState);
    Register getRegisterById(Integer id);
}
