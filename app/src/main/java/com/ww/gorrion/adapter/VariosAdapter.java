package com.ww.gorrion.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ww.gorrion.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jc on 26/05/16.
 */
public class VariosAdapter extends BaseAdapter {

    private Context context;
    private JSONArray lista;

    public VariosAdapter(Context context, JSONArray lista){
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
            return this.lista.getJSONObject(position);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater  = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_template_varios , null);
        }

        JSONObject row = getItem(position);

        TextView lblSerie = (TextView) convertView.findViewById(R.id.lblSerie);
        TextView lblFamilia = (TextView) convertView.findViewById(R.id.lblFamilia);
        TextView lblTipo = (TextView) convertView.findViewById(R.id.lblTipo);

        try {
            lblSerie.setText(row.getString("serie"));
            lblFamilia.setText(row.getString("familia"));
            lblTipo.setText(row.getString("tipo"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
