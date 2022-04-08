package com.hfut.newsbackend.controller;

import com.hfut.newsbackend.response.ResponseResult;
import com.hfut.newsbackend.service.impl.EpidemicServiceImpl;
import com.hfut.newsbackend.service.inter.EpidemicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Lucky
 * @description: TODO
 * @date 2022/4/7 15:17
 */
@RestController
public class EpimedicController {

    @Autowired
    private EpidemicServiceImpl epidemicService ;

    /**
     * TODO 获取世界疫情
     */
    @GetMapping("/epidemic/world")
    public ResponseResult getWorld() {
        return epidemicService.getWorld() ;
    }
}
