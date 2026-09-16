package com.neuedu.outpatient.service.impl;

import com.neuedu.outpatient.entity.CheckRequest;
import com.neuedu.outpatient.mapper.CheckRequestMapper;
import com.neuedu.outpatient.service.CheckRequestService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class CheckRequestServiceImpl implements CheckRequestService {

    @Resource
    private CheckRequestMapper checkRequestMapper;

    @Override
    public int addCheckRequest(CheckRequest checkRequest) {
        return checkRequestMapper.insert(checkRequest);
    }

    @Override
    public List<CheckRequest> getByRegisterId(Integer registerId) {
        return checkRequestMapper.selectByRegisterId(registerId);
    }

    @Override
    public CheckRequest getById(Integer id) {
        return checkRequestMapper.selectById(id);
    }
}
