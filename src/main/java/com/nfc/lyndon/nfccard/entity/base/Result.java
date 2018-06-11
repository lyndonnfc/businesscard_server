package com.nfc.lyndon.nfccard.entity.base;

public class Result<T> {

    private String msg;
    private Integer stat;

    private static final int successStat = 1;


    public static <T> Result<T> success(T result) {
        return build(result, "success", successStat);
    }

    public static <T> Result<T> success(T result, String msg) {
        return build(result, msg, 1);
    }

    public static <T> Result reAuthenticate() {
        return build("", "用户不存在,重新认证", 13);
    }

    public static <T> Result<T> error(T result, String msg, Integer stat) {
        return build(result, msg, stat);
    }

    public static <T> Result<T> error(String msg) {
        return build(null, msg, 2);
    }

    public static <T> Result<T> build(T result, String msg, Integer stat) {
        Result<T> b = new Result<T>();
        b.setStat(stat);
        b.setMsg(msg);
        b.setResult(result);
        return b;
    }

    private T result;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getStat() {
        return stat;
    }

    public void setStat(Integer stat) {
        this.stat = stat;
    }

}
