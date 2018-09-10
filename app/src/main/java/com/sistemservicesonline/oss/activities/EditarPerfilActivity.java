package com.sistemservicesonline.oss.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.sistemservicesonline.oss.R;
import com.sistemservicesonline.oss.adapters.EstudioAdapter;
import com.sistemservicesonline.oss.adapters.ExperienciaLaboralAdapter;
import com.sistemservicesonline.oss.adapters.PerfilProfesionalAdapter;
import com.sistemservicesonline.oss.appcode.Estudio;
import com.sistemservicesonline.oss.appcode.ExperienciaLaboral;
import com.sistemservicesonline.oss.appcode.Maestros;
import com.sistemservicesonline.oss.appcode.PerfilProfesional;
import com.sistemservicesonline.oss.appcode.Usuario;
import com.sistemservicesonline.oss.interfaces.ApiService;
import com.sistemservicesonline.oss.services.APIServiceClient;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarPerfilActivity extends AppCompatActivity {

    MaterialEditText
            EditTextNombreCompleto
            , EditTextIdentificacion
            , EditTextFechaNacimiento
            , EditTextEdad
            , EditTextCelular
            , EditTextTelefono
            , EditTextDireccion
            , EditTextNombreUsuario
            , EditTextCorreoElectronico;

    MaterialBetterSpinner
              SpinnerTipoIdentificacion
            , SpinnerCategoria
            , SpinnerGenero
            , SpinnerDepartamento
            , SpinnerCiudad
            , SpinnerEstado;

    Button
              ButtonAgregarPerfilProfesional
            , ButtonAgregarExperienciaLaboral
            , ButtonAgregarEstudio;

    TextView
              TextViewCambiarFoto;

    ImageView
              ImageViewFotoPerfil
            , ImageViewGuardar
            , ImageViewCambiarFechaNacimiento;

    SwipeRefreshLayout
            swipeRefreshLayout;

    ProgressDialog
            progressDialog;

    RecyclerView
            ReciclerViewPerfilProfesional
            , ReciclerViewExperienciasProfesionaes
            , ReciclerViewEstudio;

    private PerfilProfesionalAdapter PerfilProfesionalAdapter;
    private ExperienciaLaboralAdapter ExperienciaLaboralAdapter;
    private EstudioAdapter EstudioAdapter;

    private String gsToken = "";
    private String sTokenInvitado = "";
    private static final String CERO = "0";
    private static final String BARRA = "-";

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();
    SimpleDateFormat ObjSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    final int iCodigoFotoSeleccionada = 10;
    final int iCodigoFotoTomada = 20;

    List<Usuario> LstUsuario = new ArrayList<>();
    List<Maestros> LstMaestros = new ArrayList<>();
    List<String> LstMaestrosCodigos = new ArrayList<>();
    List<String> LstTiposIdentificacion = new ArrayList<>();
    List<String> LstCategorias = new ArrayList<>();
    List<String> LstGeneros = new ArrayList<>();
    List<String> LstDepartamentos = new ArrayList<>();
    List<String> LstCiudades = new ArrayList<>();
    List<String> LstEstados = new ArrayList<>();
    List<PerfilProfesional> LstPerfilesProfesionales = new ArrayList<>();
    List<ExperienciaLaboral> LstExperienciasLaborales = new ArrayList<>();
    List<Estudio> LstEstudios = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        gsToken = getIntent().getExtras().getString("Token") != null ? getIntent().getExtras().getString("Token").toString() : "";
        sTokenInvitado = getIntent().getExtras().getString("TokenInvitado") != null ? getIntent().getExtras().getString("TokenInvitado").toString() : "";

        InicializarControles();
        ObtenerMaestros();

        if (!sTokenInvitado.equals("")) {
            ImageViewGuardar.setVisibility(View.GONE);
            TextViewCambiarFoto.setVisibility(View.GONE);
            ObtenerUsuario();
        } else {
            ObtenerUsuario();
        }
    }

    private void InicializarControles() {
        try {
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

            swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeRefreshLayout.setRefreshing(false);
                }
            });

            progressDialog = new ProgressDialog(EditarPerfilActivity.this);
            progressDialog.setMessage("Cargando...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            progressDialog.setCancelable(false);

            /*Inicio TextView*/
            TextViewCambiarFoto = findViewById(R.id.TextViewCambiarFoto);
            TextViewCambiarFoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CargarImagenPerfil();
                }
            });
            /*Fin TextView*/

            /*Inicio MaterialEditText*/
            EditTextNombreCompleto = findViewById(R.id.EditTextNombreCompleto);
            EditTextIdentificacion = findViewById(R.id.EditTextIdentificacion);
            EditTextFechaNacimiento = findViewById(R.id.EditTextFechaNacimiento);
            EditTextEdad = findViewById(R.id.EditTextEdad);
            EditTextCelular = findViewById(R.id.EditTextCelular);
            EditTextTelefono = findViewById(R.id.EditTextTelefono);
            EditTextDireccion = findViewById(R.id.EditTextDireccion);
            EditTextNombreUsuario = findViewById(R.id.EditTextNombreUsuario);
            EditTextCorreoElectronico = findViewById(R.id.EditTextCorreoElectronico);
            /*Fin MaterialEditText*/

            ImageViewFotoPerfil = findViewById(R.id.ImageViewFotoPerfil);
            ImageViewCambiarFechaNacimiento = findViewById(R.id.ImageViewCambiarFechaNacimiento);
            ImageViewCambiarFechaNacimiento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    obtenerFecha();
                }
            });

            /*Inicio ImageView Controls*/
            ImageViewGuardar = findViewById(R.id.ImageViewGuardar);
            ImageViewGuardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressDialog.show();
                    ActualizarUsuario();
                }
            });
            /*Fin ImageView Controls*/

            /*Inicio MaterialBetterSpinner*/
            SpinnerTipoIdentificacion = findViewById(R.id.spinner_TipoIdentificacion);
            SpinnerCategoria = findViewById(R.id.spinner_Categoria);
            SpinnerGenero = findViewById(R.id.spinner_Genero);
            SpinnerDepartamento = findViewById(R.id.spinner_Departamento);
            SpinnerCiudad = findViewById(R.id.spinner_Ciudad);
            SpinnerEstado = findViewById(R.id.spinner_Estado);
            /*Fin MaterialBetterSpinner*/

            /*Inicio Buttons*/
            ButtonAgregarPerfilProfesional = findViewById(R.id.ButtonAgregarPerfilProfesional);
            ButtonAgregarPerfilProfesional.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent ObjIntent = new Intent(EditarPerfilActivity.this, PerfilProfesionalActivity.class);
                    ObjIntent.putExtra("Token", gsToken);
                    startActivity(ObjIntent);
                }
            });
            ButtonAgregarExperienciaLaboral = findViewById(R.id.ButtonAgregarExperienciaLaboral);
            ButtonAgregarExperienciaLaboral.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent ObjIntent = new Intent(EditarPerfilActivity.this, ExperienciaLaboralActivity.class);
                    ObjIntent.putExtra("Token", gsToken);
                    startActivity(ObjIntent);
                }
            });
            ButtonAgregarEstudio = findViewById(R.id.ButtonAgregarEstudio);
            ButtonAgregarEstudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent ObjIntent = new Intent(EditarPerfilActivity.this, EstudiosActivity.class);
                    ObjIntent.putExtra("Token", gsToken);
                    startActivity(ObjIntent);
                }
            });
            /*Fin Buttons*/

            ReciclerViewPerfilProfesional = findViewById(R.id.ReciclerViewPerfilProfesional);
            ReciclerViewExperienciasProfesionaes = findViewById(R.id.ReciclerViewExperienciasProfesionaes);
            ReciclerViewEstudio = findViewById(R.id.ReciclerViewEstudio);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
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
                                if (LstMaestros.get(i).getTipo().equals("TipoIdentificacion")) {
                                    LstTiposIdentificacion.add(LstMaestros.get(i).getNombre() != null ? LstMaestros.get(i).getNombre().toString() : "");
                                } else if (LstMaestros.get(i).getTipo().equals("Categorias")) {
                                    LstCategorias.add(LstMaestros.get(i).getNombre() != null ? LstMaestros.get(i).getNombre().toString() : "");
                                } else if (LstMaestros.get(i).getTipo().equals("Generos")) {
                                    LstGeneros.add(LstMaestros.get(i).getNombre() != null ? LstMaestros.get(i).getNombre().toString() : "");
                                } else if (LstMaestros.get(i).getTipo().equals("Departamentos")) {
                                    LstDepartamentos.add(LstMaestros.get(i).getNombre() != null ? LstMaestros.get(i).getNombre().toString() : "");
                                } else if (LstMaestros.get(i).getTipo().equals("Ciudades")) {
                                    LstCiudades.add(LstMaestros.get(i).getNombre() != null ? LstMaestros.get(i).getNombre().toString() : "");
                                } else if (LstMaestros.get(i).getTipo().equals("Estados")) {
                                    LstEstados.add(LstMaestros.get(i).getNombre() != null ? LstMaestros.get(i).getNombre().toString() : "");
                                }
                            }
                            ArrayAdapter<String> AdapterTipoIdentificacion = new ArrayAdapter<String>(EditarPerfilActivity.this, android.R.layout.simple_dropdown_item_1line, LstTiposIdentificacion);
                            SpinnerTipoIdentificacion.setAdapter(AdapterTipoIdentificacion);
                            ArrayAdapter<String> AdapterCategorias = new ArrayAdapter<String>(EditarPerfilActivity.this, android.R.layout.simple_dropdown_item_1line, LstCategorias);
                            SpinnerCategoria.setAdapter(AdapterCategorias);
                            ArrayAdapter<String> AdapterGenero = new ArrayAdapter<String>(EditarPerfilActivity.this, android.R.layout.simple_dropdown_item_1line, LstGeneros);
                            SpinnerGenero.setAdapter(AdapterGenero);
                            ArrayAdapter<String> AdapterDepartamentos = new ArrayAdapter<String>(EditarPerfilActivity.this, android.R.layout.simple_dropdown_item_1line, LstDepartamentos);
                            SpinnerDepartamento.setAdapter(AdapterDepartamentos);
                            ArrayAdapter<String> AdapterCiudades = new ArrayAdapter<String>(EditarPerfilActivity.this, android.R.layout.simple_dropdown_item_1line, LstCiudades);
                            SpinnerCiudad.setAdapter(AdapterCiudades);
                            ArrayAdapter<String> AdapterEstados = new ArrayAdapter<String>(EditarPerfilActivity.this, android.R.layout.simple_dropdown_item_1line, LstEstados);
                            SpinnerEstado.setAdapter(AdapterEstados);
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
        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void ObtenerUsuario () {
        try {
            ApiService apiService = APIServiceClient.getClient().create(ApiService.class);
            Call call = apiService.ConsultarUsuario(gsToken);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        LstUsuario = (List<Usuario>) response.body();
                        if (LstUsuario.size() > 0) {
                            for (int i = 0; i < LstUsuario.size(); i++) {
                                String sPrimerNombre = LstUsuario.get(i).getPrimerNombre() != null ? LstUsuario.get(i).getPrimerNombre().toString() : "";
                                String sSegundoNombre = LstUsuario.get(i).getSegundoNombre() != null ? LstUsuario.get(i).getSegundoNombre().toString() : "";
                                String sPrimerApellido = LstUsuario.get(i).getPrimerApellido() != null ? LstUsuario.get(i).getPrimerApellido().toString() : "";
                                String sSegundoApellido = LstUsuario.get(i).getSegundoApellido() != null ? LstUsuario.get(i).getSegundoApellido().toString() : "";
                                String sNombreCompleto = sSegundoNombre != "" ? sPrimerNombre + " " + sSegundoNombre + " " + sPrimerApellido + " " + sSegundoApellido : sPrimerNombre + " " + sPrimerApellido + " " + sSegundoApellido;
                                EditTextNombreCompleto.setText(sNombreCompleto);
                                SpinnerTipoIdentificacion.setText(LstUsuario.get(i).getTipoIdentificacion() != null ? LstUsuario.get(i).getTipoIdentificacion().toString() : "");
                                EditTextIdentificacion.setText(LstUsuario.get(i).getIdentificacion() != null ? LstUsuario.get(i).getIdentificacion().toString() : "");
                                EditTextFechaNacimiento.setText(LstUsuario.get(i).getFechaNacimiento() != null ? LstUsuario.get(i).getFechaNacimiento().toString() : "");
                                EditTextEdad.setText(LstUsuario.get(i).getEdad() != null ? LstUsuario.get(i).getEdad().toString() : "");
                                SpinnerCategoria.setText(LstUsuario.get(i).getCategoria() != null ? LstUsuario.get(i).getCategoria().toString() : "");
                                SpinnerGenero.setText(LstUsuario.get(i).getGenero() != null ? LstUsuario.get(i).getGenero().toString() : "");
                                EditTextCelular.setText(LstUsuario.get(i).getCelular() != null ? LstUsuario.get(i).getCelular().toString() : "");
                                EditTextTelefono.setText(LstUsuario.get(i).getTelefono() != null ? LstUsuario.get(i).getTelefono().toString() : "");
                                SpinnerDepartamento.setText(LstUsuario.get(i).getDepartamento() != null ? LstUsuario.get(i).getDepartamento().toString() : "");
                                SpinnerCiudad.setText(LstUsuario.get(i).getCiudad() != null ? LstUsuario.get(i).getCiudad().toString() : "");
                                EditTextDireccion.setText(LstUsuario.get(i).getDireccion() != null ? LstUsuario.get(i).getDireccion().toString() : "");
                                EditTextNombreUsuario.setText(LstUsuario.get(i).getUsuario() != null ? LstUsuario.get(i).getUsuario().toString() : "");
                                EditTextCorreoElectronico.setText(LstUsuario.get(i).getEmail() != null ? LstUsuario.get(i).getEmail().toString() : "");
                                SpinnerEstado.setText(LstUsuario.get(i).getEstado() != null ? LstUsuario.get(i).getEstado().toString() : "");
                            }
                            CargarPerfilesProfesionalesUsuario();
                            CargarExperienciasLaboralesUsuario();
                            CargarEstudiosUsuario();
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
        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void ActualizarUsuario () {
        try {
            String sTipoIdentificacion = ObtenerCodigoMaestros(LstMaestrosCodigos, SpinnerTipoIdentificacion.getText() != null ? SpinnerTipoIdentificacion.getText().toString() : "");
            String sIdentificacion = EditTextIdentificacion.getText() != null ? EditTextIdentificacion.getText().toString() : "";
            String sNombreCompleto = EditTextNombreCompleto.getText() != null ? EditTextNombreCompleto.getText().toString() : "";
            String sPrimerNombre = sNombreCompleto.split(" ")[0] != null ? sNombreCompleto.split(" ")[0].toString() : "";
            String sSegundoNombre = sNombreCompleto.split(" ")[1] != null ? sNombreCompleto.split(" ")[1].toString() : "";
            String sPrimerApellido = sNombreCompleto.split(" ")[2] != null ? sNombreCompleto.split(" ")[2].toString() : "";
            String sSegundoApellido = sNombreCompleto.split(" ")[3] != null ? sNombreCompleto.split(" ")[3].toString() : "";
            String sFechaNacimiento = EditTextFechaNacimiento.getText() != null ? EditTextFechaNacimiento.getText().toString() : "";
            int iEdad = EditTextEdad.getText() != null ? Integer.parseInt(EditTextEdad.getText().toString())  : 0;
            String sCategoria = ObtenerCodigoMaestros(LstMaestrosCodigos, SpinnerCategoria.getText() != null ? SpinnerCategoria.getText().toString()  : "");
            String sGenero = ObtenerCodigoMaestros(LstMaestrosCodigos, SpinnerGenero.getText() != null ? SpinnerGenero.getText().toString()  : "");
            String sDepartamento = ObtenerCodigoMaestros(LstMaestrosCodigos, SpinnerDepartamento.getText() != null ? SpinnerDepartamento.getText().toString()  : "");
            String sCiudad = ObtenerCodigoMaestros(LstMaestrosCodigos, SpinnerCiudad.getText() != null ? SpinnerCiudad.getText().toString()  : "");
            String sDireccion = EditTextDireccion.getText() != null ? EditTextDireccion.getText().toString() : "";
            String sCelular = EditTextCelular.getText() != null ? EditTextCelular.getText().toString() : "";
            String sTelefono = EditTextTelefono.getText() != null ? EditTextTelefono.getText().toString() : "";
            String sEmail = EditTextCorreoElectronico.getText() != null ? EditTextCorreoElectronico.getText().toString() : "";
            String sEstado = ObtenerCodigoMaestros(LstMaestrosCodigos, SpinnerEstado.getText() != null ? SpinnerEstado.getText().toString() : "");
            String sUsuario = EditTextNombreUsuario.getText() != null ? EditTextNombreUsuario.getText().toString() : "";

            Usuario ObjUsuario = new Usuario();
            ObjUsuario.setTipoIdentificacion(sTipoIdentificacion);
            ObjUsuario.setIdentificacion(sIdentificacion);
            ObjUsuario.setPrimerNombre(sPrimerNombre);
            ObjUsuario.setSegundoNombre(sSegundoNombre);
            ObjUsuario.setPrimerApellido(sPrimerApellido);
            ObjUsuario.setSegundoApellido(sSegundoApellido);
            ObjUsuario.setFechaNacimiento(sFechaNacimiento);
            ObjUsuario.setEdad(iEdad);
            ObjUsuario.setCategoria(sCategoria);
            ObjUsuario.setGenero(sGenero);
            ObjUsuario.setDepartamento(sDepartamento);
            ObjUsuario.setCiudad(sCiudad);
            ObjUsuario.setDireccion(sDireccion);
            ObjUsuario.setCelular(sCelular);
            ObjUsuario.setTelefono(sTelefono);
            ObjUsuario.setEmail(sEmail);
            ObjUsuario.setEstado(sEstado);
            ObjUsuario.setUsuario(sUsuario);
            ApiService apiService = APIServiceClient.getClient().create(ApiService.class);
            Call call = apiService.ActualizarUsuario(gsToken, ObjUsuario);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        Intent ObjIntent = new Intent (EditarPerfilActivity.this, MainActivity.class);
                        ObjIntent.putExtra("Token", gsToken);
                        startActivity(ObjIntent);
                        finish();
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

    private void CargarPerfilesProfesionalesUsuario () {
        try {
            ApiService apiService = APIServiceClient.getClient().create(ApiService.class);
            Call call = apiService.ConsultarExperienciasLaborales(gsToken);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        LstExperienciasLaborales = (List<ExperienciaLaboral>) response.body();
                        if (LstExperienciasLaborales.size() > 0) {
                            ExperienciaLaboralAdapter = new ExperienciaLaboralAdapter(LstExperienciasLaborales);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(EditarPerfilActivity.this);
                            ReciclerViewExperienciasProfesionaes.setLayoutManager(layoutManager);
                            ReciclerViewExperienciasProfesionaes.setAdapter(ExperienciaLaboralAdapter);
                            ExperienciaLaboralAdapter.setOnItemClickListener(new ExperienciaLaboralAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position, String sCodigo) {
                                    Intent ObjIntent = new Intent (EditarPerfilActivity.this, ExperienciaLaboralActivity.class);
                                    ObjIntent.putExtra("Token", gsToken);
                                    ObjIntent.putExtra("Codigo", sCodigo);
                                    startActivity(ObjIntent);

                                }
                            });
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
        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void CargarExperienciasLaboralesUsuario () {
        try {
            ApiService apiService = APIServiceClient.getClient().create(ApiService.class);
            Call call = apiService.ConsultarPerfilesProfesionales(gsToken);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        LstPerfilesProfesionales = (List<PerfilProfesional>) response.body();
                        if (LstPerfilesProfesionales.size() > 0) {
                            PerfilProfesionalAdapter = new PerfilProfesionalAdapter(LstPerfilesProfesionales);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(EditarPerfilActivity.this);
                            ReciclerViewPerfilProfesional.setLayoutManager(layoutManager);
                            ReciclerViewPerfilProfesional.setAdapter(PerfilProfesionalAdapter);
                            PerfilProfesionalAdapter.setOnItemClickListener(new PerfilProfesionalAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position, String sCodigo) {
                                    Intent ObjIntent = new Intent (EditarPerfilActivity.this, PerfilProfesionalActivity.class);
                                    ObjIntent.putExtra("Token", gsToken);
                                    ObjIntent.putExtra("Codigo", sCodigo);
                                    startActivity(ObjIntent);
                                }
                            });
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
        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void CargarEstudiosUsuario () {
        try {
            ApiService apiService = APIServiceClient.getClient().create(ApiService.class);
            Call call = apiService.ConsultarEstudios(gsToken);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        LstEstudios = (List<Estudio>) response.body();
                        if (LstEstudios.size() > 0) {
                            EstudioAdapter = new EstudioAdapter(LstEstudios);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(EditarPerfilActivity.this);
                            ReciclerViewEstudio.setLayoutManager(layoutManager);
                            ReciclerViewEstudio.setAdapter(EstudioAdapter);
                            EstudioAdapter.setOnItemClickListener(new EstudioAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position, String sCodigo) {
                                    Intent ObjIntent = new Intent (EditarPerfilActivity.this, EstudiosActivity.class);
                                    ObjIntent.putExtra("Token", gsToken);
                                    ObjIntent.putExtra("Codigo", sCodigo);
                                    startActivity(ObjIntent);
                                }
                            });
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

    private void CargarImagenPerfil() {
        final CharSequence[] opciones = {"Tomar Foto", "Cargar Foto", "Cancelar"};
        final AlertDialog.Builder AlertOpciones = new AlertDialog.Builder(EditarPerfilActivity.this);
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
                try {
                    final int mesActual = month + 1;
                    String diaFormateado = (dayOfMonth < 10) ? CERO + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                    String mesFormateado = (mesActual < 10) ? CERO + String.valueOf(mesActual) : String.valueOf(mesActual);
                    EditTextFechaNacimiento.setText(year + BARRA + mesFormateado + BARRA + diaFormateado);
                    EditTextEdad.setText(ObtenerEdad(ObjSimpleDateFormat.parse(year + BARRA + mesFormateado + BARRA + diaFormateado)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        },anio, mes, dia);
        recogerFecha.show();
    }

    public static String ObtenerEdad(Date fechaNacimiento) {
        if (fechaNacimiento != null) {
            StringBuilder result = new StringBuilder();
            Calendar c = new GregorianCalendar();
            c.setTime(fechaNacimiento);
            int sEdad = calcularEdad(c);
            result.append(sEdad);
            result.append(sEdad != 1 ? " años" : " año");
            return result.toString();
        }
        return "";
    }

    private static int calcularEdad(Calendar fechaNac) {
        Calendar today = Calendar.getInstance();
        int diffYear = today.get(Calendar.YEAR) - fechaNac.get(Calendar.YEAR);
        int diffMonth = today.get(Calendar.MONTH) - fechaNac.get(Calendar.MONTH);
        int diffDay = today.get(Calendar.DAY_OF_MONTH) - fechaNac.get(Calendar.DAY_OF_MONTH);
        if (diffMonth < 0 || (diffMonth == 0 && diffDay < 0)) {
            diffYear = diffYear - 1;
        }
        return diffYear;
    }

    @Override
    public void onBackPressed() {
        Intent ObjIntent = new Intent(EditarPerfilActivity.this, PerfilActivity.class);
        ObjIntent.putExtra("Token", gsToken);
        startActivity(ObjIntent);
        finish();
    }
}
