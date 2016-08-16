package com.ww.gorrion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.ww.gorrion.adapter.GuiasAdapter;
import com.ww.gorrion.common.Global;
import com.ww.gorrion.common.HttpUtil;
import com.ww.gorrion.common.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class GuiaActivity extends AppCompatActivity implements View.OnClickListener, ListView.OnItemClickListener {

    private EditText txtNumeroGuia;
    private TextView txtContacto;
    private Spinner spinner;
    private GuiasAdapter mGuiasAdapter = null;
    private ListView lvGuiasProductos;
    private View footerView;
    private JSONObject objContacto = null;
    //private ProgressDialog mDialog;
    private Button btnBuscarGuia;
    private int pagina = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guia);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtNumeroGuia = (EditText) findViewById(R.id.txtNumeroGuia);
        txtContacto = (TextView) findViewById(R.id.txtContacto);
        spinner = (Spinner) findViewById(R.id.spCia);
        lvGuiasProductos = (ListView) findViewById(R.id.lvGuiasProductos);
        footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listview_loader, null, false);
        lvGuiasProductos.setAdapter(null);
        lvGuiasProductos.setOnItemClickListener(this);
        lvGuiasProductos.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {}

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if(lvGuiasProductos.getAdapter()==null){
                    return;
                }

                boolean lastItem = (firstVisibleItem + visibleItemCount == totalItemCount);
                //boolean moreRows = lvGuiasProductos.getAdapter().getCount() < datasource.getSize();

                if (btnBuscarGuia.isEnabled() && lastItem) { // && moreRows
                    //loading = true;
                    //getListView().addFooterView(footerView, null, false);
                    pagina++;
                    buscarGuia(pagina);
                }
            }
        });

        //spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.cia_array, R.layout.spinner_cia_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        btnBuscarGuia = (Button) findViewById(R.id.btnBuscarGuia);
        btnBuscarGuia.setOnClickListener(this);
        //ImageButton btnBuscarContacto = (ImageButton) findViewById(R.id.btnBuscarContacto);
        //btnBuscarContacto.setOnClickListener(this);
    }

    private void buscarGuia(int pagina){
        String numeroGuia = txtNumeroGuia.getText().toString().trim();
        if(numeroGuia.isEmpty()){
            numeroGuia = "all";
        }
        int ciaId = 0, contactoId = 0;
        ciaId = spinner.getSelectedItemPosition()+1;
        if(objContacto!=null){
            try {
                contactoId = objContacto.getInt("id");
            } catch (JSONException e) {
                Log.e("JSONException", e.getMessage());
            }
        }
        btnBuscarGuia.setEnabled(false);
        lvGuiasProductos.addFooterView(footerView, null, false);
        if(pagina==0) {
            lvGuiasProductos.setAdapter(null);
        }
        Util.hideKeyboard(txtNumeroGuia);
        cargarGuias(ciaId+"", contactoId+"", numeroGuia, pagina);

    }

    private void cargarGuias(String ciaId, String contactoId, String numeroGuia, final int pagina){
        HttpUtil.get(Global.URL_GUIA_LISTAR.replace("{ciaId}", ciaId).replace("{contactoId}", contactoId).replace("{numeroGuia}", numeroGuia).replace("{pagina}", pagina+""), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                btnBuscarGuia.setEnabled(true);
                try {
                    JSONObject objResponse = Util.getJson(response);
                    JSONArray data = objResponse.getJSONArray("data");
                    if(data.length()==0) {
                        Toast.makeText(GuiaActivity.this, objResponse.getString("message"), Toast.LENGTH_LONG).show();
                    }else if(data.length()>=1) {
                        if(pagina==0) {
                            mGuiasAdapter = new GuiasAdapter(GuiaActivity.this, data);
                            lvGuiasProductos.setAdapter(mGuiasAdapter);
                        }else{
                            for (int i = 0; i < data.length(); i++){
                                JSONObject obj = data.getJSONObject(i);
                                mGuiasAdapter.addItem(obj);
                                mGuiasAdapter.notifyDataSetChanged();
                            }
                        }
                        lvGuiasProductos.removeFooterView(footerView);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                try {
                    String data = Util.getString(errorResponse);
                    if(data.equals("Unauthorized Access")) {
                        Util.showLoginview(GuiaActivity.this);
                    }else{
                        Log.e("onFailure", data.toString());
                    }
                } catch (JSONException e1) {
                    Log.e("JSONException", e1.getMessage());
                }
            }
        });
    }

    private void limpiar(){
        txtNumeroGuia.setText("");
        txtContacto.setText("");
        spinner.setSelection(0);
        objContacto = null;
        lvGuiasProductos.setAdapter(null);
        pagina = 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.guia, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.guia_limpiar:
                limpiar();
                return true;
            case R.id.guia_buscar_contacto:
                Intent i = new Intent(this, ContactoActivity.class);
                i.putExtra("back", true);
                startActivityForResult(i, 1);
                Util.entrar(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnBuscarGuia){
            buscarGuia(pagina);
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
                    pagina = 0;
                    buscarGuia(pagina);
                    //Log.e("RESULT", objContacto.getInt("id")+"");
                } catch (JSONException e) {
                    Log.e("JSONException", e.getMessage());
                }
            }else if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Util.salir(this);
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object json = parent.getItemAtPosition(position);
        //try {
            //Log.e("json.toString()", json.toString());
            //JSONObject data = new JSONObject(json.toString());
            Intent intent = new Intent(this, GuiaResumenActivity.class);
            //intent.putExtra("id", data.getInt("id"));
            intent.putExtra("json", json.toString());
            startActivity(intent);
            Util.entrar(this);
        /*} catch (JSONException e) {
            e.printStackTrace();
        }*/
    }
}