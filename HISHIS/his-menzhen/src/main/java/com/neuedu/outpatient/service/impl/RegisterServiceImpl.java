package com.neuedu.outpatient.service.impl;

import com.neuedu.outpatient.entity.Register;
import com.neuedu.outpatient.mapper.RegisterMapper;
import com.neuedu.outpatient.service.RegisterService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Resource
    private RegisterMapper registerMapper;

    @Override
    public List<Register> getWaitPatient(Integer employeeId) {
        return registerMapper.selectWaitPatientByDoctorId(employeeId);
    }

    @Override
    public int changeVisitState(Integer id, Integer visitState) {
        return registerMapper.updateVisitState(id,visitState);
    }

    @Override
    public Register getRegisterById(Integer id) {
        return registerMapper.selectById(id);
    }
}
