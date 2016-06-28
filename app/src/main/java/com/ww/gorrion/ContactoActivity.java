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

public class ContactoActivity extends AppCompatActivity implements View.OnClickListener, ListView.OnItemClickListener {

    private EditText txtBuscarContacto;
    private ProgressDialog mDialog;
    private ListView lvResultadoContacto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) {
            overridePendingTransition(R.animator.anim_slide_in_left, R.animator.anim_slide_out_left);
        }

        txtBuscarContacto = (EditText) findViewById(R.id.txtBuscarContacto);
        txtBuscarContacto.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                OnClickBuscar();
                return false;
            }
        });
        Button btnBuscarContacto = (Button) findViewById(R.id.btnBuscarContacto);
        btnBuscarContacto.setOnClickListener(this);
        lvResultadoContacto = (ListView) findViewById(R.id.lvResultadoContacto);
        lvResultadoContacto.setOnItemClickListener(this);
    }

    private void OnClickBuscar() {
        String buscar = txtBuscarContacto.getText().toString().trim();

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
            HttpUtil.get(Global.URL_CONTACTO_LISTAR.replace("{nombreContacto}", buscar), new AsyncHttpResponseHandler() {
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
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnBuscarContacto){
            OnClickBuscar();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object json = parent.getItemAtPosition(position);
        Log.e("json.toString()", json.toString());

        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", json.toString());
        setResult(Activity.RESULT_OK, returnIntent);
        onBackPressed();
        /*
        Intent intent = new Intent(this, ProductoResultadoActivity.class);
        intent.putExtra("id", json);
        startActivity(intent);
        overridePendingTransition(R.animator.enter, R.animator.exit);
         */
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