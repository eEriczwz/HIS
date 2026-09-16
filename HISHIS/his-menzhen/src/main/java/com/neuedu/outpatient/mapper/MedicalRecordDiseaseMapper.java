package com.neuedu.outpatient.mapper;

import com.neuedu.outpatient.entity.MedicalRecordDisease;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface MedicalRecordDiseaseMapper {
    //批量新增病历疾病
    int batchInsert(@Param("list") List<MedicalRecordDisease> list);
    //根据病历id删除所有疾病关联
    int deleteByMedicalRecordId(@Param("medicalRecordId") Integer medicalRecordId);
}
