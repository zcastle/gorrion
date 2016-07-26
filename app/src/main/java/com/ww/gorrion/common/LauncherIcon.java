package com.ww.gorrion.common;

public class LauncherIcon {

    public static final String LAUNCHER_PRODUCTO = "Productos";
    public static final String LAUNCHER_GUIA = "Guias";
    public static final String LAUNCHER_CONTACTO = "Contactos";
    public static final String LAUNCHER_CERRAR = "Cerrar";

    public int imgId;
    public String text;

    public LauncherIcon(int imgId, String text) {
        super();
        this.imgId = imgId;
        this.text = text;
    }

}