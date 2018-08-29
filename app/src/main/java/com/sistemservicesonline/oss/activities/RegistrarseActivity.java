package com.sistemservicesonline.oss.activities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.sistemservicesonline.oss.appcode.Usuario;
import com.sistemservicesonline.oss.appcode.Utilidades;
import com.sistemservicesonline.oss.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class RegistrarseActivity extends AppCompatActivity {

    Button
            ButtonRegistrarse;

    MaterialEditText
            EditTextNombreCompleto,
            EditTextIdentificacion,
            EditTextFechaNacimiento,
            EditTextCelular,
            EditTextCorreoElectronico,
            EditTextContrasena,
            EditTextConfirmarContrasena;

    MaterialBetterSpinner
            MaterialBetterSpinnerTipoIdentificacion,
            MaterialBetterSpinnerCategoria,
            MaterialBetterSpinnerGenero;

    TextView
            TextViewCambiarFoto;

    ImageView
            ImageViewFotoPerfil,
            ImageViewCambiarFechaNacimiento;

    private static final String CERO = "0";
    private static final String BARRA = "/";

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    final int iCodigoFotoSeleccionada = 10;
    final int iCodigoFotoTomada = 20;

    ArrayList<String> lstTiposIdentificacion;
    ArrayList<String> lstGeneros;

    Utilidades ObjUtilidades = new Utilidades();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageViewFotoPerfil = findViewById(R.id.ImageViewFotoPerfil);
        ImageViewCambiarFechaNacimiento = findViewById(R.id.ImageViewCambiarFechaNacimiento);
        ImageViewCambiarFechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerFecha();
            }
        });

        TextViewCambiarFoto = findViewById(R.id.TextViewCambiarFoto);
        TextViewCambiarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CargarImagenPerfil();
            }
        });

        /*Buttons*/
        ButtonRegistrarse = findViewById(R.id.ButtonRegistrar);
        ButtonRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistrarUsuario(v);
            }
        });

        EditTextIdentificacion = findViewById(R.id.EditTextNombreCompleto);
        EditTextNombreCompleto = findViewById(R.id.EditTextNombreCompleto);
        EditTextFechaNacimiento = findViewById(R.id.EditTextFechaNacimiento);
        EditTextCelular = findViewById(R.id.EditTextCelular);
        EditTextCorreoElectronico = findViewById(R.id.EditTextCorreoElectronico);
        EditTextContrasena = findViewById(R.id.EditTextContrasena);
        EditTextConfirmarContrasena = findViewById(R.id.EditTextConfirmarContrasena);

        /*Inicio MaterialBetterSpinner*/
        lstTiposIdentificacion = ObjUtilidades.ConsultarMaestro("TiposIdentificacion");
        MaterialBetterSpinnerTipoIdentificacion = findViewById(R.id.MaterialBetterSpinnerTipoIdentificacion);
        ArrayAdapter<String> AdapterTipoIdentificacion = new ArrayAdapter<String>(RegistrarseActivity.this, android.R.layout.simple_dropdown_item_1line, lstTiposIdentificacion);
        MaterialBetterSpinnerTipoIdentificacion.setAdapter(AdapterTipoIdentificacion);

        ArrayList<String> lstCategorias = new ArrayList<>();
        lstCategorias.add("Arte y Diseño");
        lstCategorias.add("Vehiculos");
        lstCategorias.add("Belleza");
        lstCategorias.add("Deportes");
        lstCategorias.add("Educación");
        lstCategorias.add("Entretenimiento");
        lstCategorias.add("Servicios Tecnícos");
        lstCategorias.add("Medicina");
        MaterialBetterSpinnerCategoria = findViewById(R.id.MaterialBetterSpinnerCategoria);
        ArrayAdapter<String> AdapterCategorias = new ArrayAdapter<String>(RegistrarseActivity.this, android.R.layout.simple_dropdown_item_1line, lstCategorias);
        MaterialBetterSpinnerCategoria.setAdapter(AdapterCategorias);

        lstGeneros = ObjUtilidades.ConsultarMaestro("Generos");
        MaterialBetterSpinnerGenero = findViewById(R.id.MaterialBetterSpinnerGenero);
        ArrayAdapter<String> AdapterGenero = new ArrayAdapter<String>(RegistrarseActivity.this, android.R.layout.simple_dropdown_item_1line, lstGeneros);
        MaterialBetterSpinnerGenero.setAdapter(AdapterGenero);
    }

    public void RegistrarUsuario(View v) {
        try {
            Usuario gObjUsuario = new Usuario();
            SimpleDateFormat ObjSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

            String sToken = UUID.randomUUID().toString().toUpperCase();
            String sTipoIdentificacion = MaterialBetterSpinnerTipoIdentificacion.getText() != null ? MaterialBetterSpinnerTipoIdentificacion.getText().toString() : "";
            String sIdentificacion = EditTextIdentificacion.getText() != null ? EditTextIdentificacion.getText().toString() : "";
            String sNombreCompleto = EditTextNombreCompleto.getText() != null ? EditTextNombreCompleto.getText().toString() : "";
            String sPrimerNombre = sNombreCompleto.split(" ")[0] != null ? sNombreCompleto.split(" ")[0] : "";
            String sSegundoNombre = sNombreCompleto.split(" ")[1] != null ? sNombreCompleto.split(" ")[1] : "";
            String sPrimerApellido = sNombreCompleto.split(" ")[2] != null ? sNombreCompleto.split(" ")[2] : "";
            String sSegundoApellido = sNombreCompleto.split(" ")[3] != null ? sNombreCompleto.split(" ")[3] : "";
            String sFechaNacimiento = EditTextFechaNacimiento.getText() != null ? EditTextFechaNacimiento.getText().toString() : "";
            String sCategoria = MaterialBetterSpinnerCategoria.getText() != null ? MaterialBetterSpinnerCategoria.getText().toString() : "";
            String sGenero = MaterialBetterSpinnerGenero.getText() != null ? MaterialBetterSpinnerGenero.getText().toString() : "";
            String sCelular = MaterialBetterSpinnerGenero.getText() != null ? MaterialBetterSpinnerGenero.getText().toString() : "";
            String sCorreoElectronico = EditTextCorreoElectronico.getText() != null ? EditTextCorreoElectronico.getText().toString() : "";
            String sEstado = "Disponible";
            boolean bActivo = true;
            String sContrasena = EditTextContrasena.getText() != null ? EditTextContrasena.getText().toString() : "";
            String sContrasenaConfirmada = EditTextConfirmarContrasena.getText() != null ? EditTextConfirmarContrasena.getText().toString() : "";

            if (!sNombreCompleto.equals("") && !sCorreoElectronico.equals("") && !sContrasena.equals("") && !sContrasenaConfirmada.equals("")) {
                if (sContrasenaConfirmada == sContrasena) {
                    /*gObjUsuario.sCodUsuarioAplicacion = sToken;
                    gObjUsuario.sTipoIdentificacion = sTipoIdentificacion;
                    gObjUsuario.sIdentificacion = sIdentificacion;
                    gObjUsuario.sPrimerNombre = sPrimerNombre;
                    gObjUsuario.sSegundoNombre = sSegundoNombre;
                    gObjUsuario.sPrimerApellido = sPrimerApellido;
                    gObjUsuario.sSegundoApellido = sSegundoApellido;
                    gObjUsuario.dFechaNacimiento = ObjSimpleDateFormat.parse(sFechaNacimiento);
                    gObjUsuario.iEdad = Integer.parseInt(ObjUtilidades.ObtenerEdad(gObjUsuario.dFechaNacimiento));
                    gObjUsuario.sGenero = sGenero;
                    gObjUsuario.sCelular = sCelular;
                    gObjUsuario.sEmail = sCorreoElectronico;
                    gObjUsuario.bActivo = bActivo;
                    gObjUsuario.sContrasena = sContrasena;*/

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

    private void CargarImagenPerfil() {
        final CharSequence[] opciones = {"Tomar Foto", "Cargar Foto", "Cancelar"};
        final AlertDialog.Builder AlertOpciones = new AlertDialog.Builder(RegistrarseActivity.this);
        AlertOpciones.setTitle("Seleccione una opción");
        AlertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")) {
                    Intent ObjIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (ObjIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(ObjIntent, iCodigoFotoTomada);
                    }
                } else {
                    if (opciones[i].equals("Cargar Foto")) {
                        Intent ObjPictureIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        ObjPictureIntent.setType("image/");
                        startActivityForResult(Intent.createChooser(ObjPictureIntent, "Seleccione la aplicación"), iCodigoFotoSeleccionada);
                    } else {
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        AlertOpciones.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case iCodigoFotoSeleccionada:
                    Uri PathFotoSeleccionada = data.getData();
                    ImageViewFotoPerfil.setImageURI(PathFotoSeleccionada);
                    break;
                case iCodigoFotoTomada:
                    Bundle extras = data.getExtras();
                    Bitmap bitmap = (Bitmap) extras.get("data");
                    ImageViewFotoPerfil.setImageBitmap(bitmap);
                    break;
            }
        }
    }

    private void obtenerFecha(){
        DatePickerDialog recogerFecha = new DatePickerDialog(this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10) ? CERO + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10) ? CERO + String.valueOf(mesActual) : String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                EditTextFechaNacimiento.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);
            }
        },anio, mes, dia);
        recogerFecha.show();
    }
}
