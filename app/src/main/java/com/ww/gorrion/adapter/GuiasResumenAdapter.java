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

public class GuiasResumenAdapter extends BaseAdapter {

    private Context context;
    private JSONArray lista;

    public GuiasResumenAdapter(Context context, JSONArray lista){
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

    public void addItem(JSONObject item){
        lista.put(item);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater  = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listview_guias_resumen_item, null);
        }

        JSONObject row = getItem(position);

        TextView tvFamilia = (TextView) view.findViewById(R.id.tvFamilia);
        TextView tvFamiliaCantidad = (TextView) view.findViewById(R.id.tvFamiliaCantidad);

        try {
            tvFamilia.setText(row.getString("familia"));
            tvFamiliaCantidad.setText(row.getString("cantidad"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }
}
