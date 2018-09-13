package com.sistemservicesonline.oss.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sistemservicesonline.oss.adapters.ComentariosAdapter;
import com.sistemservicesonline.oss.adapters.EstudioAdapter;
import com.sistemservicesonline.oss.adapters.ExperienciaLaboralAdapter;
import com.sistemservicesonline.oss.adapters.PerfilProfesionalAdapter;
import com.sistemservicesonline.oss.appcode.Comentario;
import com.sistemservicesonline.oss.appcode.Estudio;
import com.sistemservicesonline.oss.appcode.ExperienciaLaboral;
import com.sistemservicesonline.oss.appcode.PerfilProfesional;
import com.sistemservicesonline.oss.appcode.Usuario;
import com.sistemservicesonline.oss.R;
import com.sistemservicesonline.oss.interfaces.ApiService;
import com.sistemservicesonline.oss.services.APIServiceClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilActivity extends AppCompatActivity {

    TextView
              TxvNombreCompleto
            , TxvNumeroServicios
            , TxvCalificacion
            , TxvEstado
            , TxvCorreoElectronico
            , TxvCelular
            , TxvTelefono
            , TxvCiudad
            , TextViewPerfilProfesional
            , TextViewExperienciasLaborales
            , TextViewEstudios
            , TextViewNombreCompletocoment;

    ImageView
              ImageViewEditarPerfil;

    RelativeLayout
            RelativeLayoutComentarios;

    SwipeRefreshLayout
            swipeRefreshLayout;

    ProgressDialog
            progressDialog;

    RecyclerView
            ReciclerViewPerfilProfesional
            , ReciclerViewExperienciasProfesionaes
            , ReciclerViewEstudio
            , ReciclerViewComentarios;

    Button
            ButtonContactar;

    private String gsToken = "";
    private String gsTokenInvitado = "";

    private PerfilProfesionalAdapter PerfilProfesionalAdapter;
    private ExperienciaLaboralAdapter ExperienciaLaboralAdapter;
    private EstudioAdapter EstudioAdapter;
    private ComentariosAdapter ComentariosAdapter;

    RatingBar ratingBar;
    View Header;

    List<Usuario> LstUsuario = new ArrayList<>();
    List<Usuario> LstUsuarioInvitado = new ArrayList<>();
    List<PerfilProfesional> LstPerfilesProfesionales = new ArrayList<>();
    List<ExperienciaLaboral> LstExperienciasLaborales = new ArrayList<>();
    List<Estudio> LstEstudios = new ArrayList<>();
    List<Comentario> LstComentarios = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        gsToken = getIntent().getExtras().getString("Token") != null ? getIntent().getExtras().getString("Token").toString() : "";
        gsTokenInvitado = getIntent().getExtras().getString("TokenInvitado") != null ? getIntent().getExtras().getString("TokenInvitado").toString() : "";

        InicializarControles();
        if (!gsToken.equals("")) {
            ImageViewEditarPerfil.setVisibility(View.VISIBLE);
            ButtonContactar.setVisibility(View.GONE);
            RelativeLayoutComentarios.setVisibility(View.GONE);
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

            progressDialog = new ProgressDialog(PerfilActivity.this);
            progressDialog.setMessage("Cargando...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            progressDialog.setCancelable(false);

            swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeRefreshLayout.setRefreshing(false);
                }
            });

            ratingBar = (RatingBar) findViewById(R.id.ratingBar);

            /*Inicio TextView Controls*/
            TxvNombreCompleto = findViewById(R.id.TextViewNombreCompleto);
            TxvNumeroServicios = findViewById(R.id.TextViewNumeroServicios);
            TxvCalificacion = findViewById(R.id.TextViewCalificacion);
            TxvEstado = findViewById(R.id.TextViewEstado);
            TxvCorreoElectronico = findViewById(R.id.TextViewCorreoElectronico);
            TxvCelular = findViewById(R.id.TextViewCelular);
            TxvTelefono = findViewById(R.id.TextViewTelefono);
            TxvCiudad = findViewById(R.id.TextViewCiudad);
            TextViewPerfilProfesional = findViewById(R.id.TextViewPerfilProfesional);
            TextViewExperienciasLaborales = findViewById(R.id.TextViewExperienciasLaborales);
            TextViewEstudios = findViewById(R.id.TextViewEstudios);
            TextViewNombreCompletocoment = findViewById(R.id.TextViewNombreCompletocoment);
            /*Fin TextView Controls*/

            /*Inicio ImageView Controls*/
            ImageViewEditarPerfil = findViewById(R.id.ImageViewEditarPerfil);
            ImageViewEditarPerfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent ObjIntent = new Intent (PerfilActivity.this, EditarPerfilActivity.class);
                    ObjIntent.putExtra("Token", gsToken);
                    startActivity(ObjIntent);
                    finish();
                }
            });
            /*Fin ImageView Controls*/

            /*Inicio LinearLayout Controls*/
            RelativeLayoutComentarios = findViewById(R.id.RelativeLayoutComentarios);
            /*Fin LinearLayout Controls*/

            /*Inicio ReciclerView Controls*/
            ReciclerViewPerfilProfesional = findViewById(R.id.ReciclerViewPerfilProfesional);
            ReciclerViewExperienciasProfesionaes = findViewById(R.id.ReciclerViewExperienciasProfesionaes);
            ReciclerViewEstudio = findViewById(R.id.ReciclerViewEstudio);
            ReciclerViewComentarios = findViewById(R.id.ReciclerViewComentarios);
            /*Fin ReciclerView Controls*/

            /*Inicio Button Controls*/
            ButtonContactar = findViewById(R.id.ButtonContactar);
            /*Fin Button Controls*/
        } catch (Exception e) {
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
                                TxvNombreCompleto.setText(sNombreCompleto);
                                TxvEstado.setText(LstUsuario.get(i) != null ? LstUsuario.get(i).getEstado() : "");
                                TxvCorreoElectronico.setText(LstUsuario.get(i) != null ? LstUsuario.get(i).getEmail() : "");
                                TxvCelular.setText(LstUsuario.get(i) != null ? LstUsuario.get(i).getCelular() : "");
                                TxvTelefono.setText(LstUsuario.get(i) != null ? LstUsuario.get(i).getTelefono() : "");
                                TxvCiudad.setText(LstUsuario.get(i) != null ? LstUsuario.get(i).getCiudad() : "");
                            }

                            if (!gsTokenInvitado.equals("")) {
                                ImageViewEditarPerfil.setVisibility(View.GONE);
                                ButtonContactar.setVisibility(View.VISIBLE);
                                RelativeLayoutComentarios.setVisibility(View.VISIBLE);
                                ObtenerUsuarioInvitado();
                            }

                            CargarPerfilesProfesionalesUsuario();
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

    private void ObtenerUsuarioInvitado () {
        try {
            ApiService apiService = APIServiceClient.getClient().create(ApiService.class);
            Call call = apiService.ConsultarUsuario(gsTokenInvitado);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        LstUsuarioInvitado = (List<Usuario>) response.body();
                        if (LstUsuarioInvitado.size() > 0) {
                            for (int i = 0; i < LstUsuarioInvitado.size(); i++) {
                                TextViewNombreCompletocoment.setText(LstUsuarioInvitado.get(i).getPrimerNombre() + " " + LstUsuarioInvitado.get(i).getSegundoNombre() + " " + LstUsuarioInvitado.get(i).getPrimerApellido() + " " + LstUsuarioInvitado.get(i).getSegundoApellido());
                            }
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

    private void CargarPerfilesProfesionalesUsuario () {
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
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PerfilActivity.this);
                            ReciclerViewPerfilProfesional.setLayoutManager(layoutManager);
                            ReciclerViewPerfilProfesional.setAdapter(PerfilProfesionalAdapter);
                            PerfilProfesionalAdapter.setOnItemClickListener(new PerfilProfesionalAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position, String sCodigo) {
                                    Intent ObjIntent = new Intent (PerfilActivity.this, PerfilProfesionalActivity.class);
                                    ObjIntent.putExtra("Token", gsToken);
                                    ObjIntent.putExtra("Codigo", sCodigo);
                                    startActivity(ObjIntent);
                                }
                            });
                        } else {
                            TextViewPerfilProfesional.setVisibility(View.GONE);
                        }
                        CargarExperienciasLaboralesUsuario();
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
            Call call = apiService.ConsultarExperienciasLaborales(gsToken);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        LstExperienciasLaborales = (List<ExperienciaLaboral>) response.body();
                        if (LstExperienciasLaborales.size() > 0) {
                            ExperienciaLaboralAdapter = new ExperienciaLaboralAdapter(LstExperienciasLaborales);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PerfilActivity.this);
                            ReciclerViewExperienciasProfesionaes.setLayoutManager(layoutManager);
                            ReciclerViewExperienciasProfesionaes.setAdapter(ExperienciaLaboralAdapter);
                            ExperienciaLaboralAdapter.setOnItemClickListener(new ExperienciaLaboralAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position, String sCodigo) {
                                    Intent ObjIntent = new Intent (PerfilActivity.this, ExperienciaLaboralActivity.class);
                                    ObjIntent.putExtra("Token", gsToken);
                                    ObjIntent.putExtra("Codigo", sCodigo);
                                    startActivity(ObjIntent);

                                }
                            });
                        } else {
                            TextViewExperienciasLaborales.setVisibility(View.GONE);
                        }
                        CargarEstudiosUsuario();
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
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PerfilActivity.this);
                            ReciclerViewEstudio.setLayoutManager(layoutManager);
                            ReciclerViewEstudio.setAdapter(EstudioAdapter);
                            EstudioAdapter.setOnItemClickListener(new EstudioAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position, String sCodigo) {
                                    Intent ObjIntent = new Intent (PerfilActivity.this, EstudiosActivity.class);
                                    ObjIntent.putExtra("Token", gsToken);
                                    ObjIntent.putExtra("Codigo", sCodigo);
                                    startActivity(ObjIntent);
                                }
                            });
                        } else {
                            TextViewEstudios.setVisibility(View.GONE);
                        }
                        CargarComentariosUsuario();
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

    private void CargarComentariosUsuario () {
        try {
            ApiService apiService = APIServiceClient.getClient().create(ApiService.class);
            Call call = apiService.ConsultarComentarios(gsToken);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        LstComentarios = (List<Comentario>) response.body();
                        if (LstComentarios.size() > 0) {
                            ComentariosAdapter = new ComentariosAdapter(LstComentarios);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PerfilActivity.this);
                            ReciclerViewComentarios.setLayoutManager(layoutManager);
                            ReciclerViewComentarios.setAdapter(ComentariosAdapter);
                        } else {
                            ReciclerViewComentarios.setVisibility(View.GONE);
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

    private void GuardarComentario () {
        try {

        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent ObjIntent = new Intent(PerfilActivity.this, MainActivity.class);
        ObjIntent.putExtra("Token", !gsTokenInvitado.isEmpty() ? gsTokenInvitado : gsToken);
        startActivity(ObjIntent);
        finish();
    }
}
