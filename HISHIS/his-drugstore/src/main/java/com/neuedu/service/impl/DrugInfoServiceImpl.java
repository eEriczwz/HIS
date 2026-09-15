package com.neuedu.service.impl;

import com.neuedu.entity.DrugInfo;
import com.neuedu.mapper.DrugInfoMapper;
import com.neuedu.service.DrugInfoService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class DrugInfoServiceImpl implements DrugInfoService {

    @Resource
    private DrugInfoMapper drugInfoMapper;

    @Override
    public List<DrugInfo> list(String drugCode, String drugName) {
        return drugInfoMapper.list(drugCode,drugName);
    }

    @Override
    public int add(DrugInfo drugInfo) {
        return drugInfoMapper.add(drugInfo);
    }

    @Override
    public int update(DrugInfo drugInfo) {
        return drugInfoMapper.update(drugInfo);
    }

    @Override
    public int deleteById(Integer id) {
        return drugInfoMapper.deleteById(id);
    }
}
