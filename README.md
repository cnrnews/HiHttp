# HiHttp

#### 介绍
封装OkHttp请求接口，客户端链式调用

#### 软件架构
软件架构说明

 

#### 使用说明


```
 private void testHttp(){
        HttpUtils.with(this)
                .url("https://www.wanandroid.com/article/list/1/json")
                .get()
                .execute(new HttpCallBack<DiscoverListResult>() {
                    @Override
                    public void onError(Exception e) {
                        Log.e("Callback", e.toString());
                    }

                    @Override
                    public void onSuccess(DiscoverListResult result) {
                        Log.e("onSuccess", result.data.datas.size() + "");
                    }
                });
    }
```


 