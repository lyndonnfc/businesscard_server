package com.nfc.lyndon.nfccard.service.upy;

import com.nfc.lyndon.nfccard.entity.base.Result;
import com.nfc.lyndon.nfccard.util.log.LoggerInterface;
import main.java.com.UpYun;
import main.java.com.upyun.UpException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileUploader implements LoggerInterface {

    private static final String UPYUN_HOST = "http://innmall-hotel.b0.upaiyun.com";

    @Value("${upaiyun.bucketName}")
    private String bucketName;

    @Value("${upaiyun.operatorName}")
    private String operatorName;

    @Value("${upaiyun.operatorPwd}")
    private String operatorPwd;

    public String upload(String location, File file) throws IOException, UpException {
        UpYun upyun = new UpYun(bucketName, operatorName, operatorPwd);
        upyun.setContentMD5(UpYun.md5(file));
        long startTime = System.currentTimeMillis();
        boolean isUploadSuccess = upyun.writeFile(location, file, true);
        logger.info("调用upYun的上传文件服务，耗时："+(System.currentTimeMillis()-startTime) + "ms，调用结果："+isUploadSuccess);
        if (isUploadSuccess) {
            return UPYUN_HOST + location;
        }
        return null;
    }

    public String saveToUpy(String path, byte[] source){
        long startTime = System.currentTimeMillis();
        File imageFile = this.getImageFile(source);
        logger.info("保存至又拍云，转换为File耗时："+(System.currentTimeMillis() - startTime)+ "ms");
        return this.saveToUpy(path, imageFile);
    }

    public String saveToUpy(String path, byte[] source, String fileName) {
        long startTime = System.currentTimeMillis();
        File imageFile = this.getImageFile(source);
        logger.info("保存至又拍云，转换为File耗时：" + (System.currentTimeMillis() - startTime) + "ms");
        UPanYunLocation location = getLocation(path, fileName);
        try {
            Result result=this.saveFileToImg(imageFile,location);
            return (String)result.getResult();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UpException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String saveToUpy(String path, File file) {
        UPanYunLocation location = getLocation(path);
        try {
            Result result=this.saveFileToImg(file,location);
            return (String)result.getResult();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UpException e) {
            e.printStackTrace();
        }
        return "";
    }

    private File getImageFile(byte[] source) {
        String name = UUID.randomUUID().toString();
        File file = new File(name);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(source);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 上传文件到又拍云.
     * @param file 待上传的文件
     * @param location 设置又拍云的路径
     * @return 上传结果
     */
    public Result saveFileToImg(final File file, UPanYunLocation location) throws IOException, UpException {
        final String remotePath = location.fullPath();
        String imgUrl = this.upload(remotePath, file);
        file.delete();
        return StringUtils.isBlank(imgUrl) ?
                Result.error("上传图片失败") : Result.success(imgUrl);
    }

    public UPanYunLocation getLocation(String path) {
        String name = UUID.randomUUID().toString();
        return getLocation(path, name);
    }

    public UPanYunLocation getLocation(String path,String fileName) {
        UPanYunLocation location = new UPanYunLocation() {
            @Override
            public String filePath() {
                return path;
            }

            @Override
            public String fileName() {
                return fileName;
            }

            @Override
            public String fileNameExtension() {
                return "png";
            }
        };
        return location;
    }

    public boolean delet(String url) {
        url = url.substring(url.lastIndexOf("/")+1);
        url =  "/toop_member_applet/role_gift/wxcode/" + url;
        UpYun upyun = new UpYun(bucketName, operatorName, operatorPwd);
        return upyun.deleteFile(url);
    }

    public interface UPanYunLocation {

        String filePath();

        String fileName();

        String fileNameExtension();

        default String fullPath() {
            String path = filePath();
            int slash = path.lastIndexOf('/');
            if (slash == path.length() - 1) {
                return path + fileName() + '.'+ fileNameExtension();
            }
            return path + '/' + fileName() + '.'+ fileNameExtension();
        }
    }
}
