package com.ww.gorrion;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.ww.gorrion.adapter.GuiasResumenAdapter;
import com.ww.gorrion.common.Global;
import com.ww.gorrion.common.HttpUtil;
import com.ww.gorrion.common.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class GuiaResumenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guia_resumen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ListView lvGuiasResumen = (ListView) findViewById(R.id.lvGuiasResumen);
        final View footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listview_loader, null, false);
        lvGuiasResumen.addFooterView(footerView, null, false);

        String json = getIntent().getStringExtra("json");

        try {
            JSONObject data = new JSONObject(json);
            getSupportActionBar().setTitle(data.getString("contacto").concat("[").concat(data.getString("numero")).concat("][").concat(data.getString("fecha")).concat("]"));

            HttpUtil.get(Global.URL_GUIA_RESUMEN.replace("{guiaId}", data.getString("id")), new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    try {
                        JSONObject objResponse = Util.getJson(response);
                        JSONArray data = objResponse.getJSONArray("data");
                        lvGuiasResumen.setAdapter(new GuiasResumenAdapter(GuiaResumenActivity.this, data));
                        lvGuiasResumen.removeFooterView(footerView);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                    try {
                        String data = Util.getString(errorResponse);
                        if(data.equals("Unauthorized Access")) {
                            Util.showLoginview(GuiaResumenActivity.this);
                        }else{
                            Log.e("onFailure", data.toString());
                        }
                    } catch (JSONException e1) {
                        Log.e("JSONException", e1.getMessage());
                    }
                }
            });
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Util.salir(this);
        finish();
    }
}
