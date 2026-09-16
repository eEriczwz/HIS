package com.neuedu.outpatient.mapper;

import com.neuedu.outpatient.entity.Disease;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface DiseaseMapper {
    //通过病历id查询该病历绑定的所有疾病（多对多联查）
    List<Disease> selectByMedicalRecordId(@Param("medicalRecordId") Integer medicalRecordId);
    //根据id批量查询疾病
    List<Disease> selectByIds(@Param("ids") List<Integer> ids);
    //按名称/编码/ICD 模糊查询疾病（诊断多选下拉）
    List<Disease> selectByKeyword(@Param("keyword") String keyword);
}
