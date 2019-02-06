package com.xht97.whulibraryseat.util;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtil {

    private OkHttpClient client;
    private static final int TIMEOUT = 1000 * 30;
    private static final MediaType JSON = MediaType
            .parse("application/json; charset=utf-8");
    private Handler handler = new Handler(Looper.getMainLooper());
    // 采用单例模式
    private static HttpUtil mHttpUtils;

    private HttpUtil() {
        client = new OkHttpClient();
        client.newBuilder().connectTimeout(TIMEOUT, TimeUnit.SECONDS).
                writeTimeout(TIMEOUT,TimeUnit.SECONDS).readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    public static HttpUtil getInstance() {
        if(mHttpUtils == null) {
            synchronized (HttpUtil.class) {
                mHttpUtils = new HttpUtil();
            }
        }
        return mHttpUtils;
    }

    // 不带Token的直接GET请求
    public void get(String url, final HttpCallBack callBack){
        Request request = new Request.Builder()
                .removeHeader("User-Agent")
                .addHeader("User-Agent", "doSingle/11 CFNetwork/893.14.2 Darwin/17.3.0")
                .url(url).build();
        onStart(callBack);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onError(callBack, e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    onSuccess(callBack, response.body().string());
                } else {
                    onError(callBack, response.message());
                }
            }
        });
    }

    // 带Token的GET请求
    public void getWithToken(String token, String url, final HttpCallBack callBack) {
        Request request = new Request.Builder()
                .removeHeader("User-Agent")
                .addHeader("User-Agent", "doSingle/11 CFNetwork/893.14.2 Darwin/17.3.0")
                .addHeader("token", token)
                .url(url).build();
        onStart(callBack);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onError(callBack, e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    onSuccess(callBack, response.body().string());
                } else {
                    onError(callBack, response.message());
                }
            }
        });
    }

    // POST请求，请求体为键值对应的Map
    public void postWithToken(String url, String token, Map<String,String> map, final HttpCallBack callBack){
        FormBody.Builder builder = new FormBody.Builder();

        // 遍历map
        if(map != null){
            for (Map.Entry<String,String> entry : map.entrySet()){
                builder.add(entry.getKey(), entry.getValue().toString());
            }
        }
        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .removeHeader("User-Agent")
                .addHeader("User-Agent", "doSingle/11 CFNetwork/893.14.2 Darwin/17.3.0")
                .addHeader("token", token)
                .url(url).post(body).build();
        onStart(callBack);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onError(callBack, e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    onSuccess(callBack, response.body().string());
                }else{
                    onError(callBack, response.message());
                }
            }
        });
    }


    private void onStart(HttpCallBack callBack){
        if(callBack != null){
            callBack.onStart();
        }
    }

    private void onSuccess(final HttpCallBack callBack,final String data){
        if(callBack != null){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //在主线程操作，取到的data即为返回内容
                    callBack.onSuccess(data);
                }
            });
        }
    }

    private void onError(final HttpCallBack callBack, final String msg){
        if(callBack != null){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    callBack.onError(msg);
                }
            });
        }
    }

    // HttpCallBack的抽象接口
    public static abstract class HttpCallBack{
        //开始
        public void onStart(){
        }
        //成功回调
        public abstract void onSuccess(String data);
        //失败
        public void onError(String msg){
            Log.d("HttpUtil", msg);
        }
    }

}
