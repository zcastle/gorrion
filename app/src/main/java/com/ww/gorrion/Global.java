package com.ww.gorrion;

public class Global {

    public static final boolean DEBUG = true;

    private static String HOST;
    private static final String HOST_PROD = "http://www.winwaresac.com/ww/";
    private static final String HOST_DEV = "http://192.168.2.44:5000/api/";

    public static final String URL_PRODUCTO;
    public static final String URL_LOGIN = "http://www.winwaresac.com/ww/verifylogin";

    public static String LABEL_USUARIO = "LABEL_USUARIO";

    public static String ARG_LAYOUT = "ARG_LAYOUT";

    static {
        if(DEBUG){
            HOST = HOST_PROD;
        }else{
            HOST = HOST_PROD;
        }

        //URL_PRODUCTO = HOST.concat("producto/{serie}/{serieExacta}");
        URL_PRODUCTO = HOST.concat("util/serie/{serie}/{serieExacta}");

    }

}
