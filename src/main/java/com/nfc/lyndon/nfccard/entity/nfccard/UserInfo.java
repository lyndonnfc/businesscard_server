package com.nfc.lyndon.nfccard.entity.nfccard;

import java.time.LocalDateTime;

public class UserInfo {
    private Long id;

    private String mobile;

    private String vefifyCode;

    private LocalDateTime expiryTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getVefifyCode() {
        return vefifyCode;
    }

    public void setVefifyCode(String vefifyCode) {
        this.vefifyCode = vefifyCode == null ? null : vefifyCode.trim();
    }

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }
}