package com.imooc.dialogdemo.http;

import android.content.Context;

import java.util.Map;

/**
 * 通用网络接口
 */
public interface IHttpEngine {
    /**
     * get
     * @param url
     * @param params
     * @param callBack
     */
    void get(Context context, String url, Map<String,Object> params, EngineCallBack callBack);

    /**
     * post
     * @param url
     * @param params
     * @param callBack
     */
    void post(Context context,String url,Map<String,Object> params,EngineCallBack callBack);
}
