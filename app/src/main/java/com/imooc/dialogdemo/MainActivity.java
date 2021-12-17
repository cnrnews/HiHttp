package com.imooc.dialogdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.imooc.dialogdemo.http.HttpCallBack;
import com.imooc.dialogdemo.http.HttpUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testHttp();
    }
    private void testHttp(){
        HttpUtils.with(this)
                .url("https://www.wanandroid.com/article/list/1/json")
//                .addParam("username","jake112")
//                .addParam("password","159346")
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
}