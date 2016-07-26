package com.ww.gorrion;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.ww.gorrion.adapter.ContactosAdapter;
import com.ww.gorrion.common.Global;
import com.ww.gorrion.common.HttpUtil;
import com.ww.gorrion.common.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ContactoResultadoActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog mDialog;
    private TextView lblAlias;
    private TextView lblRuc;
    private TextView lblRazon;
    private TextView lblDireccion;
    private TextView lblDistrito;
    private TextView lblProvincia;
    private TextView lblDepartamento;
    private Long lat;
    private Long lng;
    private Button btnMapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto_resultado);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lblAlias = (TextView) findViewById(R.id.lblAlias);
        lblRuc = (TextView) findViewById(R.id.lblRuc);
        lblRazon = (TextView) findViewById(R.id.lblRazon);
        lblDireccion = (TextView) findViewById(R.id.lblDireccion);
        lblDistrito = (TextView) findViewById(R.id.lblDistrito);
        lblProvincia = (TextView) findViewById(R.id.lblProvincia);
        lblDepartamento = (TextView) findViewById(R.id.lblDepartamento);
        btnMapa = (Button) findViewById(R.id.btnMapa);
        btnMapa.setOnClickListener(this);

        String json = getIntent().getStringExtra("data");

        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Cargando");
        if(!mDialog.isShowing()){
            mDialog.show();
        }

        try {
            JSONObject data = new JSONObject(json);
            String id = data.getString("id");
            HttpUtil.get(Global.URL_CONTACTO.replace("{id}", id), new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    try {
                        JSONObject objResponse = Util.getJson(response);
                        JSONObject data = objResponse.getJSONObject("data");
                        cargarDatos(data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                    Util.showLoginview(ContactoResultadoActivity.this);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void cargarDatos(JSONObject data) throws JSONException {
        lblAlias.setText(data.getString("alias"));
        lblRuc.setText(data.getString("ruc"));
        lblRazon.setText(data.getString("razon"));
        lblDireccion.setText(data.getString("direccion"));
        lblDistrito.setText(data.getString("distrito"));
        lblProvincia.setText(data.getString("provincia"));
        lblDepartamento.setText(data.getString("departamento"));
        JSONObject location = data.getJSONObject("location");
        lat = location.getLong("lat");
        lng = location.getLong("lng");

        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {

    }
}
