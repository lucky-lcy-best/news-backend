package com.hfut.newsbackend.response;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author Lucky
 * @version 1.0
 * @description: 统一返回类型
 * @date 2022/3/11 16:25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    // 接口调用成功或者失败
    private Integer code = 0;
    // 失败的具体code
    private String errorCode = "";
    // 需要传递的信息，例如错误信息
    private String msg;
    // 需要传递的数据
    private T data;

    public Result(Object body) {

    }
}
