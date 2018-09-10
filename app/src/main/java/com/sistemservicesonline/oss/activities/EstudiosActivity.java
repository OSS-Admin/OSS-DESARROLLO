package com.sistemservicesonline.oss.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.sistemservicesonline.oss.R;
import com.sistemservicesonline.oss.appcode.Estudio;
import com.sistemservicesonline.oss.appcode.ExperienciaLaboral;
import com.sistemservicesonline.oss.appcode.Maestros;
import com.sistemservicesonline.oss.interfaces.ApiService;
import com.sistemservicesonline.oss.services.APIServiceClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EstudiosActivity extends AppCompatActivity {

    ProgressDialog
            progressDialog;

    ImageView
            ImageViewEliminar
            , ImageViewCambiarFechaInicial
            , ImageViewCambiarFechaFinal;

    MaterialEditText
            MaterialEditTextInstitucion
            , MaterialEditTextFechaInicial
            , MaterialEditTextFechaFinal
            , MaterialEditTextDescripcion;

    CheckBox
            CheckBoxActualmente;

    Button
            ButtonGuardarEstudio;

    private String gsToken = "";
    private String gsCodigo = "";
    private String gsInstitucion = "";
    private String gsFechaInicial = "";
    private String gsFechaFinal = "";
    private Boolean gbActualmente = false;
    private String gsDescripcion = "";

    private static final String CERO = "0";
    private static final String BARRA = "-";

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);

    List<Estudio> LstEstudios = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estudios);

        gsToken = getIntent().getExtras().getString("Token") != null ? getIntent().getExtras().getString("Token").toString() : "";
        gsCodigo = getIntent().getExtras().getString("Codigo") != null ? getIntent().getExtras().getString("Codigo").toString() : "";

        InicializarControles();

        if (!gsCodigo.isEmpty()) {
            progressDialog.show();
            GestionEstudios("Consultar");
        } else {
            ImageViewEliminar.setVisibility(View.GONE);
        }
    }

    private void InicializarControles() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_atras));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        progressDialog = new ProgressDialog(EstudiosActivity.this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        MaterialEditTextInstitucion = findViewById(R.id.MaterialEditTextInstitucion);
        MaterialEditTextFechaInicial = findViewById(R.id.MaterialEditTextFechaInicial);
        MaterialEditTextFechaFinal = findViewById(R.id.MaterialEditTextFechaFinal);
        MaterialEditTextDescripcion = findViewById(R.id.MaterialEditTextDescripcion);

        ImageViewCambiarFechaInicial = findViewById(R.id.ImageViewCambiarFechaInicial);
        ImageViewCambiarFechaInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerFechaInicial();
            }
        });
        ImageViewCambiarFechaFinal = findViewById(R.id.ImageViewCambiarFechaFinal);
        ImageViewCambiarFechaFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerFechaFinal();
            }
        });

        ImageViewEliminar = findViewById(R.id.ImageViewEliminar);
        ImageViewEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                GestionEstudios("Eliminar");
            }
        });

        CheckBoxActualmente = findViewById(R.id.CheckBoxActualmente);

        ButtonGuardarEstudio = findViewById(R.id.ButtonGuardarEstudio);
        ButtonGuardarEstudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gsCodigo != "") {
                    progressDialog.show();
                    GestionEstudios("Editar");
                } else {
                    progressDialog.show();
                    GestionEstudios("Nuevo");
                }
            }
        });
    }

    private void GestionEstudios (String sAccion) {
        try {
            gsInstitucion = MaterialEditTextInstitucion.getText() != null ? MaterialEditTextInstitucion.getText().toString() : "";
            gsFechaInicial = MaterialEditTextFechaInicial.getText() != null ? MaterialEditTextFechaInicial.getText().toString() : "";
            gsFechaFinal = MaterialEditTextFechaFinal.getText() != null ? MaterialEditTextFechaFinal.getText().toString() : "";
            gbActualmente = CheckBoxActualmente.isChecked();
            gsDescripcion = MaterialEditTextDescripcion.getText() != null ? MaterialEditTextDescripcion.getText().toString() : "";

            ApiService apiService = APIServiceClient.getClient().create(ApiService.class);
            Call call = null;

            if (!gsToken.isEmpty() && !gsInstitucion.isEmpty() && !gsFechaInicial.isEmpty() && !gsDescripcion.isEmpty() || sAccion.equals("Consultar")) {
                if (!gbActualmente && gsFechaFinal.equals("") && !sAccion.equals("Consultar")) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Existen campos por diligenciar, por favor verifique", Toast.LENGTH_SHORT).show();
                    return;
                }

                Estudio ObjEstudio  = new Estudio();
                ObjEstudio.setCodigo(gsCodigo);
                ObjEstudio.setInstitucion(gsInstitucion);
                ObjEstudio.setFechaInicial(gsFechaInicial);
                ObjEstudio.setFechaFinal(gsFechaFinal);
                ObjEstudio.setActualmente(gbActualmente);
                ObjEstudio.setDescripcion(gsDescripcion);
                ObjEstudio.setCodigoUsuario(gsToken);

                switch (sAccion) {
                    case "Consultar" :
                        call = apiService.ConsultarEstudio(gsCodigo);
                        call.enqueue(new Callback() {
                            @Override
                            public void onResponse(Call call, Response response) {
                                if (response.isSuccessful()) {
                                    LstEstudios = (List<Estudio>) response.body();
                                    if (LstEstudios.size() > 0) {
                                        for (int i = 0; i < LstEstudios.size(); i++) {
                                            MaterialEditTextInstitucion.setText(LstEstudios.get(i) != null ? LstEstudios.get(i).getInstitucion() : "");
                                            MaterialEditTextFechaInicial.setText(LstEstudios.get(i) != null ? LstEstudios.get(i).getFechaInicial() : "");
                                            gbActualmente = LstEstudios.get(i) != null ? LstEstudios.get(i).getActualmente() : false;
                                            MaterialEditTextDescripcion.setText(LstEstudios.get(i) != null ? LstEstudios.get(i).getDescripcion() : "");
                                            if (gbActualmente) {
                                                CheckBoxActualmente.setChecked(true);
                                                MaterialEditTextFechaFinal.setText("");
                                            } else {
                                                MaterialEditTextFechaFinal.setText(LstEstudios.get(i) != null ? LstEstudios.get(i).getFechaFinal() : "");
                                                CheckBoxActualmente.setChecked(false);
                                            }
                                        }
                                    }
                                    progressDialog.dismiss();
                                } else {
                                    progressDialog.dismiss();
                                    Log.i("", response.message().toString());
                                }
                            }

                            @Override
                            public void onFailure(Call call, Throwable t) {
                                progressDialog.dismiss();
                                Log.i("", t.toString());
                                Toast.makeText(getApplicationContext(), "Por favor verifica tu conexión a internet.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case "Nuevo":
                        call = apiService.RegistrarEstudio(ObjEstudio);
                        call.enqueue(new Callback() {
                            @Override
                            public void onResponse(Call call, Response response) {
                                if (response.isSuccessful()) {
                                    Intent ObjIntent = new Intent (EstudiosActivity.this, EditarPerfilActivity.class);
                                    ObjIntent.putExtra("Token", gsToken);
                                    startActivity(ObjIntent);
                                    finish();
                                } else {
                                    progressDialog.dismiss();
                                    Log.i("", response.message().toString());
                                }
                            }

                            @Override
                            public void onFailure(Call call, Throwable t) {
                                progressDialog.dismiss();
                                Log.i("", t.toString());
                                Toast.makeText(getApplicationContext(), "Por favor verifica tu conexión a internet.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case "Editar":
                        if (!gsCodigo.isEmpty()) {
                            call = apiService.ActualizarEstudio(gsCodigo, ObjEstudio);
                            call.enqueue(new Callback() {
                                @Override
                                public void onResponse(Call call, Response response) {
                                    if (response.isSuccessful()) {
                                        Intent ObjIntent = new Intent (EstudiosActivity.this, EditarPerfilActivity.class);
                                        ObjIntent.putExtra("Token", gsToken);
                                        startActivity(ObjIntent);
                                        finish();
                                    } else {
                                        progressDialog.dismiss();
                                        Log.i("", response.message().toString());
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
                        break;
                    case "Eliminar":
                        if (!gsCodigo.isEmpty()) {
                            call = apiService.EliminarEstudio(gsCodigo);
                            call.enqueue(new Callback() {
                                @Override
                                public void onResponse(Call call, Response response) {
                                    if (response.isSuccessful()) {
                                        Intent ObjIntent = new Intent (EstudiosActivity.this, EditarPerfilActivity.class);
                                        ObjIntent.putExtra("Token", gsToken);
                                        startActivity(ObjIntent);
                                        finish();
                                    } else {
                                        progressDialog.dismiss();
                                        Log.i("", response.message().toString());
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
                        break;
                }
            } else {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Existen campos por diligenciar, por favor verifique", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void obtenerFechaInicial(){
        DatePickerDialog recogerFecha = new DatePickerDialog(this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final int mesActual = month + 1;
                String diaFormateado = (dayOfMonth < 10) ? CERO + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                String mesFormateado = (mesActual < 10) ? CERO + String.valueOf(mesActual) : String.valueOf(mesActual);
                MaterialEditTextFechaInicial.setText(year + BARRA + mesFormateado + BARRA + diaFormateado);
            }
        },anio, mes, dia);
        recogerFecha.show();
    }

    private void obtenerFechaFinal(){
        DatePickerDialog recogerFecha = new DatePickerDialog(this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final int mesActual = month + 1;
                String diaFormateado = (dayOfMonth < 10) ? CERO + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                String mesFormateado = (mesActual < 10) ? CERO + String.valueOf(mesActual) : String.valueOf(mesActual);
                MaterialEditTextFechaFinal.setText(year + BARRA + mesFormateado + BARRA + diaFormateado);
            }
        },anio, mes, dia);
        recogerFecha.show();
    }

    @Override
    public void onBackPressed() {
        Intent ObjIntent = new Intent(EstudiosActivity.this, EditarPerfilActivity.class);
        ObjIntent.putExtra("Token", gsToken);
        startActivity(ObjIntent);
        finish();
    }
}
