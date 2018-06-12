package com.nfc.lyndon.nfccard.service.nfccard;

import com.alibaba.fastjson.JSONObject;
import com.nfc.lyndon.nfccard.dao.nfcard.UserInfoMapper;
import com.nfc.lyndon.nfccard.entity.nfccard.UserInfo;
import com.nfc.lyndon.nfccard.service.remote.SmsService;
import com.nfc.lyndon.nfccard.util.common.MathUtils;
import com.nfc.lyndon.nfccard.util.log.LoggerInterface;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * Created by lyndon on 2018/6/13.
 */
@Service
public class UserInfoService implements LoggerInterface{

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private SmsService smsService;


    public int sendVerifyCode(String mobile) {
        String verifyCode = MathUtils.getFourRandom();
        logger.info("发送的验证码为:{}",verifyCode);
        JSONObject jsonObject = smsService.sendSms("335312", verifyCode.concat(",5"),mobile,null);
        if (StringUtils.isNotBlank(jsonObject.getString("smsid"))) {
            return this.saveUserInfo(verifyCode,mobile);
        }
        return 0;
    }

    public UserInfo getUserInfoByVerifyCodeAndMobile(String verifyCode,String mobile) {
        UserInfo userInfo = new UserInfo();
        userInfo.setVefifyCode(verifyCode);
        userInfo.setMobile(mobile);
        return userInfoMapper.selectByConditions(userInfo);
    }

    private int saveUserInfo(String verifyCode,String mobile) {
        UserInfo userInfo = userInfoMapper.selectByMobile(mobile);
        if (userInfo == null) {
            userInfo = new UserInfo();
            userInfo.setVefifyCode(verifyCode);
            userInfo.setMobile(mobile);
            userInfo.setExpiryTime(LocalDateTime.now().plusMinutes(5));
            return userInfoMapper.insertSelective(userInfo);
        } else {
            userInfo.setVefifyCode(verifyCode);
            userInfo.setMobile(mobile);
            userInfo.setExpiryTime(LocalDateTime.now().plusMinutes(5));
            return userInfoMapper.updateByPrimaryKeySelective(userInfo);
        }
    }
}
