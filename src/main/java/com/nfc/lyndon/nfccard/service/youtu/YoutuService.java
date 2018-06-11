package com.nfc.lyndon.nfccard.service.youtu;

import com.nfc.lyndon.nfccard.exception.BusinessException;
import com.nfc.lyndon.nfccard.util.log.LoggerInterface;
import com.nfc.lyndon.nfccard.util.youtu.Youtu;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by lyndon on 2018/6/2.
 */
@Service
public class YoutuService implements LoggerInterface{

    public static final String APP_ID = "10133693";
    public static final String SECRET_ID = "AKIDKbq1415iNqFvVxJsmZFl6FD1e6b0hTXV";
    public static final String SECRET_KEY = "0pUBk9XnxyHVZu691jZSDsTt9MRQ4c1I";
    public static final String USER_ID = "1024459475";


    public String getRecognitionItemsStr(String imgUrl){
        Youtu faceYoutu = new Youtu(APP_ID, SECRET_ID, SECRET_KEY,Youtu.API_YOUTU_END_POINT,USER_ID);
        JSONObject jsonObject = null;
        try {
            jsonObject = faceYoutu.BcOcrUrl(imgUrl);
            if (jsonObject != null) {
                logger.info("识别结果为：{}" , jsonObject.get("items").toString());
                return jsonObject.get("items").toString();
            }
        } catch (IOException e) {
            throw new BusinessException("名片识别识别,请重试");
        } catch (JSONException e) {
            throw new BusinessException("名片识别识别,请重试");
        } catch (KeyManagementException e) {
            throw new BusinessException("名片识别识别,请重试");
        } catch (NoSuchAlgorithmException e) {
            throw new BusinessException("名片识别识别,请重试");
        }
        return null;
    }
}
