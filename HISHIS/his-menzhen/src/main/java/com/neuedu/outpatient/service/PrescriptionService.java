package com.neuedu.outpatient.service;

import com.neuedu.outpatient.entity.Prescription;
import java.util.List;

public interface PrescriptionService {
    int addPrescription(Prescription prescription);
    List<Prescription> getByRegisterId(Integer registerId);
    Prescription getById(Integer id);
}
