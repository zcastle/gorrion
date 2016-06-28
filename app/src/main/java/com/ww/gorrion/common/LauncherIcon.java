package com.ww.gorrion.common;

public class LauncherIcon {

    public static final String LAUNCHER_PRODUCTO = "Producto";
    public static final String LAUNCHER_GUIA = "Guia";
    public static final String LAUNCHER_CONTACTO = "Contacto";
    public static final String LAUNCHER_CERRAR = "Cerrar";

    public int imgId;
    public String text;

    public LauncherIcon(int imgId, String text) {
        super();
        this.imgId = imgId;
        this.text = text;
    }

}