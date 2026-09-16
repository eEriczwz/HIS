package com.neuedu.service.impl;

import com.neuedu.entity.Prescription;
import com.neuedu.mapper.DrugInfoMapper;
import com.neuedu.mapper.PrescriptionMapper;
import com.neuedu.service.DispenseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.List;

@Service
public class DispenseServiceImpl implements DispenseService {

    @Resource
    private PrescriptionMapper prescriptionMapper;

    @Resource
    private DrugInfoMapper drugInfoMapper;

    @Override
    public List<Prescription> list(String state, String caseNumber, String drugName) {
        return prescriptionMapper.selectForDispense(state, caseNumber, drugName);
    }

    // 发药：扣减库存 + 处方状态置为已发药
    // 返回值：>0 成功；0 处方状态不对；-1 库存不足
    @Override
    @Transactional
    public int issue(Integer id) {
        Prescription p = prescriptionMapper.selectById(id);
        if (p == null || p.getDrugId() == null) {
            return 0;
        }
        String state = p.getDrugState();
        if (!"已开立".equals(state) && !"已缴费".equals(state)) {
            return 0;
        }
        // 扣库存（库存不足时 stockOut 返回 0）
        int deducted = drugInfoMapper.stockOut(p.getDrugId(), p.getDrugNumber());
        if (deducted <= 0) {
            return -1;
        }
        return prescriptionMapper.issue(id);
    }

    // 退药：回补库存 + 处方状态置为已退药
    @Override
    @Transactional
    public int returnDrug(Integer id) {
        Prescription p = prescriptionMapper.selectById(id);
        if (p == null || p.getDrugId() == null || !"已发药".equals(p.getDrugState())) {
            return 0;
        }
        drugInfoMapper.stockIn(p.getDrugId(), p.getDrugNumber());
        return prescriptionMapper.returnDrug(id);
    }
}
