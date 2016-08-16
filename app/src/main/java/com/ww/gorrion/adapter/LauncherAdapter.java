package com.ww.gorrion.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ww.gorrion.R;
import com.ww.gorrion.common.LauncherIcon;

public class LauncherAdapter extends BaseAdapter {

    private Context mContext;
    private LauncherIcon[] launcherIcon;

    public LauncherAdapter(Context c, LauncherIcon[] launcherIcon) {
        mContext = c;
        this.launcherIcon = launcherIcon;
    }

    @Override
    public int getCount() {
        return launcherIcon.length;
    }

    @Override
    public LauncherIcon getItem(int position) {
        return launcherIcon[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder {
        public ImageView icon;
        public TextView text;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            v = vi.inflate(R.layout.launcher_iem, null);
            //v.setBackgroundColor(Color.GREEN);
            holder = new ViewHolder();
            holder.text = (TextView) v.findViewById(R.id.lacuncher_icon_text);
            holder.icon = (ImageView) v.findViewById(R.id.lacuncher_icon_img);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        holder.icon.setImageResource(launcherIcon[position].imgId);
        holder.text.setText(launcherIcon[position].text);

        return v;
    }

}
