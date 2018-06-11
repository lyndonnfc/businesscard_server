package com.nfc.lyndon.nfccard.util.log;

import org.slf4j.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by caizf on 2017/12/14.
 * 日志的代理类
 * 主要用于保留原有的的日志框架体系 多增加日志的requestId
 */
public class LoggerProxy implements InvocationHandler {

    private Logger logger;

    public LoggerProxy(Logger logger){
        this.logger = logger;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        args[0] = args[0] + " requestId:"+ ThreadContext.getRequestID();
        method.invoke(logger, args);
        return null;
    }
}
