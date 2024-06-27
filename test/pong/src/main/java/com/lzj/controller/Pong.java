package com.lzj.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.lzj.utils.BaseResponse;
import com.lzj.utils.ErrorCode;
import com.lzj.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: lzj
 * @create: 2024/6/24
 **/
@Slf4j
@RequestMapping(("/pong/req"))
@RestController
public class Pong {

    // 每秒不超过1个请求
    private static RateLimiter rateLimiter = RateLimiter.create(1.0);

    /**
     * @Author: lzj
     * @Date: 2024/6/27
     */
    @GetMapping("/pongResponse")
    public BaseResponse<String> pongResponse(String request) {
        log.info("请求参数： " + request);
        if (rateLimiter.tryAcquire()) {
            //对于任何给定的一秒，只有1请求都可以通过它来处理
            return ResultUtils.success("World");
        }else {
            //对于那些在给定秒内出现的额外请求，服务应该返回429状态代码。
            return ResultUtils.error(ErrorCode.ADDITIONAL_REQUESTS);

        }
    }


}

