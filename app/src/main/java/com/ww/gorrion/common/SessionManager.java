package com.ww.gorrion.common;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    public void setToken(Context context, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences("session_ww", Context.MODE_PRIVATE).edit();
        HttpUtil.setToken(value);
        editor.putString("token", value);
        editor.commit();
    }

    public String getToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("session_ww", Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");
        HttpUtil.setToken(token);
        return token;
    }
}
