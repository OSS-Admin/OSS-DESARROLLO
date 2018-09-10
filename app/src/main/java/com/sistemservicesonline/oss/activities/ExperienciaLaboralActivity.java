package com.sistemservicesonline.oss.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.sistemservicesonline.oss.R;
import com.sistemservicesonline.oss.appcode.ExperienciaLaboral;
import com.sistemservicesonline.oss.appcode.Maestros;
import com.sistemservicesonline.oss.appcode.PerfilProfesional;
import com.sistemservicesonline.oss.interfaces.ApiService;
import com.sistemservicesonline.oss.services.APIServiceClient;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExperienciaLaboralActivity extends AppCompatActivity {

    ProgressDialog
            progressDialog;

    TextView
            TextViewCodigoPerfilProfesional;

    ImageView
            ImageViewEliminar
            , ImageViewCambiarFechaInicial
            , ImageViewCambiarFechaFinal;

    MaterialEditText
            EditTextEmpresa
            , EditTextCargo
            , EditTextFechaInicial
            , EditTextFechaFinal
            , EditTextDescripcion;

    MaterialBetterSpinner
            MaterialBetterSpinnerDepartamento
            , MaterialBetterSpinnerCiudad;

    CheckBox
            CheckBoxActualmente;

    Button
            ButtonGuardarExerienciaProfesional;

    private String gsToken = "";
    private String gsCodigo = "";
    private String gsEmpresa = "";
    private String gsDepartamento = "";
    private String gsCiudad = "";
    private String gsCargo = "";
    private String gsFechaInicial = "";
    private String gsFechaFinal = "";
    private Boolean gbActualmente = false;
    private String gsDescripcion = "";

    private static final String CERO = "0";
    private static final String BARRA = "-";

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();
    SimpleDateFormat ObjSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);

    List<String> LstCiudades = new ArrayList<>();
    List<Maestros> LstMaestros = new ArrayList<>();
    List<String> LstDepartamentos = new ArrayList<>();
    List<String> LstMaestrosCodigos = new ArrayList<>();
    List<ExperienciaLaboral> LstExperienciaLaboral = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiencia_laboral);

        gsToken = getIntent().getExtras().getString("Token") != null ? getIntent().getExtras().getString("Token").toString() : "";
        gsCodigo = getIntent().getExtras().getString("Codigo") != null ? getIntent().getExtras().getString("Codigo").toString() : "";

        InicializarControles();
        ObtenerMaestros();

        if (!gsCodigo.isEmpty()) {
            progressDialog.show();
            GestionExperienciaLaboral("Consultar");
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

        progressDialog = new ProgressDialog(ExperienciaLaboralActivity.this);
        progressDialog.setMessage("Cargando...");
        progressDialog.show();
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        TextViewCodigoPerfilProfesional = findViewById(R.id.TextViewCodigoPerfilProfesional);

        MaterialBetterSpinnerDepartamento = findViewById(R.id.MaterialBetterSpinnerDepartamento);
        MaterialBetterSpinnerCiudad = findViewById(R.id.MaterialBetterSpinnerCiudad);

        EditTextEmpresa = findViewById(R.id.EditTextEmpresa);
        EditTextCargo = findViewById(R.id.EditTextCargo);
        EditTextFechaInicial = findViewById(R.id.EditTextFechaInicial);
        EditTextFechaFinal = findViewById(R.id.EditTextFechaFinal);
        EditTextDescripcion = findViewById(R.id.EditTextDescripcion);

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
                GestionExperienciaLaboral("Eliminar");
            }
        });

        CheckBoxActualmente = findViewById(R.id.CheckBoxActualmente);

        ButtonGuardarExerienciaProfesional = findViewById(R.id.ButtonGuardarExerienciaProfesional);
        ButtonGuardarExerienciaProfesional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gsCodigo != "") {
                    progressDialog.show();
                    GestionExperienciaLaboral("Editar");
                } else {
                    progressDialog.show();
                    GestionExperienciaLaboral("Nuevo");
                }
            }
        });
    }

    private void ObtenerMaestros () {
        try {
            ApiService apiService = APIServiceClient.getClient().create(ApiService.class);
            Call call = apiService.ConsultarMaestros();
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        LstMaestros = (List<Maestros>) response.body();
                        if (LstMaestros.size() > 0) {
                            for (int i = 0; i < LstMaestros.size(); i++) {
                                LstMaestrosCodigos.add(LstMaestros.get(i).getCodigo() != null && LstMaestros.get(i).getNombre() != null ? LstMaestros.get(i).getCodigo().toString() + "-" + LstMaestros.get(i).getNombre().toString() : "");
                                if (LstMaestros.get(i).getTipo().equals("Departamentos")) {
                                    LstDepartamentos.add(LstMaestros.get(i).getNombre() != null ? LstMaestros.get(i).getNombre().toString() : "");
                                } else if (LstMaestros.get(i).getTipo().equals("Ciudades")) {
                                    LstCiudades.add(LstMaestros.get(i).getNombre() != null ? LstMaestros.get(i).getNombre().toString() : "");
                                }
                            }

                            ArrayAdapter<String> AdapterDepartamentos = new ArrayAdapter<String>(ExperienciaLaboralActivity.this, android.R.layout.simple_dropdown_item_1line, LstDepartamentos);
                            MaterialBetterSpinnerDepartamento.setAdapter(AdapterDepartamentos);
                            ArrayAdapter<String> AdapterCiudades = new ArrayAdapter<String>(ExperienciaLaboralActivity.this, android.R.layout.simple_dropdown_item_1line, LstCiudades);
                            MaterialBetterSpinnerCiudad.setAdapter(AdapterCiudades);
                        }
                        progressDialog.dismiss();
                    }
                }
                @Override
                public void onFailure(Call call, Throwable t) {
                    progressDialog.dismiss();
                    Log.i("", t.toString());
                    Toast.makeText(getApplicationContext(), "Por favor verifica tu conexión a internet.", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void GestionExperienciaLaboral (String sAccion) {
        try {
            gsEmpresa = EditTextEmpresa.getText() != null ? EditTextEmpresa.getText().toString() : "";
            gsDepartamento = ObtenerCodigoMaestros(LstMaestrosCodigos, MaterialBetterSpinnerDepartamento.getText() != null ? MaterialBetterSpinnerDepartamento.getText().toString()  : "");
            gsCiudad = ObtenerCodigoMaestros(LstMaestrosCodigos, MaterialBetterSpinnerCiudad.getText() != null ? MaterialBetterSpinnerCiudad.getText().toString()  : "");
            gsCargo = EditTextCargo.getText() != null ? EditTextCargo.getText().toString() : "";
            gsFechaInicial = EditTextFechaInicial.getText() != null ? EditTextFechaInicial.getText().toString() : "";
            gsFechaFinal = EditTextFechaFinal.getText() != null ? EditTextFechaFinal.getText().toString() : "";
            gbActualmente = CheckBoxActualmente.isChecked();
            gsDescripcion = EditTextDescripcion.getText() != null ? EditTextDescripcion.getText().toString() : "";

            ApiService apiService = APIServiceClient.getClient().create(ApiService.class);
            Call call = null;

            if (!gsToken.isEmpty() && !gsEmpresa.isEmpty() && !gsDepartamento.isEmpty() && !gsCiudad.isEmpty() && !gsCargo.isEmpty() && !gsFechaInicial.isEmpty() || sAccion.equals("Consultar")) {
                if (!gbActualmente && gsFechaFinal.equals("") && !sAccion.equals("Consultar")) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Existen campos por diligenciar, por favor verifique", Toast.LENGTH_SHORT).show();
                    return;
                }

                ExperienciaLaboral ObjExperienciaLaboral  = new ExperienciaLaboral();
                ObjExperienciaLaboral.setCodigo(gsCodigo);
                ObjExperienciaLaboral.setEmpresa(gsEmpresa);
                ObjExperienciaLaboral.setDepartamento(gsDepartamento);
                ObjExperienciaLaboral.setCiudad(gsCiudad);
                ObjExperienciaLaboral.setCargo(gsCargo);
                ObjExperienciaLaboral.setFechaInicial(gsFechaInicial);
                ObjExperienciaLaboral.setFechaFinal(gsFechaFinal);
                ObjExperienciaLaboral.setActualmente(gbActualmente);
                ObjExperienciaLaboral.setDescripcion(gsDescripcion);
                ObjExperienciaLaboral.setCodigoUsuario(gsToken);

                switch (sAccion) {
                    case "Consultar" :
                        call = apiService.ConsultarExperienciaLaboral(gsCodigo);
                        call.enqueue(new Callback() {
                            @Override
                            public void onResponse(Call call, Response response) {
                                if (response.isSuccessful()) {
                                    LstExperienciaLaboral = (List<ExperienciaLaboral>) response.body();
                                    if (LstExperienciaLaboral.size() > 0) {
                                        for (int i = 0; i < LstExperienciaLaboral.size(); i++) {
                                            EditTextEmpresa.setText(LstExperienciaLaboral.get(i) != null ? LstExperienciaLaboral.get(i).getEmpresa() : "");
                                            MaterialBetterSpinnerDepartamento.setText(LstExperienciaLaboral.get(i) != null ? LstExperienciaLaboral.get(i).getDepartamento() : "");
                                            MaterialBetterSpinnerCiudad.setText(LstExperienciaLaboral.get(i) != null ? LstExperienciaLaboral.get(i).getCiudad() : "");
                                            MaterialBetterSpinnerCiudad.setText(LstExperienciaLaboral.get(i) != null ? LstExperienciaLaboral.get(i).getCiudad() : "");
                                            EditTextCargo.setText(LstExperienciaLaboral.get(i) != null ? LstExperienciaLaboral.get(i).getCargo() : "");
                                            EditTextFechaInicial.setText(LstExperienciaLaboral.get(i) != null ? LstExperienciaLaboral.get(i).getFechaInicial() : "");
                                            gbActualmente = LstExperienciaLaboral.get(i) != null ? LstExperienciaLaboral.get(i).getActualmente() : false;
                                            EditTextDescripcion.setText(LstExperienciaLaboral.get(i) != null ? LstExperienciaLaboral.get(i).getDescripcion() : "");
                                            if (gbActualmente) {
                                                CheckBoxActualmente.setChecked(true);
                                                EditTextFechaFinal.setText("");
                                            } else {
                                                EditTextFechaFinal.setText(LstExperienciaLaboral.get(i) != null ? LstExperienciaLaboral.get(i).getFechaFinal() : "");
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
                        call = apiService.RegistrarExperienciaLaboral(ObjExperienciaLaboral);
                        call.enqueue(new Callback() {
                            @Override
                            public void onResponse(Call call, Response response) {
                                if (response.isSuccessful()) {
                                    Intent ObjIntent = new Intent (ExperienciaLaboralActivity.this, EditarPerfilActivity.class);
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
                            call = apiService.ActualizarExperienciaLaboral(gsCodigo, ObjExperienciaLaboral);
                            call.enqueue(new Callback() {
                                @Override
                                public void onResponse(Call call, Response response) {
                                    if (response.isSuccessful()) {
                                        Intent ObjIntent = new Intent (ExperienciaLaboralActivity.this, EditarPerfilActivity.class);
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
                            call = apiService.EliminarExperienciaLaboral(gsCodigo);
                            call.enqueue(new Callback() {
                                @Override
                                public void onResponse(Call call, Response response) {
                                    if (response.isSuccessful()) {
                                        Intent ObjIntent = new Intent (ExperienciaLaboralActivity.this, EditarPerfilActivity.class);
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

    private static String ObtenerCodigoMaestros (List<String> LstMaestrosCodigos, String sNombreMaestro) {
        try {
            if (!LstMaestrosCodigos.equals("") && !sNombreMaestro.equals("")){
                for (int i = 0; i < LstMaestrosCodigos.size(); i++) {
                    String sMaestro = LstMaestrosCodigos.get(i) != null ? LstMaestrosCodigos.get(i).toString() : "";
                    String sNombre = sMaestro.split("-")[1] != null ? sMaestro.split("-")[1].toString() : "";
                    if (sNombreMaestro.equals(sNombre)) {
                        String sCodigo = sMaestro.split("-")[0] != null ? sMaestro.split("-")[0].toString() : "";
                        return sCodigo;
                    }
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return "";
    }

    private void obtenerFechaInicial(){
        DatePickerDialog recogerFecha = new DatePickerDialog(this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final int mesActual = month + 1;
                String diaFormateado = (dayOfMonth < 10) ? CERO + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                String mesFormateado = (mesActual < 10) ? CERO + String.valueOf(mesActual) : String.valueOf(mesActual);
                EditTextFechaInicial.setText(year + BARRA + mesFormateado + BARRA + diaFormateado);
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
                EditTextFechaFinal.setText(year + BARRA + mesFormateado + BARRA + diaFormateado);
            }
        },anio, mes, dia);
        recogerFecha.show();
    }

    @Override
    public void onBackPressed() {
        Intent ObjIntent = new Intent(ExperienciaLaboralActivity.this, EditarPerfilActivity.class);
        ObjIntent.putExtra("Token", gsToken);
        startActivity(ObjIntent);
        finish();
    }
}
