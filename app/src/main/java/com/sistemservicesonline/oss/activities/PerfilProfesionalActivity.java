package com.sistemservicesonline.oss.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.sistemservicesonline.oss.R;

public class PerfilProfesionalActivity extends AppCompatActivity {

    TextView
            TextViewCodigoPerfilProfesional;

    ImageView
            ImageViewAtras
            , ImageViewEliminar;

    MaterialEditText
            EditTextCargoTitulo
            , EditTextDescripcion;

    Button
            ButtonGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_profesional);

        //InicializarControles();
    }

    private void InicializarControles() {
        TextViewCodigoPerfilProfesional = findViewById(R.id.TextViewCodigoPerfilProfesional);

        /*Inicio MaterialEditText*/
        EditTextCargoTitulo = findViewById(R.id.EditTextCargoTitulo);
        EditTextDescripcion = findViewById(R.id.EditTextDescripcion);
        /*Fin MaterialEditText*/

        String sToken = getIntent().getExtras().getString("Token") != null ? getIntent().getExtras().getString("Token").toString() : "";
        //ObtenerDatosPerfilProfesional(sToken);

        /*Inicio ImageView*/

        ImageViewEliminar = findViewById(R.id.ImageViewEliminar);
        ImageViewEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sToken = getIntent().getExtras().getString("Token") != null ? getIntent().getExtras().getString("Token").toString() : "";
                String sCodigo = getIntent().getExtras().getString("Codigo") != null ? getIntent().getExtras().getString("Codigo").toString() : "";
                //EliminarPerfilProfesional(sToken, sCodigo);
            }
        });
        /*Fin ImageView*/

        /*Inicio Buttons*/
        ButtonGuardar = findViewById(R.id.ButtonGuardarPerfilProfesional);
        ButtonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sCodigo = getIntent().getExtras().getString("Codigo") != null ? getIntent().getExtras().getString("Codigo").toString() : "";
                if (sCodigo != "") {
                    String sCargo = EditTextCargoTitulo.getText() != null ? EditTextCargoTitulo.getText().toString() : "";
                    String sDescripcion = EditTextDescripcion.getText() != null ? EditTextDescripcion.getText().toString() : "";
                    String sToken = getIntent().getExtras().getString("Token") != null ? getIntent().getExtras().getString("Token").toString() : "";
                    //EditarPerfilProfesional(sCodigo, sCargo, sDescripcion, sToken);
                } else {
                    String sCargo = EditTextCargoTitulo.getText() != null ? EditTextCargoTitulo.getText().toString() : "";
                    String sDescripcion = EditTextDescripcion.getText() != null ? EditTextDescripcion.getText().toString() : "";
                    String sToken = getIntent().getExtras().getString("Token") != null ? getIntent().getExtras().getString("Token").toString() : "";
                    //AgregarPerfilProfesional(sCargo, sDescripcion, sToken);
                }
            }
        });
        /*Fin Buttons*/
    }
}
