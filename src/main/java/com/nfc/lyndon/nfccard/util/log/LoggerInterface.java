package com.nfc.lyndon.nfccard.util.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;

public interface LoggerInterface {

    Logger logger = (Logger) Proxy.newProxyInstance(Logger.class.getClassLoader(),
            new Class[]{Logger.class}, new LoggerProxy(
                    LoggerFactory.getLogger(LoggerInterface.class)));


}
