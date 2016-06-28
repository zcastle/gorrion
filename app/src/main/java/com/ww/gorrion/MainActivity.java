package com.ww.gorrion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.ww.gorrion.common.Global;
import com.ww.gorrion.common.HttpUtil;
import com.ww.gorrion.common.SessionManager;
import com.ww.gorrion.common.Util;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SessionManager manager = new SessionManager();
        String token = manager.getToken(this);
        if(token.isEmpty()){
            Util.showLoginview(this);
        }else{
            HttpUtil.get(Global.URL_LOGIN, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    Intent intent = new Intent(MainActivity.this, LauncherActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                    manager.setToken(MainActivity.this, "");
                    Util.showLoginview(MainActivity.this);
                }
            });
        }
    }
}
