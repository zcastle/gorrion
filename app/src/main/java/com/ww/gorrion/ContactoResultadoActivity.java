package com.ww.gorrion;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.ww.gorrion.adapter.ContactosAdapter;
import com.ww.gorrion.adapter.ContactosPersonasAdapter;
import com.ww.gorrion.common.Global;
import com.ww.gorrion.common.HttpUtil;
import com.ww.gorrion.common.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ContactoResultadoActivity extends AppCompatActivity implements ListView.OnItemClickListener {

    private ProgressDialog mDialog;
    //private TextView lblAlias;
    private String id;
    private String alias;
    private TextView lblRuc;
    private TextView lblRazon;
    private TextView lblDireccion;
    private TextView lblDistrito;
    private TextView lblProvincia;
    private TextView lblDepartamento;
    private ListView lvContactoPersona;
    private Double lat;
    private Double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto_resultado);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //lblAlias = (TextView) findViewById(R.id.lblAlias);
        lblRuc = (TextView) findViewById(R.id.lblRuc);
        lblRazon = (TextView) findViewById(R.id.lblRazon);
        lblDireccion = (TextView) findViewById(R.id.lblDireccion);
        lblDistrito = (TextView) findViewById(R.id.lblDistrito);
        lblProvincia = (TextView) findViewById(R.id.lblProvincia);
        lblDepartamento = (TextView) findViewById(R.id.lblDepartamento);
        lvContactoPersona = (ListView) findViewById(R.id.lvContactoPersona);
        lvContactoPersona.setOnItemClickListener(this);

        String json = getIntent().getStringExtra("data");

        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Cargando");
        if (!mDialog.isShowing()) {
            mDialog.show();
        }

        try {
            JSONObject data = new JSONObject(json);
            id = data.getString("id");
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
        //lblAlias.setText(data.getString("alias"));
        alias = data.getString("alias");
        setTitle(alias);
        lblRuc.setText(data.getString("ruc"));
        lblRazon.setText(data.getString("razon"));
        lblDireccion.setText(data.getString("direccion"));
        lblDistrito.setText(data.getString("distrito"));
        lblProvincia.setText(data.getString("provincia"));
        lblDepartamento.setText(data.getString("departamento"));
        JSONObject location = data.getJSONObject("location");
        lat = location.getDouble("lat");
        lng = location.getDouble("lng");

        JSONArray lista = data.getJSONArray("personas");
        lvContactoPersona.setAdapter(new ContactosPersonasAdapter(this, lista));

        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contacto_resultado, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = null;
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.irMapa:
                i = new Intent(this, MapsActivity.class);
                i.putExtra("contacto", alias);
                i.putExtra("direccion", lblDireccion.getText());
                i.putExtra("lat", lat);
                i.putExtra("lng", lng);
                break;
            case R.id.irWaze:
                try {
                    //String url = "waze://?q=Peru";
                    String url = "waze://?ll=" + lat + "," + lng + "&navigate=yes";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.waze"));
                    startActivity(intent);
                }
                break;
            case R.id.irFacturas:
                i = new Intent(this, ContactoFacturasPendientesActivity.class);
                i.putExtra("id", id);
                break;
        }
        if(i!=null) {
            startActivity(i);
            Util.entrar(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Util.salir(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object json = parent.getItemAtPosition(position);
        try {
            JSONObject obj = new JSONObject(json.toString());
            String nombre = obj.getString("nombre");
            final String telefono = obj.getString("telefono");
            final String correo = obj.getString("correo");

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            CharSequence[] lista = {"Enviar correo ".concat(correo), "Llamar ".concat(telefono)};
            builder
                    .setTitle(nombre)
                    .setItems(lista, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(getBaseContext(), which+"", Toast.LENGTH_LONG).show();
                            switch (which){
                                case 0:
                                    enviarCorreo(correo);
                                    break;
                                case 1:
                                    llamar(telefono);
                                    break;
                            }
                        }
                    })
                    .show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void enviarCorreo(String to){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        //intent.putExtra(Intent.EXTRA_EMAIL, to);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {to});
        //intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void llamar(String telefono){
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:".concat(telefono)));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);
    }
}
