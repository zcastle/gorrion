package com.ww.gorrion;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.ww.gorrion.adapter.ContactosAdapter;
import com.ww.gorrion.adapter.ContactosFacturasPendientesAdapter;
import com.ww.gorrion.common.Global;
import com.ww.gorrion.common.HttpUtil;
import com.ww.gorrion.common.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ContactoFacturasPendientesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto_facturas_pendientes);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String id = getIntent().getStringExtra("id");

        final ListView lvFacturasPendientes = (ListView) findViewById(R.id.lvFacturasPendientes);
        final View loaderView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listview_loader, null, false);

        lvFacturasPendientes.addFooterView(loaderView, null, false);

        HttpUtil.get(Global.URL_CONTACTO_FACTURAS_PENDIENTES.replace("{id}", id), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                lvFacturasPendientes.removeFooterView(loaderView);
                try {
                    JSONObject objResponse = Util.getJson(response);
                    JSONArray data = objResponse.getJSONArray("data");

                    lvFacturasPendientes.setAdapter(new ContactosFacturasPendientesAdapter(ContactoFacturasPendientesActivity.this, data));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Util.showLoginview(ContactoFacturasPendientesActivity.this);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Util.salir(this);
    }
}
