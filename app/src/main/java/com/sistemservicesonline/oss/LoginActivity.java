package com.sistemservicesonline.oss;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    RelativeLayout rellay1, rellay2;
    EditText ETUsuario, ETContraseña;
    Button btn_IniciarSesion;


    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        rellay1 = (RelativeLayout) findViewById(R.id.rellay1);
        rellay2 = (RelativeLayout) findViewById(R.id.rellay2);
        handler.postDelayed(runnable, 2000); //2000 is the timeout for the splash

        ETUsuario = (EditText) findViewById(R.id.ETUsuario);
        ETContraseña = (EditText) findViewById(R.id.ETContrasena);

        btn_IniciarSesion = (Button)findViewById(R.id.btn_IniciarSesion);
        btn_IniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidarUsuario(v);
            }
        });
    }

    public void ValidarUsuario (View v){
        try {
            String sUsuario = ETUsuario.getText().toString();
            String sPassword = ETContraseña.getText().toString();

            /*Para pruebas*/
            sUsuario = "Mosorio";
            sPassword = "Mosorio987";
            /*Para pruebas*/

            if (sUsuario.equals("") || sPassword.equals("")){
                Toast.makeText(getApplicationContext(), "Existen campos por diligenciar, por favor verifique.", Toast.LENGTH_SHORT).show();
            } else {
                Intent ObjIntent = new Intent (v.getContext(), MainActivity.class);
                startActivityForResult(ObjIntent, 0);
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
