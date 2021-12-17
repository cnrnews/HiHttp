package com.imooc.dialogdemo.http;

import android.content.Context;


import com.google.gson.Gson;

import java.util.Map;

public abstract class HttpCallBack<T> implements EngineCallBack {

    @Override
    public void onPreExecute(Context context, Map<String, Object> params) {
        // 添加公共请求参数
        onPreExecute();
    }
    public void onPreExecute(){
    }
    @Override
    public void onSuccess(String result) {
        Gson gson = new Gson();
        T objResult = (T) gson.fromJson(result, HttpUtils.analysisClazzInfo(this));
        onSuccess(objResult);
    }
    // 返回可以直接操作的对象
    public abstract void onSuccess(T result);
}
