package com.lzj;

import com.lzj.controller.Ping;
import com.lzj.utils.BaseResponse;
import com.lzj.utils.ErrorCode;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class PingTest {

    @Mock
    private Ping ping;

    @Test
    void send() {
        ping.send();
        Mockito.verify(ping).send();
        assertTrue(true);
    }

    @Test
    void getResult() {
        BaseResponse<String> response = new BaseResponse(ErrorCode.SUCCESS);
        Mockito.when(ping.getResult()).thenReturn(response);
        assertEquals(ErrorCode.SUCCESS.getCode(),response.getCode());
    }

}