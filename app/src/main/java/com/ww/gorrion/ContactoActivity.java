package com.ww.gorrion;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
    private ListView lvResultadoContacto;
    private View footerView;
    private Button btnBuscarContacto;
    private boolean back = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        back = getIntent().getBooleanExtra("back", false);


        txtBuscarContacto = (EditText) findViewById(R.id.txtBuscarContacto);
        txtBuscarContacto.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                OnClickBuscar();
                return false;
            }
        });
        btnBuscarContacto = (Button) findViewById(R.id.btnBuscarContacto);
        btnBuscarContacto.setOnClickListener(this);
        lvResultadoContacto = (ListView) findViewById(R.id.lvResultadoContacto);
        footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listview_guias_footer, null, false);
        lvResultadoContacto.setOnItemClickListener(this);
    }

    private void OnClickBuscar() {
        String buscar = txtBuscarContacto.getText().toString().trim();

        if(buscar.isEmpty()) {
            Toast.makeText(this, "Debe imgresar un nombre", Toast.LENGTH_SHORT).show();
            lvResultadoContacto.setAdapter(null);
        }else{
            btnBuscarContacto.setEnabled(false);
            lvResultadoContacto.addFooterView(footerView, null, false);
            lvResultadoContacto.setAdapter(null);
            Util.hideKeyboard(txtBuscarContacto);
            HttpUtil.get(Global.URL_CONTACTO_LISTAR.replace("{nombreContacto}", buscar), new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    btnBuscarContacto.setEnabled(true);
                    try {
                        JSONObject objResponse = Util.getJson(response);
                        JSONArray data = objResponse.getJSONArray("data");
                        if(data.length()==0) {
                            Toast.makeText(ContactoActivity.this, objResponse.getString("message"), Toast.LENGTH_LONG).show();
                        }else if(data.length()>=1) {
                            lvResultadoContacto.setAdapter(new ContactosAdapter(ContactoActivity.this, data));
                            lvResultadoContacto.removeFooterView(footerView);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
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
        //Log.e("json.toString()", json.toString());

        if(back) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result", json.toString());
            setResult(Activity.RESULT_OK, returnIntent);
            onBackPressed();
        }else{
            Intent i = new Intent(this, ContactoResultadoActivity.class);
            i.putExtra("data", json.toString());
            startActivity(i);
            Util.entrar(this);
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
    public void onBackPressed() {
        super.onBackPressed();
        Util.salir(this);
        finish();
    }
}