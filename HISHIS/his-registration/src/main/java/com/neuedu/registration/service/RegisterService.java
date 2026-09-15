package com.neuedu.registration.service;

import com.neuedu.registration.dto.RegisterDTO;
import com.neuedu.registration.dto.RegisterQueryDTO;
import com.neuedu.registration.entity.Register;
import java.util.List;

public interface RegisterService {
    String createRegister(RegisterDTO dto);
    List<Register> getList(RegisterQueryDTO queryDTO);
    Register getById(Integer id);
    boolean changeState(Integer id, Integer visitState);
}
