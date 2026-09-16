package com.neuedu.outpatient.service;

import com.neuedu.outpatient.vo.MedicalRecordVO;

public interface VisitDetailService {
    //根据挂号id，查询患者本次就诊全部资料
    MedicalRecordVO getMedicalRecordDetail(Integer registerId);
}
