package com.ww.gorrion.common;

public class Global {

    public static final boolean DEBUG = true;

    private static String HOST;
    private static final String HOST_DEV = "http://192.168.2.44:5000/api/";
    private static final String HOST_PROD = "http://www.winwaresac.com:5000/api/";

    public static final String URL_TOKEN;
    public static final String URL_LOGIN;
    public static final String URL_PRODUCTO;
    public static final String URL_PRODUCTO_LISTAR;
    public static final String URL_CONTACTO;
    public static final String URL_CONTACTO_LISTAR;
    public static final String URL_GUIA_LISTAR;

    //public static String LABEL_USUARIO = "LABEL_USUARIO";
    //public static String LABEL_PRODUCTO = "LABEL_PRODUCTO";

    public static String ARG_LAYOUT = "ARG_LAYOUT";

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
        //
        URL_GUIA_LISTAR = HOST.concat("guia/listar/{ciaId}/{contactoId}/{numero}");
    }
}
