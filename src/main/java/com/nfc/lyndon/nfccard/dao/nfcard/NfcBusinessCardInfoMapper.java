package com.nfc.lyndon.nfccard.dao.nfcard;

import com.nfc.lyndon.nfccard.entity.nfccard.NfcBusinessCardInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NfcBusinessCardInfoMapper {
    int insert(NfcBusinessCardInfo record);

    int insertSelective(NfcBusinessCardInfo record);

    NfcBusinessCardInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(NfcBusinessCardInfo record);

    List<NfcBusinessCardInfo> getNfcBusinessCardInfoListByUid(Long uid);

}