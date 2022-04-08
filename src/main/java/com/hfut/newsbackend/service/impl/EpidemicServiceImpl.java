package com.hfut.newsbackend.service.impl;

import com.hfut.newsbackend.mapper.WorldEpidemicMapper;
import com.hfut.newsbackend.response.ResponseResult;
import com.hfut.newsbackend.service.inter.EpidemicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Lucky
 * @description: TODO
 * @date 2022/4/7 15:21
 */
@Service
public class EpidemicServiceImpl implements EpidemicService {

    @Autowired
    private WorldEpidemicMapper worldEpidemicMapper ;
    /**
     * TODO 获取世界疫情
     * @return
     */
    @Override
    public ResponseResult getWorld() {
        return new ResponseResult(200 , "获取世界疫情成功" ,worldEpidemicMapper.selectList(null) );
    }
}
