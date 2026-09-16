package com.neuedu.outpatient.service;

import com.neuedu.outpatient.entity.MedicalRecord;

public interface MedicalRecordService {
    MedicalRecord getByRegisterId(Integer registerId);
    int addRecord(MedicalRecord medicalRecord);
    MedicalRecord getById(Integer id);
}
