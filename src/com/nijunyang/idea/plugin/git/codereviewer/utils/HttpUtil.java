package com.nijunyang.idea.plugin.git.codereviewer.utils;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSON;

import java.util.Map;


/**
 * Description:
 * Created by nijunyang on 2022/1/27 17:57
 */
public final class HttpUtil {


    public static final String HTTPS_PROTOCOL = "https://";

    private HttpUtil() {
    }


    public static  HttpResponse postWithHeader(String url, Map<String, String> headers) {
        return HttpRequest.post(url)
                .headerMap(headers, true)
                .timeout(5000)
                .execute();
    }

    public static <T> HttpResponse postWithHeader(String url, T body, Map<String, String> headers) {
        return HttpRequest.post(url)
                .headerMap(headers, true)
                .body(JSON.toJSONString(body))
                .timeout(5000)
                .execute();
    }

    public static <T> HttpResponse postWithNoHeader(String url, T body) {
        return HttpRequest.post(url)
                .body(JSON.toJSONString(body))
                .timeout(5000)
                .execute();
    }

    public static <T> T getWithNoHeader(String url, Class<T> tClass) {
        HttpResponse httpResponse = HttpRequest.get(url)
                .timeout(5000)
                .execute();
        if (httpResponse.getStatus() < HttpStatus.HTTP_MULT_CHOICE) {
            return JSON.parseObject(httpResponse.body(), tClass);
        }
        throw new RuntimeException(httpResponse.getStatus() + httpResponse.body());
    }

    public static String getWithNoHeader(String url) {
        HttpResponse httpResponse = HttpRequest.get(url)
                .timeout(5000)
                .execute();
        if (httpResponse.getStatus() < HttpStatus.HTTP_MULT_CHOICE) {
            return httpResponse.body();
        }
        throw new RuntimeException(httpResponse.getStatus() + httpResponse.body());
    }

    public static <T> T getWithHeader(String url, Map<String, String> headers, Class<T> tClass) {
        HttpResponse httpResponse = HttpRequest.get(url)
                .headerMap(headers, true)
                .timeout(5000)
                .execute();
        if (httpResponse.getStatus() < HttpStatus.HTTP_MULT_CHOICE) {
            return JSON.parseObject(httpResponse.body(), tClass);
        }
        throw new RuntimeException(httpResponse.getStatus() + httpResponse.body());
    }

    public static String getWithHeader(String url, Map<String, String> headers) {
        HttpResponse httpResponse = HttpRequest.get(url)
                .headerMap(headers, true)
                .timeout(5000)
                .execute();
        if (httpResponse.getStatus() < HttpStatus.HTTP_MULT_CHOICE) {
            return httpResponse.body();
        }
        throw new RuntimeException(httpResponse.getStatus() + httpResponse.body());
    }

}
