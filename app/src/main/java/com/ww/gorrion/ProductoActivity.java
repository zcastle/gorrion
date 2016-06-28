package com.ww.gorrion;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.ww.gorrion.adapter.ProductosAdapter;
import com.ww.gorrion.common.Global;
import com.ww.gorrion.common.HttpUtil;
import com.ww.gorrion.common.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ProductoActivity extends AppCompatActivity implements View.OnClickListener, ListView.OnItemClickListener {

    private EditText txtSerie;
    private Switch swSerieExacta;
    private ProgressDialog mDialog;
    private ListView lvProductoResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) {
            overridePendingTransition(R.animator.anim_slide_in_left, R.animator.anim_slide_out_left);
        }

        txtSerie = (EditText) findViewById(R.id.txtSerie);
        txtSerie.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                OnClickBuscar();
                return false;
            }
        });
        swSerieExacta = (Switch) findViewById(R.id.swSerieExacta);
        Button btnBuscar = (Button) findViewById(R.id.btnBuscar);
        btnBuscar.setOnClickListener(this);
        lvProductoResultado = (ListView) findViewById(R.id.lvProductoResultado);
        lvProductoResultado.setOnItemClickListener(this);
    }

    private void OnClickBuscar() {
        String serie = txtSerie.getText().toString().trim();
        String serieExacta = "false";
        if(swSerieExacta.isChecked()){
            serieExacta = "true";
        }
        if(serie.isEmpty()) {
            Toast.makeText(this, "Debe imgresar una serie", Toast.LENGTH_SHORT).show();
            lvProductoResultado.setAdapter(null);
        }else{
            Util.hideKeyboard(txtSerie);
            mDialog = new ProgressDialog(this);
            mDialog.setMessage("Buscando...");
            if(!mDialog.isShowing()){
                mDialog.show();
            }
            HttpUtil.get(Global.URL_PRODUCTO_LISTAR.replace("{serie}", serie).replace("{serieExacta}", serieExacta),  new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    if (mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    try {
                        JSONObject data = Util.getJson(response);
                        addResult(data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                    if (mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    try {
                        String data = Util.getString(errorResponse);
                        if(data.equals("Unauthorized Access")) {
                            Util.showLoginview(ProductoActivity.this);
                        }else{
                            Log.e("onFailure", data.toString());
                        }
                    } catch (JSONException e1) {
                        Log.e("JSONException", e1.getMessage());
                    }
                }
            });
        }
    }

    private void addResult(JSONObject response) throws JSONException {
        JSONArray data = response.getJSONArray("data");
        lvProductoResultado.setAdapter(null);
        if(data.length()==0) {
            Toast.makeText(this, response.getString("message"), Toast.LENGTH_LONG).show();
        }else if(data.length()==1){
            startProductoActivity(data.getJSONObject(0).getInt("id"));
        }else if(data.length()>1) {
            lvProductoResultado.setAdapter(new ProductosAdapter(this, data));
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnBuscar){
            OnClickBuscar();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object json = parent.getItemAtPosition(position);
        try {
            JSONObject data = new JSONObject(json.toString());
            startProductoActivity(data.getInt("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void startProductoActivity(int id){
        Intent intent = new Intent(this, ProductoResultadoActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.animator.anim_slide_in_right, R.animator.anim_slide_out_right);
        finish();
    }
}
