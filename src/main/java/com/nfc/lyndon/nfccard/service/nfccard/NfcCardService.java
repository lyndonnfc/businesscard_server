package com.nfc.lyndon.nfccard.service.nfccard;

import com.alibaba.fastjson.JSONArray;
import com.nfc.lyndon.nfccard.dao.nfcard.NfcBusinessCardInfoMapper;
import com.nfc.lyndon.nfccard.entity.nfccard.NfcBusinessCardInfo;
import com.nfc.lyndon.nfccard.entity.nfccard.YoutuBusinessCard;
import com.nfc.lyndon.nfccard.exception.BusinessException;
import com.nfc.lyndon.nfccard.service.qiniu.QiniuUploader;
import com.nfc.lyndon.nfccard.service.youtu.YoutuService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * Created by lyndon on 2018/6/2.
 */
@Service
public class NfcCardService {

    @Resource
    private YoutuService youtuService;

    @Resource
    private QiniuUploader qiniuUploader;

    @Resource
    private NfcBusinessCardInfoMapper businessCardInfoMapper;

    public NfcBusinessCardInfo uploadNfcBusinessCard(MultipartFile image) {
        StringBuilder strBuild = new StringBuilder("card");
        strBuild.append(String.valueOf(System.currentTimeMillis()/1000))
                .append("_")
                .append(StringUtils.substringBefore(image.getOriginalFilename(), "."));
        String imgUrl = null;
        try {
            imgUrl = qiniuUploader.saveToQiniu(image.getBytes(), strBuild.toString());
        } catch (IOException e) {
           throw new BusinessException("名片上传失败，请稍后重试");
        }
        if (StringUtils.isNotBlank(imgUrl)  ) {
            return this.getNfcBusinessCardInfo(imgUrl);
        }
        return null;
    }


    public String uploadImg(MultipartFile image) {
        StringBuilder strBuild = new StringBuilder("logo");
        strBuild.append(String.valueOf(System.currentTimeMillis()/1000))
                .append("_")
                .append(StringUtils.substringBefore(image.getOriginalFilename(), "."));
        String imgUrl = null;
        try {
            imgUrl = qiniuUploader.saveToQiniu(image.getBytes(), strBuild.toString());
        } catch (IOException e) {
            throw new BusinessException("名片上传失败，请稍后重试");
        }
        return imgUrl;
    }

    public int saveNfcBusinessCard(NfcBusinessCardInfo businessCardInfo) {
        return businessCardInfoMapper.insertSelective(businessCardInfo);
    }

    public int updateNfcBusinessCard(NfcBusinessCardInfo businessCardInfo) {
        return businessCardInfoMapper.updateByPrimaryKeySelective(businessCardInfo);
    }

    public List<NfcBusinessCardInfo> getNfcBusinessCardInfoListByUid(Long uid) {
        return businessCardInfoMapper.getNfcBusinessCardInfoListByUid(uid);
    }

    public NfcBusinessCardInfo getNfcBusinessCardById(Long id) {
        return businessCardInfoMapper.selectByPrimaryKey(id);
    }

    private NfcBusinessCardInfo getNfcBusinessCardInfo(String imgUrl){
        String str = youtuService.getRecognitionItemsStr(imgUrl);
        if (StringUtils.isNotBlank(str)) {
            List<YoutuBusinessCard> youtuBusinessCardList = JSONArray.parseArray(str, YoutuBusinessCard.class);
            return changeYoutuBusinessCard(youtuBusinessCardList);
        }
        return null;
    }

    /**
     * 姓名、英文姓名、职位、英文职位、部门、英文部门、公司、英文公司、地址、英文地址、邮编、邮箱、网址、手机、电话、传真、QQ、MSN、微信、微博、公司账号、logo、其他。
     * @param cardList
     * @return
     */
    private NfcBusinessCardInfo changeYoutuBusinessCard( List<YoutuBusinessCard>  cardList) {
        NfcBusinessCardInfo nfcBusinessCardInfo = new NfcBusinessCardInfo();
        cardList.forEach(youtuBusinessCard -> {
            switch (youtuBusinessCard.getItem()){
                case "姓名":
                    nfcBusinessCardInfo.setRealName(youtuBusinessCard.getItemString());
                    break;
                case "英文姓名":
                    nfcBusinessCardInfo.setEnglishRealName(youtuBusinessCard.getItemString());
                    break;
                case "职位":
                    nfcBusinessCardInfo.setPosition(youtuBusinessCard.getItemString());
                    break;
                case "英文职位":
                    nfcBusinessCardInfo.setEnglishPostion(youtuBusinessCard.getItemString());
                    break;
                case "公司":
                    nfcBusinessCardInfo.setCompanyName(youtuBusinessCard.getItemString());
                    break;
                case "英文公司":
                    nfcBusinessCardInfo.setEnglishCompanyName(youtuBusinessCard.getItemString());
                    break;
                case "部门":
                    nfcBusinessCardInfo.setDepartment(youtuBusinessCard.getItemString());
                    break;
                case "英文部门":
                    nfcBusinessCardInfo.setEnglishDepartment(youtuBusinessCard.getItemString());
                    break;
                case "地址":
                    nfcBusinessCardInfo.setAddress(youtuBusinessCard.getItemString());
                    break;
                case "英文地址":
                    nfcBusinessCardInfo.setEnglishAddress(youtuBusinessCard.getItemString());
                    break;
                case "邮箱":
                    nfcBusinessCardInfo.setEmail(youtuBusinessCard.getItemString());
                    break;
                case "手机":
                    nfcBusinessCardInfo.setPhone(youtuBusinessCard.getItemString());
                case "电话":
                    nfcBusinessCardInfo.setMobile(youtuBusinessCard.getItemString());
                    break;
                case "邮编":
                    nfcBusinessCardInfo.setPostcode(youtuBusinessCard.getItemString());
                    break;
                case "网址":
                    nfcBusinessCardInfo.setWebUrl(youtuBusinessCard.getItemString());
                    break;
                case "传真":
                    nfcBusinessCardInfo.setFax(youtuBusinessCard.getItemString());
                case "QQ":
                    nfcBusinessCardInfo.setQqAccount(youtuBusinessCard.getItemString());
                    break;
                case "MSN":
                    nfcBusinessCardInfo.setMsnAccount(youtuBusinessCard.getItemString());
                    break;
                case "微信":
                    nfcBusinessCardInfo.setWechatAccount(youtuBusinessCard.getItemString());
                    break;
                case "微博":
                    nfcBusinessCardInfo.setWeiboAccount(youtuBusinessCard.getItemString());
                    break;
                case "公司账号":
                    nfcBusinessCardInfo.setCompanyAccount(youtuBusinessCard.getItemString());
                case "logo":
                    nfcBusinessCardInfo.setLogo(youtuBusinessCard.getItemString());
                    break;
                case "其他":
                    nfcBusinessCardInfo.setOtherMsg(youtuBusinessCard.getItemString());
                    break;
            }

        });
        return nfcBusinessCardInfo;
    }
}
