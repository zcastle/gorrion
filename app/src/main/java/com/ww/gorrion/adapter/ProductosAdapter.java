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

public class ProductosAdapter extends BaseAdapter {

    private Context context;
    private JSONArray lista;

    public ProductosAdapter(Context context, JSONArray lista){
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
            view = inflater.inflate(R.layout.listview_productos_item, null);
        }

        JSONObject row = getItem(position);

        TextView lblSerie = (TextView) view.findViewById(R.id.lblSerie);
        TextView lblFamilia = (TextView) view.findViewById(R.id.lblFamilia);
        TextView lblTipo = (TextView) view.findViewById(R.id.lblTipo);

        try {
            lblSerie.setText(row.getString("serie"));
            lblFamilia.setText(row.getString("familia"));
            lblTipo.setText(row.getString("tipo"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }
}
