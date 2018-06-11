package com.nfc.lyndon.nfccard.service.qiniu;

import com.alibaba.druid.support.json.JSONUtils;
import com.google.gson.Gson;
import com.nfc.lyndon.nfccard.util.log.LoggerInterface;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by lyndon on 2018/6/11.
 */
@Service
public class QiniuUploader implements LoggerInterface{


    private static final String QINIU_HOST = "http://pa5ysegmb.bkt.clouddn.com/";

    @Value("${qiniu.bucket}")
    private String bucketName;

    @Value("${qiniu.accessKey}")
    private String  qnAccessKey;

    @Value("${qiniu.secretKey}")
    private String qnSecretKey;

    public String saveToQiniu(byte[] source, String fileName) {

        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(qnAccessKey, qnSecretKey);
        String upToken = auth.uploadToken(bucketName);
        logger.info("上传的文件名称：{}",fileName);
        try {
            Response response = uploadManager.put(source, fileName, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            logger.info("七牛上传结果:{}", JSONUtils.toJSONString(putRet.hash));
            if (StringUtils.isNotBlank(putRet.hash)) {
                return  QINIU_HOST.concat(fileName);
            }
            return null;
        } catch (QiniuException ex) {
            logger.error("上传失败,请稍后重试");
        }
        return null;
    }

}
