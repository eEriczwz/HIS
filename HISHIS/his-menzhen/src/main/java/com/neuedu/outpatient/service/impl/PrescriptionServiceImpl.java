package com.neuedu.outpatient.service.impl;

import com.neuedu.outpatient.entity.Prescription;
import com.neuedu.outpatient.mapper.DrugInfoMapper;
import com.neuedu.outpatient.mapper.PrescriptionMapper;
import com.neuedu.outpatient.service.PrescriptionService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {

    @Resource
    private PrescriptionMapper prescriptionMapper;

    @Resource
    private DrugInfoMapper drugInfoMapper;

    // 新增处方：先校验库存，库存不足返回 -1
    @Override
    public int addPrescription(Prescription prescription) {
        Integer stock = drugInfoMapper.selectStockById(prescription.getDrugId());
        int need = prescription.getDrugNumber() == null ? 0 : prescription.getDrugNumber();
        if (stock == null || stock < need) {
            return -1;
        }
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
