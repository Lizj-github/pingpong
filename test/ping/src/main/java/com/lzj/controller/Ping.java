package com.lzj.controller;

import cn.hutool.json.JSONUtil;
import com.lzj.utils.BaseResponse;
import com.lzj.utils.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: lzj
 * @create: 2024-06-24
 **/

@Slf4j
@Component
public class Ping {


    /**
     * 请求数量限制
     */
    private AtomicInteger atomicInteger = new AtomicInteger(0);

    /**
     * 请求地址
     */
    private static String URL_SRT = "http://localhost:8880/pong/req/pongResponse?request=Hello";

    @PostConstruct
    public void send() {

        log.info("----------------------doing-----------------------");
        log.info(String.valueOf(atomicInteger.get()));
        Flux.interval(Duration.ofSeconds(1))
                .flatMap(number -> {
                    log.info("flux : " + atomicInteger.get());
                    if (atomicInteger.get() < 2 ) {
                        atomicInteger.incrementAndGet();
                        log.info("flux : " + atomicInteger.get());
                        BaseResponse response =  getResult();
                        atomicInteger.decrementAndGet();
                        return Mono.just("code :" + response.getCode());
                    }else {
                        //超限
                        log.error("已超过限制");
                        return Mono.just("已超过限制");
                    }
                }).subscribe();
    }


    /**
     *  发起请求
     */
    public  BaseResponse getResult() {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        // HTTP Get请求
        HttpGet httpGet = new HttpGet(URL_SRT);
        String res = "";
        try {
            // 执行请求
            HttpResponse getAddrResp = httpClient.execute(httpGet);
            HttpEntity entity = getAddrResp.getEntity();
            if (entity != null) {
                res = EntityUtils.toString(entity);
            }
            try {
                log.info("请求返回数据为: " + res);
            } catch (Exception e) {
                log.error("解析有误 msg：" + e.getMessage());
            }
        } catch (Exception e) {
            log.error(" 执行有误 ："+e.getMessage(), e);
            return new BaseResponse(ErrorCode.SYSTEM_ERROR);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                log.error(" 关闭异常 ："+e.getMessage(), e);
                return new BaseResponse(ErrorCode.SYSTEM_ERROR);
            }
        }
        return JSONUtil.toBean(res,BaseResponse.class);
    }


}
