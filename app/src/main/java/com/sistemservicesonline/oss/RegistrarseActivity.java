package com.sistemservicesonline.oss;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.sistemservicesonline.oss.App_Code.GestionUsuarios.Usuario;

public class RegistrarseActivity extends AppCompatActivity {

    Button
        ButtonRegistrarse;

    MaterialEditText
            EditTextNombreCompleto,
            EditTextCorreoElectronico,
            EditTextContrasena,
            EditTextConfirmarContrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*Buttons*/
        ButtonRegistrarse = findViewById(R.id.ButtonRegistrar);
        ButtonRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistrarUsuario(v);
            }
        });

        EditTextNombreCompleto = findViewById(R.id.EditTextNombreCompleto);
        EditTextCorreoElectronico = findViewById(R.id.EditTextCorreoElectronico);
        EditTextContrasena = findViewById(R.id.EditTextContrasena);
        EditTextConfirmarContrasena = findViewById(R.id.EditTextConfirmarContrasena);
    }

    public void RegistrarUsuario (View v) {
        try {
            Usuario gObjUsuario = new Usuario();

            String sToken = "";
            String sNombreCompleto = EditTextNombreCompleto.getText() != null ? EditTextNombreCompleto.getText().toString() : "";
            String sPrimerNombre = sNombreCompleto.split(" ")[0] != null ? sNombreCompleto.split(" ")[0] : "";
            String sSegundoNombre = sNombreCompleto.split(" ")[1] != null ? sNombreCompleto.split(" ")[1] : "";
            String sPrimerApellido = sNombreCompleto.split(" ")[2] != null ? sNombreCompleto.split(" ")[2] : "";
            String sSegundoApellido = sNombreCompleto.split(" ")[3] != null ? sNombreCompleto.split(" ")[3] : "";
            String sCorreoElectronico = EditTextCorreoElectronico.getText() != null ? EditTextCorreoElectronico.getText().toString() : "";
            String sContrasena = EditTextContrasena.getText() != null ? EditTextContrasena.getText().toString() : "";
            String sContrasenaConfirmada = EditTextConfirmarContrasena.getText() != null ? EditTextConfirmarContrasena.getText().toString() : "";

            if (!sNombreCompleto.equals("") && !sCorreoElectronico.equals("") && !sContrasena.equals("") && !sContrasenaConfirmada.equals("")) {
                if (sContrasenaConfirmada == sContrasena) {
                    gObjUsuario.sCodUsuarioAplicacion = sToken;
                    gObjUsuario.sPrimerNombre = sPrimerNombre;
                    gObjUsuario.sSegundoNombre = sSegundoNombre;
                    gObjUsuario.sPrimerApellido = sPrimerApellido;
                    gObjUsuario.sSegundoApellido = sSegundoApellido;
                    gObjUsuario.sEmail = sCorreoElectronico;
                    gObjUsuario.sContrasena = sContrasena;

                    if (gObjUsuario != null) {
                        Intent ObjIntent = new Intent(RegistrarseActivity.this, MainActivity.class);
                        ObjIntent.putExtra("sToken", sToken);
                        ObjIntent.putExtra("bRegistro", true);
                        startActivity(ObjIntent);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Las contraseñas no concuerdan, por favor verifique.", Toast.LENGTH_SHORT).show();
                    EditTextConfirmarContrasena.requestFocus();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Existen campos por diligenciar, por favor verifique.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error en RegistrarUsuario() : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
