package com.nfc.lyndon.nfccard.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nfc.lyndon.nfccard.util.log.LoggerInterface;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.*;

;

/**
 * Created by waisam on 2017/2/23.
 */
@Aspect
@Component
public class SysLogAspect implements LoggerInterface {

    ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<>();

    @Pointcut("@annotation(com.nfc.lyndon.nfccard.aop.BizDescription)")
    public void initAspect(){}


    @Before("initAspect()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        Map<String, Object> map = new HashMap<>();
        map.put("startMillss", System.currentTimeMillis());
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 记录下请求内容
        JSONObject requestLog = new JSONObject();
        String requestMethod = request.getMethod();
        String requestId = request.getHeader("X-Request-ID");
        String bizName = getControllerMethodDescription(joinPoint);

        requestLog.put("X-Request-ID",requestId);
        requestLog.put("requestUrl",request.getRequestURL().toString());
        requestLog.put("requestMethod",request.getMethod());
        requestLog.put("requestIp",request.getRemoteAddr());
        requestLog.put("bizName",bizName);

        String requestJsonString = null;
        if ("GET".equalsIgnoreCase(requestMethod)) {
            String queryString = request.getQueryString();
            if (StringUtils.isNotBlank(queryString)) {
                requestJsonString = new String(queryString.getBytes("iso-8859-1"), "utf-8").replaceAll("%22", "\"");
                // 将string转成json格式
                requestJsonString = this.conventGetStringToJson(requestJsonString);
            }


        } else if ("POST".equalsIgnoreCase(requestMethod)) {
            List paramList = Arrays.asList(joinPoint.getArgs());
            if (CollectionUtils.isNotEmpty(paramList)) {
                Iterator it = paramList.iterator();
                StringBuilder sbuilder = new StringBuilder();
                while (it.hasNext()) {
                    Object obj = it.next();
                    if (obj instanceof ServletRequest || obj instanceof ServletResponse || obj instanceof HttpSession) {
                        continue;
                    }
                    if (obj instanceof String) {
                        sbuilder.append((String) obj);
                    } else {
                        sbuilder.append(JSON.toJSONString(obj));
                    }
                    if (it.hasNext()) {
                        sbuilder.append(",");
                    }
                }
                requestJsonString = sbuilder.toString();
                if (StringUtils.isNotBlank(requestJsonString)) {
                    String lastChar = requestJsonString.substring(requestJsonString.length()-1);
                    if (",".equals(lastChar)) {
                        requestJsonString = requestJsonString.substring(0,requestJsonString.length()-1);
                    }
                }
            }
        }
        requestLog.put("requestParams",requestJsonString);
        map.put("requestLog", requestLog);
        threadLocal.set(map);
    }

    @AfterReturning(pointcut = "initAspect()", returning = "ret")
    public void doAfterReturning(Object ret) {
        Map<String, Object> map = threadLocal.get();
        Object _requestLog = map.get("requestLog");
        JSONObject requestLog = null;
        if (_requestLog instanceof JSONObject) {
            requestLog = (JSONObject)_requestLog;
        }
        // 处理完请求，返回内容
        Long timeConsuming = System.currentTimeMillis() - (Long)map.get("startMillss");
        if (requestLog != null) {
            requestLog.put("timeConsuming",timeConsuming);
            requestLog.put("responseResult",JSON.toJSONString(ret));
            logger.info(requestLog.toJSONString());
        }
    }

    @AfterThrowing(pointcut = "initAspect()", throwing = "e")
    public  void doAfterThrowing(JoinPoint joinPoint, Throwable e) throws Exception {
        Map<String, Object> map = threadLocal.get();
        Object _requestLog = map.get("requestLog");
        JSONObject requestLog = null;
        if (_requestLog instanceof JSONObject) {
            requestLog = (JSONObject)_requestLog;
        }
        Long timeConsuming = System.currentTimeMillis() - (Long)map.get("startMillss");
        if (requestLog != null) {
            requestLog.put("timeConsuming",timeConsuming);
            logger.error(requestLog.toJSONString(),e);
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param joinPoint 切点
     * @return 方法描述
     * @throws Exception
     */
    public static String getControllerMethodDescription(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    description = method.getAnnotation(BizDescription.class).value();
                    break;
                }
            }
        }
        return description;
    }


    /**
     * 将get参数转为标准的json字符串
     * @param requestSting
     * @return
     * @throws UnsupportedEncodingException
     */
    public String conventGetStringToJson(String requestSting) throws UnsupportedEncodingException {
        JSONObject jsonObject = new JSONObject();
        if (StringUtils.isNotBlank(requestSting)) {
            String [] params = requestSting.split("&");
            for (int i=0; i <params.length; i++) {
                String [] strs = params[i].split("=");
                if (strs.length>1) {
                    jsonObject.put(strs[0], URLDecoder.decode(strs[1],"utf-8"));
                }
            }
            return JSON.toJSONString(jsonObject);
        }
        return null;
    }
}