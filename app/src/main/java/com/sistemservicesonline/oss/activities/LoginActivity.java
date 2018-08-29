package com.sistemservicesonline.oss.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sistemservicesonline.oss.appcode.Conexion;
import com.sistemservicesonline.oss.R;

import java.sql.ResultSet;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {

    RelativeLayout
            rellay1,
            rellay2;

    EditText
            ETUsuario,
            ETContraseña;

    Button
            btn_IniciarSesion,
            btn_Registrarse;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };

    Conexion ObjConexion = new Conexion();

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

        btn_Registrarse = (Button)findViewById(R.id.btn_Registrarse);
        btn_Registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegistrarseActivity.class));
            }
        });
    }

    public void ValidarUsuario (View v){
        try {
            String sUsuario = ETUsuario.getText() != null ? ETUsuario.getText().toString() : "";
            String sPassword = ETContraseña.getText() != null ? ETContraseña.getText().toString() : "";

            /*Para pruebas*/
            sUsuario = "Mosorio";
            sPassword = "Mosorio987";
            /*Para pruebas*/

            String sQuery = "SELECT TOP 1 UA.CodUserAplication AS CodigoUsuario FROM Usuarios.UsuarioAplicacion UA  WITH(NOLOCK) INNER JOIN Usuarios.Usuario U WITH(NOLOCK) ON U.CodUsuarioAplicacion = UA.CodUserAplication WHERE UA.Password = '" + sPassword + "' AND (UA.Usuario = '" + sUsuario + "' OR U.Email = '" + sUsuario +  "')";

            if (sUsuario.equals("") || sPassword.equals("")){
                Toast.makeText(getApplicationContext(), "Existen campos por diligenciar, por favor verifique.", Toast.LENGTH_SHORT).show();
            } else {
                Statement ObjStatement = ObjConexion.ConexionDB().createStatement();
                ResultSet ObjResultSet = ObjStatement.executeQuery(sQuery);
                if (ObjResultSet != null){
                    while (ObjResultSet.next()){
                        Intent ObjIntent = new Intent (v.getContext(), MainActivity.class);
                        ObjIntent.putExtra("Token", ObjResultSet.getString("CodigoUsuario"));
                        startActivityForResult(ObjIntent, 0);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "El Usuario o Contraseña es incorrecta, por favor verifique", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
