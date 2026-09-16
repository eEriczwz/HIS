package com.neuedu.outpatient.service.impl;

import com.neuedu.outpatient.entity.DisposalRequest;
import com.neuedu.outpatient.mapper.DisposalRequestMapper;
import com.neuedu.outpatient.service.DisposalRequestService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class DisposalRequestServiceImpl implements DisposalRequestService {

    @Resource
    private DisposalRequestMapper disposalRequestMapper;

    @Override
    public int addDisposalRequest(DisposalRequest disposalRequest) {
        return disposalRequestMapper.insert(disposalRequest);
    }

    @Override
    public List<DisposalRequest> getByRegisterId(Integer registerId) {
        return disposalRequestMapper.selectByRegisterId(registerId);
    }

    @Override
    public DisposalRequest getById(Integer id) {
        return disposalRequestMapper.selectById(id);
    }
}
