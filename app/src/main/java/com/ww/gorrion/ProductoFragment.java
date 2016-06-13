package com.ww.gorrion;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.ww.gorrion.adapter.VariosAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ProductoFragment extends Fragment implements View.OnClickListener, ListView.OnItemClickListener {

    private LayoutInflater inflater;
    private EditText txtSerie;
    private Switch swSerieExacta;
    //private Button btnBuscar;
    private ProgressDialog mDialog;
    private RelativeLayout lResultado;
    private ListView lvResultadoVarios;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        View rootView = inflater.inflate(R.layout.fragment_productos, container, false);

        txtSerie = (EditText) rootView.findViewById(R.id.txtSerie);
        swSerieExacta = (Switch) rootView.findViewById(R.id.swSerieExacta);
        Button btnBuscar = (Button) rootView.findViewById(R.id.btnBuscar);
        btnBuscar.setOnClickListener(this);
        /*Button btnBuscarCliente = (Button) rootView.findViewById(R.id.btnBuscarCliente);
        btnBuscarCliente.setOnClickListener(this);*/
        lResultado = (RelativeLayout) rootView.findViewById(R.id.pblResultado);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnBuscar){
            OnClickBuscar();
        }else if(v.getId()==R.id.btnVolver){
            lResultado.removeAllViews();
            lResultado.addView(this.lvResultadoVarios);
        }else if(v.getId()==R.id.btnBuscarCliente){

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object json = parent.getItemAtPosition(position);
        try {
            JSONObject row = new JSONObject(json.toString());
            lResultado.removeAllViews();
            lResultado.addView(getViewUno(row, true));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void OnClickBuscar() {
        hideKeyboard(txtSerie);
        String serie = txtSerie.getText().toString().trim();
        String serieExacta = "false";
        if(swSerieExacta.isChecked()){
            serieExacta = "true";
        }
        if(serie.isEmpty()) {
            Toast.makeText(getActivity(), "Debe imgresar una serie", Toast.LENGTH_SHORT).show();
            lResultado.removeAllViews();
        }else{
            mDialog = new ProgressDialog(getActivity());
            mDialog.setMessage("Buscando...");
            if(!mDialog.isShowing()){
                mDialog.show();
            }
            HttpUtil.get(Global.URL_PRODUCTO.replace("{serie}", serie).replace("{serieExacta}", serieExacta), null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    //txtSerie.setText("");
                    if (mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    try {
                        addResult(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {}
            });
        }
    }

    private void addResult(JSONObject response) throws JSONException {
        String message = response.getString("message");
        JSONArray data = response.getJSONArray("data");
        lResultado.removeAllViews();
        if(data.length()==0) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        }else if(data.length()==1) {
            JSONObject row = data.getJSONObject(0);
            lResultado.addView(getViewUno(row, false));
        }else if(data.length()>1) {
            lvResultadoVarios = new ListView(getActivity());
            lvResultadoVarios.setOnItemClickListener((AdapterView.OnItemClickListener) this);
            lvResultadoVarios.setAdapter(new VariosAdapter(getActivity(), data));
            lResultado.addView(lvResultadoVarios);
        }
    }

    private View getViewUno(JSONObject row, boolean visible) throws JSONException {
        //LayoutInflater inflater  = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.layout_template_uno , null);

        TextView lblSerie = (TextView) view.findViewById(R.id.lblSerieUno);
        TextView lblFamilia = (TextView) view.findViewById(R.id.lblFamiliaUno);
        TextView lblMarca = (TextView) view.findViewById(R.id.lblModeloUno);
        TextView lblModelo = (TextView) view.findViewById(R.id.lblModeloUno);
        TextView lblTipo = (TextView) view.findViewById(R.id.lblTipoUno);
        TextView lblGuia = (TextView) view.findViewById(R.id.lblGuiaUno);
        TextView lblFecha = (TextView) view.findViewById(R.id.lblFechaUno);
        TextView lblCliente = (TextView) view.findViewById(R.id.lblClienteUno);
        Button btnVolver = (Button) view.findViewById(R.id.btnVolver);
        btnVolver.setVisibility(View.INVISIBLE);
        if(visible){
            btnVolver.setVisibility(View.VISIBLE);
            btnVolver.setOnClickListener(this);
        }

        lblSerie.setText(row.getString("serie"));
        lblFamilia.setText(row.getString("familia"));
        lblMarca.setText(row.getString("marca"));
        lblModelo.setText(row.getString("modelo"));
        lblTipo.setText(row.getString("tipo"));
        lblGuia.setText(row.getString("guia"));
        lblFecha.setText(row.getString("fecha"));
        lblCliente.setText(row.getString("cliente"));

        return view;
    }

    public static void hideKeyboard(View v) {
        try {
            v.clearFocus();
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        } catch (Exception e) {
            // we all saw shit happening on this code before
        }
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_barcode:
                IntentIntegrator integrator = new IntentIntegrator(this);
                integrator.initiateScan();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }*/

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            String value = scanResult.getContents();
            //Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
            txtSerie.setText(value);
            onClick(txtSerie);
        }
    }
}