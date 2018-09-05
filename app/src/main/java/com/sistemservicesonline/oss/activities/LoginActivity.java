package com.sistemservicesonline.oss.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sistemservicesonline.oss.appcode.Conexion;
import com.sistemservicesonline.oss.R;
import com.sistemservicesonline.oss.appcode.Usuario;
import com.sistemservicesonline.oss.interfaces.ApiService;
import com.sistemservicesonline.oss.services.APIServiceClient;

import java.security.MessageDigest;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    ProgressDialog
            progressDialog;

    private String gsToken = "";

    List<Usuario> LstUsuario = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        rellay1 = (RelativeLayout) findViewById(R.id.rellay1);
        rellay2 = (RelativeLayout) findViewById(R.id.rellay2);
        handler.postDelayed(runnable, 2000); //2000 is the timeout for the splash

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        ETUsuario = (EditText) findViewById(R.id.ETUsuario);
        ETContraseña = (EditText) findViewById(R.id.ETContrasena);

        btn_IniciarSesion = (Button)findViewById(R.id.btn_IniciarSesion);
        btn_IniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                ValidarUsuario();
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

    public void ValidarUsuario (){
        try {
            String sUsuario = ETUsuario.getText() != null ? ETUsuario.getText().toString() : "";
            String sPassword = EncriptarContrasena(ETContraseña.getText() != null ? ETContraseña.getText().toString() : "", "MD5");

            if (sUsuario.equals("") || sPassword.equals("")){
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Existen campos por diligenciar, por favor verifique.", Toast.LENGTH_SHORT).show();
            } else {
                ApiService apiService = APIServiceClient.getClient().create(ApiService.class);
                Call call = apiService.Login(sUsuario, sPassword);
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        if (response.isSuccessful()) {
                            LstUsuario = (List<Usuario>) response.body();
                            if (!LstUsuario.equals(null) && LstUsuario.size() > 0) {
                                for (int i = 0; i < LstUsuario.size(); i++) {
                                    gsToken = LstUsuario.get(i).getCodigoUsuario() != null ? LstUsuario.get(i).getCodigoUsuario().toString() : "";
                                }
                                progressDialog.dismiss();
                                Intent ObjIntent = new Intent(LoginActivity.this, MainActivity.class);
                                ObjIntent.putExtra("Token", gsToken);
                                ObjIntent.putExtra("bRegistro", "");
                                startActivity(ObjIntent);
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Los datos ingresados no son correctos, por favor verifique.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call call, Throwable t) {
                        progressDialog.dismiss();
                        Log.i("", t.toString());
                        Toast.makeText(getApplicationContext(), "Por favor verifica tu conexión a internet.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static String EncriptarContrasena(String txt, String hashType) {
        try {
            MessageDigest md = java.security.MessageDigest.getInstance(hashType);
            byte[] array = md.digest(txt.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
                        .substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
