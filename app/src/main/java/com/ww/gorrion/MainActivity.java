package com.ww.gorrion;

import android.content.Context;
import android.content.Intent;
//import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.ww.gorrion.common.Global;
import com.ww.gorrion.common.HttpUtil;
import com.ww.gorrion.common.SessionManager;
import com.ww.gorrion.common.Util;

import java.util.Timer;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        /*RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
        rl.setVisibility(View.GONE);
        int mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);*/
        //

        //
        /*rl.setAlpha(0f);
        rl.setVisibility(View.VISIBLE);
        rl.animate()
            .alpha(1f)
            .setDuration(mShortAnimationDuration)
            .setListener(null);*/
        //
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
                    Util.entrar(MainActivity.this);
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

    /*@Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }*/
}
