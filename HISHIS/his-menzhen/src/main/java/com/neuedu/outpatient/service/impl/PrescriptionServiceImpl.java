package com.neuedu.outpatient.service.impl;

import com.neuedu.outpatient.entity.Prescription;
import com.neuedu.outpatient.mapper.PrescriptionMapper;
import com.neuedu.outpatient.service.PrescriptionService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {

    @Resource
    private PrescriptionMapper prescriptionMapper;

    @Override
    public int addPrescription(Prescription prescription) {
        return prescriptionMapper.insert(prescription);
    }

    @Override
    public List<Prescription> getByRegisterId(Integer registerId) {
        return prescriptionMapper.selectByRegisterId(registerId);
    }

    @Override
    public Prescription getById(Integer id) {
        return prescriptionMapper.selectById(id);
    }
}
