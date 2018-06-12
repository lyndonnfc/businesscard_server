package com.nfc.lyndon.nfccard.controller;

import com.alibaba.fastjson.JSONObject;
import com.nfc.lyndon.nfccard.aop.BizDescription;
import com.nfc.lyndon.nfccard.entity.base.Result;
import com.nfc.lyndon.nfccard.entity.nfccard.UserInfo;
import com.nfc.lyndon.nfccard.service.nfccard.UserInfoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * Created by lyndon on 2018/6/13.
 */
@RestController
@RequestMapping("user")
public class UserInfoController {

    @Resource
    private UserInfoService userInfoService;

    @BizDescription("获取验证码接口")
    @GetMapping(value = "getVerifyCode", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getVerifyCode(String mobile) {
        if (StringUtils.isBlank(mobile)) {
            return Result.error("手机号不能为空");
        }
        int count = userInfoService.sendVerifyCode(mobile);
        if (count > 0) {
            return Result.success(null,"获取验证码成功");
        }
        return Result.error("获取验证码失败，请重试");
    }

    @BizDescription("获取验证码接口")
    @PostMapping(value = "login", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result login(@RequestBody UserInfo user) {
        if (StringUtils.isBlank(user.getMobile())) {
            return Result.error("手机号不能为空");
        }
        if (StringUtils.isBlank(user.getVefifyCode())) {
            return Result.error("验证码不能为空");
        }
        UserInfo userInfo = userInfoService.getUserInfoByVerifyCodeAndMobile(user.getVefifyCode(),user.getMobile());
        if (userInfo != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("uid",userInfo.getId());
            jsonObject.put("isValid", LocalDateTime.now().isBefore(userInfo.getExpiryTime()));
            return Result.success(jsonObject,"登录成功");
        }
        return Result.error("登录失败，请重试");
    }

}
