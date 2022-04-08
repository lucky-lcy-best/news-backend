package com.hfut.newsbackend.pojo.base;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Lucky
 * @description: TODO
 * @date 2022/4/7 15:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("covid19_world")
public class WorldEpidemic {

    private Long id ;

    private String name ;

    private Long incConfirm ;

    private Long allConfirm ;

    private Long dead ;

    private Long heal ;

}
