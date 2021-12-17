package com.imooc.dialogdemo.http;

import android.content.Context;
import android.os.Handler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils{

    private Context mContext;
    private String mUrl;
    // 请求方式
    private int mType = GET_TYPE;
    private static final int POST_TYPE = 0x0011;
    private static final int GET_TYPE = 0x0012;

    private Map<String,Object> mParams;

    // 默认网络引擎
    private static IHttpEngine mHttpEngine = new OkHttpEngine();

    public static Handler handler = new Handler();

    public HttpUtils(Context context) {
        this.mContext = context;
        mParams = new HashMap<>();
    }

    public static HttpUtils with(Context context){
        return new HttpUtils(context);
    }
    public HttpUtils url(String url){
        this.mUrl = url;
        return this;
    }
    public HttpUtils get(){
        mType = GET_TYPE;
        return this;
    }
    public HttpUtils post(){
        mType = POST_TYPE;
        return this;
    }

    public static void init(IHttpEngine httpEngine){
        mHttpEngine = httpEngine;
    }

    /**
     * 添加请求参数
     * @param key
     * @param value
     * @return
     */
    public HttpUtils addParam(String key,String value){
        mParams.put(key,value);
        return this;
    }
    public HttpUtils addParams(Map<String,Object> params){
        mParams.putAll(params);
        return this;
    }
    /**
     * 执行请求，回调
     */
    public void execute(EngineCallBack callBack){

        callBack.onPreExecute(mContext,mParams);

        if (callBack==null){
            callBack = EngineCallBack.DEFAULT_CALL_BACK;
        }

        // 判断执行方法
        if (mType == POST_TYPE){
            post(mUrl,mParams,callBack);
        }
        if (mType == GET_TYPE){
            get(mUrl,mParams,callBack);
        }
    }

    /**
     * 执行请求，不需要回调
     */
    public void execute(){
        execute(null);
    }
    /**
     * 切换引擎
     * @param httpEngine
     */
    public void exchangeEngine(IHttpEngine httpEngine){
        mHttpEngine = httpEngine;
    }
    public void get(String url, Map<String, Object> params, EngineCallBack callBack) {
        mHttpEngine.get(mContext,url, params, callBack);
    }

    public void post(String url, Map<String, Object> params, EngineCallBack callBack) {
        mHttpEngine.post(mContext,url, params, callBack);
    }

    /**
     * 解析一个类上面的class信息
     * @param object
     * @return
     */
    public static Class<?> analysisClazzInfo(Object object){
        Type genericSuperclass = object.getClass().getGenericSuperclass();
        Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
        return (Class<?>) actualTypeArguments[0];
    }
}
