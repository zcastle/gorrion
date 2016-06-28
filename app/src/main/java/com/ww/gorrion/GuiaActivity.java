package com.ww.gorrion;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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

public class GuiaActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txtNumeroGuia;
    private TextView txtContacto;
    private Spinner spinner;
    private JSONObject objContacto = null;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guia);
        if (savedInstanceState == null) {
            overridePendingTransition(R.animator.anim_slide_in_left, R.animator.anim_slide_out_left);
        }

        txtNumeroGuia = (EditText) findViewById(R.id.txtNumeroGuia);
        txtContacto = (TextView) findViewById(R.id.txtContacto);
        spinner = (Spinner) findViewById(R.id.spCia);
        //spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.cia_array, R.layout.spinner_cia_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Button btnBuscar = (Button) findViewById(R.id.btnBuscarGuia);
        btnBuscar.setOnClickListener(this);
        ImageButton btnBuscarContacto = (ImageButton) findViewById(R.id.btnBuscarContacto);
        btnBuscarContacto.setOnClickListener(this);
    }

    private void buscarGuia(){
        /*String buscar = txtBuscarContacto.getText().toString().trim();

        if(buscar.isEmpty()) {
            Toast.makeText(this, "Debe imgresar un nombre", Toast.LENGTH_SHORT).show();
            lvResultadoContacto.setAdapter(null);
        }else{
            Util.hideKeyboard(txtBuscarContacto);
            mDialog = new ProgressDialog(this);
            mDialog.setMessage("Buscando...");
            if(!mDialog.isShowing()){
                mDialog.show();
            }
            HttpUtil.get(Global.URL_GUIA_LISTAR.replace("{nombreContacto}", buscar), new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    if (mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    try {
                        JSONObject objResponse = Util.getJson(response);
                        JSONArray data = objResponse.getJSONArray("data");
                        if(data.length()==0) {
                            Toast.makeText(ContactoActivity.this, objResponse.getString("message"), Toast.LENGTH_LONG).show();
                        }else if(data.length()>=1) {
                            lvResultadoContacto.setAdapter(new ContactosAdapter(ContactoActivity.this, data));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                    if (mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    Util.showLoginview(ContactoActivity.this);
                }
            });
        }*/
    }

    private void limpiar(){
        txtNumeroGuia.setText("");
        txtContacto.setText("");
        spinner.setSelection(0);
        objContacto = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.guia, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.guia_limpiar:
                limpiar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnBuscarGuia){
            //Toast.makeText(this, txtNumeroGuia.getText().toString(), Toast.LENGTH_LONG).show();
            Toast.makeText(this, spinner.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            //buscarGuia();
        }else if(v.getId()==R.id.btnBuscarContacto){
            Intent i = new Intent(this, ContactoActivity.class);
            startActivityForResult(i, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result = data.getStringExtra("result");
                try {
                    objContacto = new JSONObject(result);
                    txtContacto.setText(objContacto.getString("alias"));
                    Log.e("RESULT", objContacto.getInt("id")+"");
                } catch (JSONException e) {
                    Log.e("JSONException", e.getMessage());
                }
            }else if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
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
