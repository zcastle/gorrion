package com.ww.gorrion;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.ww.gorrion.common.Global;
import com.ww.gorrion.common.HttpUtil;
import com.ww.gorrion.common.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ProductoResultadoActivity extends AppCompatActivity {

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto_resultado);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int id = getIntent().getIntExtra("id", 0);
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Buscando...");
        if(!mDialog.isShowing()){
            mDialog.show();
        }
        HttpUtil.get(Global.URL_PRODUCTO.replace("{id}", id+""),  new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                try {
                    JSONObject data = Util.getJson(response);
                    //Log.e("URL_PRODUCTO", data.toString());
                    loadView(data.getJSONArray("data").getJSONObject(0));
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
                        Util.showLoginview(ProductoResultadoActivity.this);
                    }else{
                        Log.e("onFailure", data.toString());
                    }
                } catch (JSONException e1) {
                    Log.e("JSONException", e1.getMessage());
                }
            }
        });

    }

    private void loadView(JSONObject row) throws JSONException {
        TextView lblSerie = (TextView) findViewById(R.id.lblSerie);
        TextView lblFamilia = (TextView) findViewById(R.id.lblFamilia);
        TextView lblMarca = (TextView) findViewById(R.id.lblMarca);
        TextView lblModelo = (TextView) findViewById(R.id.lblModelo);
        TextView lblTipo = (TextView) findViewById(R.id.lblTipo);
        TextView lblPadre = (TextView) findViewById(R.id.lblPadre);
        TextView lblGuia = (TextView) findViewById(R.id.lblGuia);
        TextView lblGuiaTipo = (TextView) findViewById(R.id.lblGuiaTipo);
        TextView lblFecha = (TextView) findViewById(R.id.lblFecha);
        TextView lblCliente = (TextView) findViewById(R.id.lblCliente);
        TextView lblCompraFecha = (TextView) findViewById(R.id.lblCompraFecha);
        TextView lblCompraNumero = (TextView) findViewById(R.id.lblCompraNumero);
        TextView lblCompraGuia = (TextView) findViewById(R.id.lblCompraGuia);
        TextView lblCompraProveedor = (TextView) findViewById(R.id.lblCompraProveedor);

        lblSerie.setText(row.getString("serie"));
        lblFamilia.setText(row.getString("familia"));
        lblMarca.setText(row.getString("marca"));
        lblModelo.setText(row.getString("modelo"));
        lblTipo.setText(row.getString("tipo"));
        lblPadre.setText(row.getString("padre"));

        JSONObject guia = row.getJSONObject("guia");
        if(guia.length()>0){
            lblGuia.setText(guia.getString("numero"));
            lblGuiaTipo.setText(guia.getString("tipo"));
            lblFecha.setText(guia.getString("fecha"));
            lblCliente.setText(guia.getString("cliente"));
        }

        JSONObject compra = row.getJSONObject("compra");
        if(compra.length()>0){
            lblCompraFecha.setText(compra.getString("fecha"));
            lblCompraNumero.setText(compra.getString("numero"));
            lblCompraGuia.setText(compra.getString("guia"));
            lblCompraProveedor.setText(compra.getString("proveedor"));
        }

        JSONArray comp = row.getJSONArray("componentes");
        if(comp.length()>0){
            TableLayout tblComponentes = (TableLayout) findViewById(R.id.tblComponentes);
            //tblComponentes.setShrinkAllColumns(true);
            tblComponentes.setColumnShrinkable(1, true);
            TableRow tblRow = null;
            TextView lblCompFamilia, lblCompSerie, lblCompCapacidad;
            for (int i = 0; i < comp.length(); i++) {
                JSONObject c = comp.getJSONObject(i);
                tblRow = (TableRow) getLayoutInflater().inflate(R.layout.layout_componentes_item, null);
                lblCompFamilia = (TextView) tblRow.findViewById(R.id.lblCompFamilia);
                lblCompSerie = (TextView) tblRow.findViewById(R.id.lblCompSerie);
                lblCompCapacidad = (TextView) tblRow.findViewById(R.id.lblCompCapacidad);
                lblCompFamilia.setText(c.getString("familia"));
                lblCompSerie.setText(c.getString("serie"));
                lblCompCapacidad.setText(c.getString("capacidad"));
                tblComponentes.addView(tblRow);
            }
        }
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
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.animator.anim_slide_in_left, R.animator.anim_slide_out_left);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.animator.anim_slide_in_right, R.animator.anim_slide_out_right);
        finish();
    }
}
