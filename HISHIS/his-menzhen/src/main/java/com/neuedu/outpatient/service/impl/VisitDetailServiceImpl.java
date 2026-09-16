package com.neuedu.outpatient.service.impl;

import com.neuedu.outpatient.entity.*;
import com.neuedu.outpatient.mapper.*;
import com.neuedu.outpatient.service.*;
import com.neuedu.outpatient.vo.MedicalRecordVO;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class VisitDetailServiceImpl implements VisitDetailService {

    @Resource
    private RegisterService registerService;
    @Resource
    private MedicalRecordService medicalRecordService;
    @Resource
    private CheckRequestService checkRequestService;
    @Resource
    private DisposalRequestService disposalRequestService;
    @Resource
    private PrescriptionService prescriptionService;
    @Resource
    private DiseaseMapper diseaseMapper;

    @Override
    public MedicalRecordVO getMedicalRecordDetail(Integer registerId) {
        MedicalRecordVO vo = new MedicalRecordVO();

        //1.挂号信息
        vo.setRegister(registerService.getRegisterById(registerId));

        //2.病历
        MedicalRecord record = medicalRecordService.getByRegisterId(registerId);
        vo.setMedicalRecord(record);

        //3.病历绑定的疾病
        if(record != null){
            List<Disease> diseaseList = diseaseMapper.selectByMedicalRecordId(record.getId());
            vo.setDiseaseList(diseaseList);
        }

        //4.检查单、处置单、处方
        vo.setCheckList(checkRequestService.getByRegisterId(registerId));
        vo.setDisposalList(disposalRequestService.getByRegisterId(registerId));
        vo.setPrescriptionList(prescriptionService.getByRegisterId(registerId));

        return vo;
    }
}
