package com.nfc.lyndon.nfccard.util.common;

import com.alibaba.fastjson.JSON;
import com.nfc.lyndon.nfccard.exception.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class LocalHttpClient {

    private static Logger logger = LoggerFactory.getLogger(LocalHttpClient.class);

    private static HttpClient httpClient = HttpClientFactory.createHttpClient(100, 10);

    public static void init(int maxTotal, int maxPerRoute) {
        httpClient = HttpClientFactory.createHttpClient(maxTotal, maxPerRoute);
    }

    public static String executeGet(String url, Map<String, Object> params) {
        return executeGet(url, params, null);
    }

    public static String executeGet(String url, Map<String, Object> params, Map<String, String> headers) {
        RequestBuilder builder = RequestBuilder.get();
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(builder::setHeader);
        }

        HttpUriRequest request = builder.setUri(composeGetUrl(url, params)).build();
        return execute(request, response -> EntityUtils.toString(response.getEntity(), Consts.UTF_8));
    }

    private static String composeGetUrl(String url, Map<String, Object> map) {
        if (null == map || map.isEmpty()) {
            return url;
        }
        StringBuilder urlBuilder;
        if (url.contains("?")) {
            urlBuilder = new StringBuilder(url).append("&");
        } else {
            urlBuilder = new StringBuilder(url).append("?");
        }
        Set<Map.Entry<String, Object>> entrySet = map.entrySet();
        Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String,Object> entry = iterator.next();
            Object entryValue = entry.getValue();
            String entryValueStr;
            if (entryValue instanceof String) {
                entryValueStr = (String) entryValue;
            } else {
                entryValueStr = JSON.toJSONString(entryValue);
            }
            try {
                if (StringUtils.isNotBlank(entryValueStr)) {
                    entryValueStr = URLEncoder.encode(entryValueStr, "UTF-8");
                    urlBuilder.append(entry.getKey()).append("=").append(entryValueStr);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (iterator.hasNext()) {
                urlBuilder.append("&");
            }
        }
        return urlBuilder.toString();
    }

    public static String executePostJson(String url, Map<String, Object> params) {
        return executePostJson(url, params, null);
    }

    public static String executePostJson(String url, Map<String, Object> params, Map<String, String> headers) {
        RequestBuilder builder = RequestBuilder.post();
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(builder::setHeader);
        }
        HttpUriRequest request = builder.setUri(url)
                .setEntity(new StringEntity(JSON.toJSONString(params), ContentType.APPLICATION_JSON))
                .build();
        return execute(request, response -> EntityUtils.toString(response.getEntity(), Consts.UTF_8));
    }

    public static byte[] executePostJsonGetByteArray(String url, Map<String, Object> params) {
        return executePostJsonGetByteArray(url, params, null);
    }

    public static byte[] executePostJsonGetByteArray(String url, Map<String, Object> params, Map<String, String> headers) {
        RequestBuilder builder = RequestBuilder.post();
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(builder::setHeader);
        }
        HttpUriRequest request = builder.setUri(url)
                .setEntity(new StringEntity(JSON.toJSONString(params), ContentType.APPLICATION_JSON))
                .build();
        return execute(request, response -> {
            HttpEntity entity = response.getEntity();
            if (StringUtils.startsWithAny(entity.getContentType().getValue(),
                    new String[]{"image/jpeg", "image/gif", "image/png", "image/bmp"})) {
                return EntityUtils.toByteArray(entity);
            }
            throw new BusinessException(String.format("获取小程序二维码失败: %s", EntityUtils.toString(entity)));
        });
    }

    protected static <T> T execute(HttpUriRequest request, ResponseHandler<T> responseHandler) {
        try {
            long start = System.currentTimeMillis(); // 开始时间
            T t = httpClient.execute(request, responseHandler);
            long end = System.currentTimeMillis();
            logger.info("发送请求: " + request.getRequestLine() + " ====耗时：" + (end - start));
            logger.info("请求返回: " + t.toString());
            return t;
        } catch (Exception ex) {
            logger.error("httpclient请求异常", ex);
            if (ex instanceof BusinessException) {
                throw (BusinessException)ex;
            }
            throw new RuntimeException(ex);
        }
    }
}
