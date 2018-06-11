package com.nfc.lyndon.nfccard.util.log;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by waisam on 2017/12/27.
 */
public class ThreadContext {

    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL = new ThreadLocal<>();

    private static final String REQUEST_ID = "X-Request-ID";

    private static Object get(String attribute){
        Map<String, Object> context = THREAD_LOCAL.get();
        if(context != null)
            return context.get(attribute);
        return null;
    }

    public static <T> T get(String attribute, Class<T> clazz){
        return clazz.cast(get(attribute));
    }

    public static void set(String attribute, Object value){
        Map<String, Object> context = THREAD_LOCAL.get();
        if(context == null)
            context = new HashMap<>();
        THREAD_LOCAL.set(context);
        context.put(attribute, value);
    }

    public static void setRequestID(String requestID){
        set(ThreadContext.REQUEST_ID, requestID);
    }

    public static String getRequestID(){
        if(StringUtils.isBlank(get(ThreadContext.REQUEST_ID, String.class))){
            setRequestID("trace_id_" + System.currentTimeMillis());
        }
        return get(ThreadContext.REQUEST_ID, String.class);
    }
}
