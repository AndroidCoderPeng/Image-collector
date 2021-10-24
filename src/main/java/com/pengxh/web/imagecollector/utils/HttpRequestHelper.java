package com.pengxh.web.imagecollector.utils;

import lombok.NonNull;
import okhttp3.*;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 网络请求工具
 *
 * @author a203
 */
public class HttpRequestHelper {

    public static RequestBody createRequestBody(String value) {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), value);
    }

    public static String doPost(Request request) {
        return streamResponse(request);
    }

    public static String doGet(String url) {
        return streamResponse(new Request.Builder()
                .url(url)
                .build());
    }

    private static String streamResponse(@NonNull Request request) {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        Call call = httpClient.newCall(request);
        try {
            Response response = call.execute();
            return response.body() != null ? Objects.requireNonNull(response.body()).string() : "";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
