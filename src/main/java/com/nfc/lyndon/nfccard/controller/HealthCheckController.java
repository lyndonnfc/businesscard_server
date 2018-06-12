package com.nfc.lyndon.nfccard.controller;


import com.nfc.lyndon.nfccard.aop.BizDescription;
import com.nfc.lyndon.nfccard.entity.base.Result;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class HealthCheckController {

    @BizDescription("健康检查接口")
    @RequestMapping(value = "health", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Result checkHealth() {
        return Result.success(null,"接口正常");
    }

}
