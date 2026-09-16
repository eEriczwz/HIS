package com.neuedu.outpatient.mapper;

import com.neuedu.outpatient.entity.MedicalRecord;
import org.apache.ibatis.annotations.Param;

public interface MedicalRecordMapper {
    // 根据挂号ID查询病历（判断是否已经存在病历）
    MedicalRecord selectByRegisterId(@Param("registerId") Integer registerId);
    // 新增病历
    int insert(MedicalRecord medicalRecord);
    // 根据id查询病历详情
    MedicalRecord selectById(@Param("id") Integer id);
}
