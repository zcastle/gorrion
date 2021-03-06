package com.ww.gorrion;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.ww.gorrion.adapter.LauncherAdapter;
import com.ww.gorrion.common.Global;
import com.ww.gorrion.common.LauncherIcon;
import com.ww.gorrion.common.SessionManager;
import com.ww.gorrion.common.Util;

public class LauncherActivity extends AppCompatActivity implements ListView.OnItemClickListener {

    GridView gvDash;
    private SessionManager manager;

    static final LauncherIcon[] ICONS_CLIENTE = {
            new LauncherIcon(R.drawable.ic_laptop_white_48dp, LauncherIcon.LAUNCHER_PRODUCTO),
            new LauncherIcon(R.drawable.ic_exit_to_app_white_48dp, LauncherIcon.LAUNCHER_CERRAR)
    };

    static final LauncherIcon[] ICONS = {
            new LauncherIcon(R.drawable.ic_laptop_white_48dp, LauncherIcon.LAUNCHER_PRODUCTO),
            new LauncherIcon(R.drawable.ic_content_paste_white_48dp, LauncherIcon.LAUNCHER_GUIA),
            new LauncherIcon(R.drawable.ic_group_white_48dp, LauncherIcon.LAUNCHER_CONTACTO),
            new LauncherIcon(R.drawable.ic_exit_to_app_white_48dp, LauncherIcon.LAUNCHER_CERRAR)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        gvDash = (GridView) findViewById(R.id.gvDash);
        if(Global.ROL_ACTIVE==Global.ROL_CLIENTE){
            gvDash.setAdapter(new LauncherAdapter(this, ICONS_CLIENTE));
        }else{
            gvDash.setAdapter(new LauncherAdapter(this, ICONS));
        }

        gvDash.setOnItemClickListener(this);
        manager = new SessionManager();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LauncherIcon launcherIcon = (LauncherIcon) parent.getItemAtPosition(position);
        Intent i = null;
        switch (launcherIcon.text){
            case LauncherIcon.LAUNCHER_PRODUCTO:
                i = new Intent(this, ProductoActivity.class);
                break;
            case LauncherIcon.LAUNCHER_GUIA:
                i = new Intent(this, GuiaActivity.class);
                break;
            case LauncherIcon.LAUNCHER_CONTACTO:
                i = new Intent(this, ContactoActivity.class);
                i.putExtra("back", false);
                break;
            case LauncherIcon.LAUNCHER_CERRAR:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder
                        .setMessage(R.string.lbl_cerrar_sesion)
                        .setPositiveButton(R.string.lbl_si,  new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                manager.setToken(LauncherActivity.this, "");
                                Util.showLoginview(LauncherActivity.this);
                            }
                        })
                        .setNegativeButton(R.string.lbl_no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        })
                        .show();
                break;

        }
        if(i!=null) {
            startActivity(i);
            Util.entrar(this);
        }
    }
}
