package com.ww.gorrion.common;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by jc on 26/05/16.
 */
public class HttpUtil {

    private static final AsyncHttpClient client = new AsyncHttpClient();

    public static final void setToken(String token){
        client.setBasicAuth(token, "unused");
    }

    public static final void setUsuario(String usuario, String clave){
        client.setBasicAuth(usuario, clave);
    }

    //public static final void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
    public static final void get(String url, AsyncHttpResponseHandler responseHandler) {
        //client.get(url, params, responseHandler);
        client.get(url, responseHandler);
    }

    public static final void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);
    }

}
