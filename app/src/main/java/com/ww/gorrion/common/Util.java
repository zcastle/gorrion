package com.ww.gorrion.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.ww.gorrion.LoginActivity;
import com.ww.gorrion.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Util {

    public static void hideKeyboard(View v) {
        try {
            v.clearFocus();
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        } catch (Exception e) {
            // we all saw shit happening on this code before
        }
    }

    public static void showLoginview(Activity activity){
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    public static final String md5(String value){
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(value.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2) {
                    h = "0" + h;
                }
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static final String getString(byte[] response) throws JSONException {
        String values = Arrays.toString(response);
        String[] byteValues = values.substring(1, values.length() - 1).split(",");
        byte[] bytes = new byte[byteValues.length];

        for (int i=0, len=bytes.length; i<len; i++) {
            bytes[i] = Byte.parseByte(byteValues[i].trim());
        }

        return new String(bytes);
    }

    public static final JSONObject getJson(byte[] response) throws JSONException {
        return new JSONObject(getString(response));
    }
}
