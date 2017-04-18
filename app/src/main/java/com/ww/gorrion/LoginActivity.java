package com.ww.gorrion;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.ww.gorrion.common.Global;
import com.ww.gorrion.common.HttpUtil;
import com.ww.gorrion.common.SessionManager;
import com.ww.gorrion.common.Util;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    private EditText mUsuarioView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private SessionManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsuarioView = (EditText) findViewById(R.id.usuario);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                attemptLogin();
                return false;
            }
        });

        Button btnLogin = (Button) findViewById(R.id.sign_in_button);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        manager = new SessionManager();
    }

    private void attemptLogin() {
        mUsuarioView.setError(null);
        mPasswordView.setError(null);

        String usuario = mUsuarioView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(usuario)) {
            mUsuarioView.setError(getString(R.string.error_field_required));
            focusView = mUsuarioView;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            login(usuario, Util.md5(password));
        }
    }

    private void login(final String usuario, final String password){
        showProgress(true, true);
        HttpUtil.setUsuario(usuario, password);
        HttpUtil.get(Global.URL_TOKEN, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                showProgress(false, true);
                try {
                    JSONObject data = Util.getJson(response);
                    String token = data.getString("token");
                    int rol = Global.ROL_CLIENTE;
                    try{
                        rol = data.getInt("rol");
                    }catch(Exception e){
                        rol = Global.ROL_CLIENTE;
                    }finally {
                        Global.ROL_ACTIVE = rol;
                    }

                    manager.setToken(LoginActivity.this, token);
                    Intent intent = new Intent(LoginActivity.this, LauncherActivity.class);
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    mPasswordView.setError(getString(R.string.error_desconocida));
                    mPasswordView.requestFocus();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                showProgress(false, true);
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder
                        .setMessage("Usuario/Clave incorrecto")
                        .setPositiveButton("Aceptar",  new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                mPasswordView.setError(getString(R.string.error_desconocida));
                                mPasswordView.requestFocus();
                            }
                        })
                        .show();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show, final boolean showForm) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            if(showForm) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                mLoginFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                    }
                });
            }

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            if(showForm) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        }
    }
}
