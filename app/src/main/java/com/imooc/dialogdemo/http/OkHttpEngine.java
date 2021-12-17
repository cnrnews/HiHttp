package com.imooc.dialogdemo.http;

import android.content.Context;


import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpEngine implements IHttpEngine {

    private OkHttpClient mOkHttpClient = new OkHttpClient();

    @Override
    public void get(Context context, String url, Map<String, Object> params, EngineCallBack callBack) {

        Request.Builder requestBuilder = new Request.Builder().url(url).tag(context);
        requestBuilder.method("GET",null);
        Request request = requestBuilder.build();
        mOkHttpClient.newCall(request).enqueue(new Callback(){
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                executeSuccess(callBack,response.body().string());
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
               executeError(callBack,e);
            }
        });

    }

    @Override
    public void post(Context context, String url, Map<String, Object> params, EngineCallBack callBack) {
        RequestBody requestBody = appendBody(params);
        Request request = new Request.Builder()
                .url(url)
                .tag(context)
                .post(requestBody)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback(){

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                executeSuccess(callBack,response.body().string());
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                executeError(callBack,e);
            }
        });

    }
    /**
     * 执行成功的回调
     * @param callBack
     * @param result
     */
    private void executeSuccess(EngineCallBack callBack, String result) {
        HttpUtils.handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onSuccess(result);
            }
        });
    }
    /**
     * 执行失败的回调
     * @param callBack
     * @param e
     */
    private void executeError(EngineCallBack callBack, IOException e) {
        HttpUtils.handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onError(e);
            }
        });
    }

    private String guessMineType(String path){
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null){
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    /**
     * 组装 post 请求参数 body
     * @param params
     * @return
     */
    protected RequestBody appendBody(Map<String,Object> params){
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        addParams(builder,params);
        return builder.build();
    }

    /**
     * 添加参数
     * @param builder
     * @param params
     */
    private void addParams(MultipartBody.Builder builder, Map<String, Object> params) {
        if (params!=null && !params.isEmpty()){
            for (String key : params.keySet()) {
                builder.addFormDataPart(key,params.get(key)+"");
                Object value = params.get(key);
                if (value instanceof File){
                    File file = (File) value;
                    builder.addFormDataPart(key,file.getName(),RequestBody.create(
                            MediaType.parse(guessMineType(file.getAbsolutePath())),file));
                }else if(value instanceof List){
                    try{
                        List<File> listFiles = (List<File>) value;
                        for (int i = 0; i < listFiles.size(); i++) {
                            File file = listFiles.get(i);
                            builder.addFormDataPart(key+i,file.getName(),RequestBody.create(
                                    MediaType.parse(guessMineType(file.getAbsolutePath())),file));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    builder.addFormDataPart(key,value+"");
                }
            }
        }
    }

}
