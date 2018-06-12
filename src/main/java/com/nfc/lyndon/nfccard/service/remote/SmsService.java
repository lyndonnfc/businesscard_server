package com.nfc.lyndon.nfccard.service.remote;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nfc.lyndon.nfccard.util.common.LocalHttpClient;
import com.nfc.lyndon.nfccard.util.log.LoggerInterface;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lyndon on 2018/6/13.
 */
@Service
public class SmsService implements LoggerInterface{

    private static final String SMS_HOST = "https://open.ucpaas.com/ol/sms/sendsms";

    @Value("${sms.server.sid}")
    private String sid;

    @Value("${sms.server.token}")
    private String token;

    @Value("${sms.server.appid}")
    private String appid;


    /**
     *
     * @param templateid
     * @param param
     * @param mobile
     * @param uid
     * @return
     */
    public JSONObject sendSms(String templateid, String param, String mobile,
                              String uid) {
        Map<String,Object> jsonObject = new HashMap<>();
        jsonObject.put("sid", sid);
        jsonObject.put("token", token);
        jsonObject.put("appid", appid);
        jsonObject.put("templateid", templateid);
        jsonObject.put("param", param);
        jsonObject.put("mobile", mobile);
        jsonObject.put("uid", uid);
        String result;
        try {
            logger.info("发送短信的参数: {}", JSON.toJSONString(jsonObject));
            result = LocalHttpClient.executePostJson(SMS_HOST, jsonObject);
            logger.info("短信发送返回返回推送结果: {}", result);
        } catch (Exception e) {
            logger.error("推送的管家消息失败: {}", e.getMessage());
            return null;
        }
        return JSON.parseObject(result);
    }

}
