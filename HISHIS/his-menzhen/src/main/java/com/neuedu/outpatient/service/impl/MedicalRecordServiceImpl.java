package com.neuedu.outpatient.service.impl;

import com.neuedu.outpatient.entity.MedicalRecord;
import com.neuedu.outpatient.mapper.MedicalRecordMapper;
import com.neuedu.outpatient.service.MedicalRecordService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    @Resource
    private MedicalRecordMapper medicalRecordMapper;

    @Override
    public MedicalRecord getByRegisterId(Integer registerId) {
        return medicalRecordMapper.selectByRegisterId(registerId);
    }

    @Override
    public int addRecord(MedicalRecord medicalRecord) {
        return medicalRecordMapper.insert(medicalRecord);
    }

    @Override
    public MedicalRecord getById(Integer id) {
        return medicalRecordMapper.selectById(id);
    }
}
