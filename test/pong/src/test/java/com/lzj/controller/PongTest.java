package com.lzj.controller;


import com.lzj.utils.BaseResponse;
import com.lzj.utils.ErrorCode;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PongTest {

    @Mock
    private Pong pong;

    @Test
    void query() {

        BaseResponse response = pong.pongResponse("Hello");
        BaseResponse response1 = pong.pongResponse("Hello");
        if(Objects.nonNull(response)){
            System.out.println(response.getMessage());
            assertEquals(ErrorCode.SUCCESS.getCode(),response.getCode());
        }

        if(Objects.nonNull(response1)){
            System.out.println(response1.getMessage());
            assertEquals(ErrorCode.ADDITIONAL_REQUESTS.getCode(),response1.getCode());
        }
    }
}