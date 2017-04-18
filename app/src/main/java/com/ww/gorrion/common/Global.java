package com.ww.gorrion.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Global {

    public static final boolean DEBUG = false;

    private static String HOST;
    private static final String HOST_DEV = "http://192.168.2.10:5000/api/";
    private static final String HOST_PROD = "http://www.winwaresac.com:5000/api/";
    //private static final String HOST_PROD = "http://winwaresac.ddns.net:5000/api/";

    public static final String URL_TOKEN;
    public static final String URL_LOGIN;
    public static final String URL_PRODUCTO;
    public static final String URL_PRODUCTO_LISTAR;
    public static final String URL_CONTACTO;
    public static final String URL_CONTACTO_LISTAR;
    public static final String URL_CONTACTO_FACTURAS_PENDIENTES;
    public static final String URL_GUIA_LISTAR;
    public static final String URL_GUIA_TIPO_LISTAR;
    public static final String URL_GUIA_RESUMEN;
    //
    public static int ROL_ACTIVE;
    public static int ROL_ADMINISTRACION_VENTAS = 34;
    public static int ROL_ALMACEN = 38;
    public static int ROL_VENTAS = 40;
    public static int ROL_ADMINISTRACION = 41;
    public static int ROL_CAJA = 42;
    public static int ROL_SIN_AREA = 43;
    public static int ROL_GERENIA = 44;
    public static int ROL_SOPORTE = 45;
    public static int ROL_SISTEMAS= 46;
    public static int ROL_VENTAS2 = 47;
    public static int ROL_CLIENTE = 99;
    /*
    public static String LABEL_USUARIO = "LABEL_USUARIO";
    public static String LABEL_PRODUCTO = "LABEL_PRODUCTO";
    public static String ARG_LAYOUT = "ARG_LAYOUT";
    */

    static {
        if(DEBUG){
            HOST = HOST_DEV;
        }else{
            HOST = HOST_PROD;
        }
        //
        URL_TOKEN = HOST.concat("token");
        URL_LOGIN = HOST.concat("login");
        //
        URL_PRODUCTO = HOST.concat("producto/{id}");
        URL_PRODUCTO_LISTAR = HOST.concat("producto/listar/{serie}/{serieExacta}");
        //
        URL_CONTACTO = HOST.concat("contacto/{id}");
        URL_CONTACTO_LISTAR = HOST.concat("contacto/listar/{nombreContacto}");
        URL_CONTACTO_FACTURAS_PENDIENTES = HOST.concat("contacto/facturas/pendientes/{id}");
        //
        URL_GUIA_LISTAR = HOST.concat("guia/listar/{ciaId}/{contactoId}/{numeroGuia}/{pagina}");
        URL_GUIA_TIPO_LISTAR = HOST.concat("guia/tipo/listar");
        URL_GUIA_RESUMEN = HOST.concat("guia/detalle/resumen/{guiaId}");
    }

    /*private Boolean conectadoWifi(){
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    private Boolean conectadoRedMovil(){
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }*/
}