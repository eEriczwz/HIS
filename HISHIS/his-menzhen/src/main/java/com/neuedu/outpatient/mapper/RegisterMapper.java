package com.neuedu.outpatient.mapper;

import com.neuedu.outpatient.entity.Register;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface RegisterMapper {
    // 查询当前医生待接诊患者 visit_state=1
    List<Register> selectWaitPatientByDoctorId(@Param("employeeId") Integer employeeId);
    // 修改挂号就诊状态（接诊）
    int updateVisitState(@Param("id") Integer id, @Param("visitState") Integer visitState);
    // 根据挂号ID查询挂号信息
    Register selectById(@Param("id") Integer id);
}
