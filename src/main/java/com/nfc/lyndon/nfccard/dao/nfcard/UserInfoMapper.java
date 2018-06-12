package com.nfc.lyndon.nfccard.dao.nfcard;

import com.nfc.lyndon.nfccard.entity.nfccard.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoMapper {
    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);

    UserInfo selectByConditions(UserInfo record);

    UserInfo selectByMobile(String mobile);
}