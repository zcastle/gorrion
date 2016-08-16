package com.ww.gorrion.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ww.gorrion.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ContactosFacturasPendientesAdapter extends BaseAdapter {

    private Context context;
    private JSONArray lista;

    public ContactosFacturasPendientesAdapter(Context context, JSONArray lista){
        this.context = context;
        this.lista = lista;
    }

    @Override
    public int getCount() {
        return this.lista.length();
    }

    @Override
    public JSONObject getItem(int position) {
        try {
            return lista.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater  = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listview_contactos_facturas_pendientes_item, null);
        }

        JSONObject row = getItem(position);

        TextView tvFacturaFecha = (TextView) view.findViewById(R.id.tvFacturaFecha);
        TextView tvFacturaNumero = (TextView) view.findViewById(R.id.tvFacturaNumero);
        TextView tvFacturaOrden = (TextView) view.findViewById(R.id.tvFacturaOrden);
        TextView tvFacturaMoneda = (TextView) view.findViewById(R.id.tvFacturaMoneda);
        TextView tvFacturaMonto = (TextView) view.findViewById(R.id.tvFacturaMonto);

        try {
            tvFacturaFecha.setText(row.getString("fecha"));
            tvFacturaNumero.setText(row.getString("numero"));
            if(!row.getString("orden").isEmpty()) {
                tvFacturaOrden.setText("OC: ".concat(row.getString("orden")));
            }
            tvFacturaMoneda.setText(row.getString("moneda").concat(" "));
            tvFacturaMonto.setText(row.getString("monto"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }
}
