package com.neuedu.service;

import com.neuedu.entity.Prescription;
import java.util.List;

public interface DispenseService {
    List<Prescription> list(String state, String caseNumber, String drugName);
    int issue(Integer id);
    int returnDrug(Integer id);
}
