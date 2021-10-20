package com.pengxh.web.imagecollector.utils;

import lombok.NonNull;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 网络请求工具
 *
 * @author a203
 */
public class HttpRequestHelper {

//    public static String doPost(String url, String value) {
//        RequestBody requestBody = RequestBody.create(MediaType.parse(ShipConstant.CONTENT_TYPE), value);
//        Request request = new Request.Builder()
//                .addHeader(ShipConstant.HEADER_NAME, ShipConstant.KEY_VALUE)
//                .url(url)
//                .post(requestBody)
//                .build();
//        return streamResponse(request);
//    }

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
