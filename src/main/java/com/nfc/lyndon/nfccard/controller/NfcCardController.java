package com.nfc.lyndon.nfccard.controller;

import com.alibaba.fastjson.JSONObject;
import com.nfc.lyndon.nfccard.aop.BizDescription;
import com.nfc.lyndon.nfccard.entity.base.Result;
import com.nfc.lyndon.nfccard.entity.nfccard.NfcBusinessCardInfo;
import com.nfc.lyndon.nfccard.service.nfccard.NfcCardService;
import com.nfc.lyndon.nfccard.util.log.LoggerInterface;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Created by lyndon on 2018/6/2.
 */
@RestController
@RequestMapping("nfccard")
public class NfcCardController implements LoggerInterface {

    @Resource
    private NfcCardService nfcCardService;

    @BizDescription("识别名片")
    @PostMapping(value = "uploadBusinessCard", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Result uploadBusinessCard(@RequestParam MultipartFile image) throws IOException {
        NfcBusinessCardInfo nfcBusinessCardInfo = nfcCardService.uploadNfcBusinessCard(image);
        if (nfcBusinessCardInfo != null) {
            nfcCardService.saveNfcBusinessCard(nfcBusinessCardInfo);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("nfcBusinessCardInfo",nfcBusinessCardInfo);
            return Result.success(jsonObject,"识别成功");
        }
        return Result.error("识别失败");
    }


    @BizDescription("上传图片信息")
    @PostMapping(value = "uploadLogo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Result uploadLogo(@RequestParam MultipartFile image) throws IOException {
        String url  = nfcCardService.uploadImg(image);
        if (StringUtils.isNotBlank(url)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("imgUrl",url);
            return Result.success(jsonObject,"上传成功");
        }
        return Result.error("上传失败");
    }

    @BizDescription("创建名片")
    @PostMapping(value = "createBusinessCard", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result createBusinessCard(@RequestBody NfcBusinessCardInfo businessCardInfo)  {
        int count = nfcCardService.saveNfcBusinessCard(businessCardInfo);
        if (count > 0) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",businessCardInfo.getId());
            return Result.success(jsonObject,"保存成功");
        }
        return Result.error("保存失败");
    }

    @BizDescription("修改名片")
    @PostMapping(value = "updateBusinessCard", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result updateBusinessCard(@RequestBody NfcBusinessCardInfo businessCardInfo)  {
        NfcBusinessCardInfo cardInfo = nfcCardService.getNfcBusinessCardById(businessCardInfo.getId());
        if (cardInfo == null) {
            return Result.error("当前名片不存在");
        }
        int count = nfcCardService.updateNfcBusinessCard(businessCardInfo);
        if (count > 0) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("businessCardInfo",businessCardInfo);
            return Result.success(jsonObject,"修改成功");
        }
        return Result.error("修改失败");
    }

    @BizDescription("获取名片详情")
    @GetMapping(value = "getBusinessCardById",produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getBusinessCardById(Long cardId)  {
        NfcBusinessCardInfo cardInfo = nfcCardService.getNfcBusinessCardById(cardId);
        if (cardInfo == null) {
            return Result.error("当前名片不存在");
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cardInfo",cardInfo);
        return Result.success(jsonObject,"获取成功");
    }

    @BizDescription("获取名片列表")
    @GetMapping(value = "getBusinessCardListByUid", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getBusinessCardListByUid(Long uid)  {
        List<NfcBusinessCardInfo> cardInfoList = nfcCardService.getNfcBusinessCardInfoListByUid(uid);
        if (cardInfoList.isEmpty()) {
            cardInfoList = Collections.emptyList();
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cardInfoList",cardInfoList);
        return Result.success(jsonObject,"获取成功");
    }

}
