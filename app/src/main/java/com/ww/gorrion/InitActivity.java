package com.ww.gorrion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class InitActivity extends AppCompatActivity {

    private SessionManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        manager = new SessionManager();
        String status = manager.getPreferences(InitActivity.this, "status");
        if(status.equals("1")){
            final String usuario = manager.getPreferences(InitActivity.this, "usuario");
            final String password = manager.getPreferences(InitActivity.this, "password");

            RequestParams params = new RequestParams();
            params.put("txt_usuario", usuario);
            params.put("txt_contrasena", password);

            HttpUtil.post(Global.URL_LOGIN, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        String data = response.getString("data");
                        if(data.equals("443")){
                            loadLoginView();
                        }else if(data.equals("true")){
                            manager.setPreferences(InitActivity.this, "status", "1");
                            Intent intent = new Intent(InitActivity.this, MainActivity.class);
                            //intent.putExtra(Global.LABEL_USUARIO, "USUARIO LOGEADO");
                            startActivity(intent);
                            overridePendingTransition(R.animator.enter, R.animator.exit);
                            finish();
                        }
                    } catch (JSONException e) {
                        loadLoginView();
                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {}
            });
        }else{
            loadLoginView();
        }
    }

    private void loadLoginView(){
        Intent intent = new Intent(InitActivity.this, LoginActivity.class);
        //intent.putExtra(Global.LABEL_USUARIO, "USUARIO LOGEADO");
        startActivity(intent);
        overridePendingTransition(R.animator.enter, R.animator.exit);
        finish();
    }
}
