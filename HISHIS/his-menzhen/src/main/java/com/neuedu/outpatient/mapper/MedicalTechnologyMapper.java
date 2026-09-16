package com.neuedu.outpatient.mapper;

import com.neuedu.outpatient.entity.MedicalTechnology;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface MedicalTechnologyMapper {
    // 按类型（检查/检验/处置）与名称/编码模糊查询医技项目
    List<MedicalTechnology> selectByType(@Param("techType") String techType, @Param("keyword") String keyword);
}
