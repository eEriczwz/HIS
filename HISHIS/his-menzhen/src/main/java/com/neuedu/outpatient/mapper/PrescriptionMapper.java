package com.neuedu.outpatient.mapper;

import com.neuedu.outpatient.entity.Prescription;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface PrescriptionMapper {
    //新增处方
    int insert(Prescription prescription);
    //根据挂号id查询该患者所有处方
    List<Prescription> selectByRegisterId(@Param("registerId") Integer registerId);
    //根据id查询处方
    Prescription selectById(@Param("id") Integer id);
}
