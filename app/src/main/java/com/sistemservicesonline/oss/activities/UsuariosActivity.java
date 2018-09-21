package com.sistemservicesonline.oss.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.sistemservicesonline.oss.R;
import com.sistemservicesonline.oss.adapters.UsuariosAdapter;
import com.sistemservicesonline.oss.appcode.Usuario;
import com.sistemservicesonline.oss.interfaces.ApiService;
import com.sistemservicesonline.oss.services.APIServiceClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuariosActivity extends AppCompatActivity {

    SwipeRefreshLayout
            swipeRefreshLayout;

    ProgressDialog
            progressDialog;

    RecyclerView
            ReciclerViewUsuariosArteDiseno
            , ReciclerViewUsuariosVehiculos
            , ReciclerViewUsuariosBelleza
            , ReciclerViewUsuariosDeportes
            , ReciclerViewUsuariosEducacion
            , ReciclerViewUsuariosEntretenimiento
            , ReciclerViewUsuariosMedicina
            , ReciclerViewUsuariosServiciosTecnicos;

    RadioButton
            RadioButtonArteDiseno
            , RadioButtonVehiculos
            , RadioButtonBelleza
            , RadioButtonDeportes
            , RadioButtonEducacion
            , RadioButtonEntretenimiento
            , RadioButtonServiciosTecnicos
            , RadioButtonMedicina;

    LinearLayout
            LinearLayoutArteDiseno
            , LinearLayoutVehiculos
            , LinearLayoutBelleza
            , LinearLayoutDeportes
            , LinearLayoutEducacion
            , LinearLayoutEntretenimiento
            , LinearLayoutMedicina
            , LinearLayoutServiciosTecnicos;


    Button
            ButtonFiltros;

    private String gsToken = "";
    private String gsCategoria = "";

    private Boolean gbArteDiseno = false;
    private Boolean gbVehiculos = false;
    private Boolean gbBelleza = false;
    private Boolean gbDeportes = false;
    private Boolean gbEducacion = false;
    private Boolean gbEntretenimiento = false;
    private Boolean gbServiciosTecnicos = false;
    private Boolean gbMedicina = false;

    private UsuariosAdapter UsuariosAdapter;

    List<Usuario> LstUsuariosArteDiseno = new ArrayList<>();
    List<Usuario> LstUsuariosVehiculos = new ArrayList<>();
    List<Usuario> LstUsuariosBelleza = new ArrayList<>();
    List<Usuario> LstUsuariosDeportes = new ArrayList<>();
    List<Usuario> LstUsuariosEducacion = new ArrayList<>();
    List<Usuario> LstUsuariosEntretenimiento = new ArrayList<>();
    List<Usuario> LstUsuariosMedicina = new ArrayList<>();
    List<Usuario> LstUsuariosServiciosTecnicos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);

        gsToken = getIntent().getExtras().getString("Token") != null ? getIntent().getExtras().getString("Token").toString() : "";
        gsCategoria = getIntent().getExtras().getString("Categoria") != null ? getIntent().getExtras().getString("Categoria").toString() : "0";

        InicializarControles();
        if (gsCategoria.equals("0")) {
            progressDialog.dismiss();
            SeleccionarFiltros();
        } else {
            progressDialog.show();
            CargarUsuarios(gsCategoria);
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

            progressDialog = new ProgressDialog(UsuariosActivity.this);
            progressDialog.setMessage("Cargando...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);

            ButtonFiltros = findViewById(R.id.ButtonFiltros);
            ButtonFiltros.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SeleccionarFiltros();
                }
            });

            LinearLayoutArteDiseno = findViewById(R.id.LinearLayoutArteDiseno);
            LinearLayoutVehiculos = findViewById(R.id.LinearLayoutVehiculos);
            LinearLayoutBelleza = findViewById(R.id.LinearLayoutBelleza);
            LinearLayoutDeportes = findViewById(R.id.LinearLayoutDeportes);
            LinearLayoutEducacion = findViewById(R.id.LinearLayoutEducacion);
            LinearLayoutEntretenimiento = findViewById(R.id.LinearLayoutEntretenimiento);
            LinearLayoutMedicina = findViewById(R.id.LinearLayoutMedicina);
            LinearLayoutServiciosTecnicos = findViewById(R.id.LinearLayoutServiciosTecnicos);

            ReciclerViewUsuariosArteDiseno = findViewById(R.id.ReciclerViewUsuariosArteDiseno);
            ReciclerViewUsuariosVehiculos = findViewById(R.id.ReciclerViewUsuariosVehiculos);
            ReciclerViewUsuariosBelleza = findViewById(R.id.ReciclerViewUsuariosBelleza);
            ReciclerViewUsuariosDeportes = findViewById(R.id.ReciclerViewUsuariosDeportes);
            ReciclerViewUsuariosEducacion = findViewById(R.id.ReciclerViewUsuariosEducacion);
            ReciclerViewUsuariosEntretenimiento = findViewById(R.id.ReciclerViewUsuariosEntretenimiento);
            ReciclerViewUsuariosMedicina = findViewById(R.id.ReciclerViewUsuariosMedicina);
            ReciclerViewUsuariosServiciosTecnicos = findViewById(R.id.ReciclerViewUsuariosServiciosTecnicos);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void SeleccionarFiltros() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(UsuariosActivity.this, R.style.AlertDialogTheme);
        View mView = getLayoutInflater().inflate(R.layout.alert_filtros, null);

        RadioButtonArteDiseno = mView.findViewById(R.id.RadioButtonArteDiseno);
        RadioButtonArteDiseno.setChecked(gsCategoria.equals("CTG1") || gbArteDiseno ? true : false);
        RadioButtonVehiculos = mView.findViewById(R.id.RadioButtonVehiculos);
        RadioButtonVehiculos.setChecked(gsCategoria.equals("CTG2") || gbVehiculos ? true : false);
        RadioButtonBelleza = mView.findViewById(R.id.RadioButtonBelleza);
        RadioButtonBelleza.setChecked(gsCategoria.equals("CTG3") || gbBelleza ? true : false);
        RadioButtonDeportes = mView.findViewById(R.id.RadioButtonDeportes);
        RadioButtonDeportes.setChecked(gsCategoria.equals("CTG4") || gbDeportes ? true : false);
        RadioButtonEducacion = mView.findViewById(R.id.RadioButtonEducacion);
        RadioButtonEducacion.setChecked(gsCategoria.equals("CTG5") || gbEducacion ? true : false);
        RadioButtonEntretenimiento = mView.findViewById(R.id.RadioButtonEntretenimiento);
        RadioButtonEntretenimiento.setChecked(gsCategoria.equals("CTG6") || gbEntretenimiento ? true : false);
        RadioButtonMedicina = mView.findViewById(R.id.RadioButtonMedicina);
        RadioButtonMedicina.setChecked(gsCategoria.equals("CTG7") || gbMedicina ? true : false);
        RadioButtonServiciosTecnicos = mView.findViewById(R.id.RadioButtonServiciosTecnicos);
        RadioButtonServiciosTecnicos.setChecked(gsCategoria.equals("CTG8") || gbServiciosTecnicos ? true : false);

        mBuilder.setView(mView).setTitle("OSS").setMessage("Seleccione las categorias a filtrar.")
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        gbArteDiseno = RadioButtonArteDiseno.isChecked();
                        if (gbArteDiseno) CargarUsuarios("CTG1");
                        gbVehiculos = RadioButtonVehiculos.isChecked();
                        if (gbVehiculos) CargarUsuarios("CTG2");
                        gbBelleza = RadioButtonBelleza.isChecked();
                        if (gbBelleza) CargarUsuarios("CTG3");
                        gbDeportes = RadioButtonDeportes.isChecked();
                        if (gbDeportes) CargarUsuarios("CTG4");
                        gbEducacion = RadioButtonEducacion.isChecked();
                        if (gbEducacion) CargarUsuarios("CTG5");
                        gbEntretenimiento = RadioButtonEntretenimiento.isChecked();
                        if (gbEntretenimiento) CargarUsuarios("CTG6");
                        gbMedicina = RadioButtonMedicina.isChecked();
                        if (gbMedicina) CargarUsuarios("CTG7");
                        gbServiciosTecnicos = RadioButtonServiciosTecnicos.isChecked();
                        if (gbServiciosTecnicos) CargarUsuarios("CTG8");
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressDialog.dismiss();
                    }
                });
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    private void CargarUsuarios (String sCategoria) {
        try {

            ApiService apiService = APIServiceClient.getClient().create(ApiService.class);
            Call call = apiService.ConsultarUsuarios(gsToken, sCategoria);
            progressDialog.show();

            switch (sCategoria) {
                case "CTG1" :
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            if (response.isSuccessful()) {
                                LstUsuariosArteDiseno = (List<Usuario>) response.body();
                                if (LstUsuariosArteDiseno.size() > 0) {
                                    UsuariosAdapter = new UsuariosAdapter(LstUsuariosArteDiseno);
                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(UsuariosActivity.this);
                                    ReciclerViewUsuariosArteDiseno.setLayoutManager(layoutManager);
                                    ReciclerViewUsuariosArteDiseno.setAdapter(UsuariosAdapter);
                                    UsuariosAdapter.setOnItemClickListener(new UsuariosAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(int position, String sTokenVisitado) {
                                            Intent ObjIntent = new Intent (UsuariosActivity.this, PerfilActivity.class);
                                            ObjIntent.putExtra("TokenInvitado", gsToken);
                                            ObjIntent.putExtra("Token", sTokenVisitado);
                                            ObjIntent.putExtra("Origen", "Servicios");
                                            startActivity(ObjIntent);
                                        }
                                    });
                                    LinearLayoutArteDiseno.setVisibility(View.VISIBLE);
                                    LinearLayoutVehiculos.setVisibility(View.GONE);
                                    LinearLayoutBelleza.setVisibility(View.GONE);
                                    LinearLayoutDeportes.setVisibility(View.GONE);
                                    LinearLayoutEducacion.setVisibility(View.GONE);
                                    LinearLayoutEntretenimiento.setVisibility(View.GONE);
                                    LinearLayoutMedicina.setVisibility(View.GONE);
                                    LinearLayoutServiciosTecnicos.setVisibility(View.GONE);
                                } else {
                                    LinearLayoutArteDiseno.setVisibility(View.GONE);
                                    LinearLayoutVehiculos.setVisibility(View.GONE);
                                    LinearLayoutBelleza.setVisibility(View.GONE);
                                    LinearLayoutDeportes.setVisibility(View.GONE);
                                    LinearLayoutEducacion.setVisibility(View.GONE);
                                    LinearLayoutEntretenimiento.setVisibility(View.GONE);
                                    LinearLayoutMedicina.setVisibility(View.GONE);
                                    LinearLayoutServiciosTecnicos.setVisibility(View.GONE);
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
                    break;
                case "CTG2" :
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            if (response.isSuccessful()) {
                                LstUsuariosVehiculos = (List<Usuario>) response.body();
                                if (LstUsuariosVehiculos.size() > 0) {
                                    UsuariosAdapter = new UsuariosAdapter(LstUsuariosVehiculos);
                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(UsuariosActivity.this);
                                    ReciclerViewUsuariosVehiculos.setLayoutManager(layoutManager);
                                    ReciclerViewUsuariosVehiculos.setAdapter(UsuariosAdapter);
                                    UsuariosAdapter.setOnItemClickListener(new UsuariosAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(int position, String sTokenVisitado) {
                                            Intent ObjIntent = new Intent (UsuariosActivity.this, PerfilActivity.class);
                                            ObjIntent.putExtra("TokenInvitado", gsToken);
                                            ObjIntent.putExtra("Token", sTokenVisitado);
                                            ObjIntent.putExtra("Origen", "Servicios");
                                            startActivity(ObjIntent);
                                        }
                                    });
                                    LinearLayoutArteDiseno.setVisibility(View.GONE);
                                    LinearLayoutVehiculos.setVisibility(View.VISIBLE);
                                    LinearLayoutBelleza.setVisibility(View.GONE);
                                    LinearLayoutDeportes.setVisibility(View.GONE);
                                    LinearLayoutEducacion.setVisibility(View.GONE);
                                    LinearLayoutEntretenimiento.setVisibility(View.GONE);
                                    LinearLayoutMedicina.setVisibility(View.GONE);
                                    LinearLayoutServiciosTecnicos.setVisibility(View.GONE);
                                } else {
                                    LinearLayoutArteDiseno.setVisibility(View.GONE);
                                    LinearLayoutVehiculos.setVisibility(View.GONE);
                                    LinearLayoutBelleza.setVisibility(View.GONE);
                                    LinearLayoutDeportes.setVisibility(View.GONE);
                                    LinearLayoutEducacion.setVisibility(View.GONE);
                                    LinearLayoutEntretenimiento.setVisibility(View.GONE);
                                    LinearLayoutMedicina.setVisibility(View.GONE);
                                    LinearLayoutServiciosTecnicos.setVisibility(View.GONE);
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
                    break;
                case "CTG3" :
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            if (response.isSuccessful()) {
                                LstUsuariosBelleza = (List<Usuario>) response.body();
                                if (LstUsuariosBelleza.size() > 0) {
                                    UsuariosAdapter = new UsuariosAdapter(LstUsuariosBelleza);
                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(UsuariosActivity.this);
                                    ReciclerViewUsuariosBelleza.setLayoutManager(layoutManager);
                                    ReciclerViewUsuariosBelleza.setAdapter(UsuariosAdapter);
                                    UsuariosAdapter.setOnItemClickListener(new UsuariosAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(int position, String sTokenVisitado) {
                                            Intent ObjIntent = new Intent (UsuariosActivity.this, PerfilActivity.class);
                                            ObjIntent.putExtra("TokenInvitado", gsToken);
                                            ObjIntent.putExtra("Token", sTokenVisitado);
                                            ObjIntent.putExtra("Origen", "Servicios");
                                            startActivity(ObjIntent);
                                        }
                                    });
                                    LinearLayoutArteDiseno.setVisibility(View.GONE);
                                    LinearLayoutVehiculos.setVisibility(View.GONE);
                                    LinearLayoutBelleza.setVisibility(View.VISIBLE);
                                    LinearLayoutDeportes.setVisibility(View.GONE);
                                    LinearLayoutEducacion.setVisibility(View.GONE);
                                    LinearLayoutEntretenimiento.setVisibility(View.GONE);
                                    LinearLayoutMedicina.setVisibility(View.GONE);
                                    LinearLayoutServiciosTecnicos.setVisibility(View.GONE);
                                } else {
                                    LinearLayoutArteDiseno.setVisibility(View.GONE);
                                    LinearLayoutVehiculos.setVisibility(View.GONE);
                                    LinearLayoutBelleza.setVisibility(View.GONE);
                                    LinearLayoutDeportes.setVisibility(View.GONE);
                                    LinearLayoutEducacion.setVisibility(View.GONE);
                                    LinearLayoutEntretenimiento.setVisibility(View.GONE);
                                    LinearLayoutMedicina.setVisibility(View.GONE);
                                    LinearLayoutServiciosTecnicos.setVisibility(View.GONE);
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
                    break;
                case "CTG4" :
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            if (response.isSuccessful()) {
                                LstUsuariosDeportes = (List<Usuario>) response.body();
                                if (LstUsuariosDeportes.size() > 0) {
                                    UsuariosAdapter = new UsuariosAdapter(LstUsuariosDeportes);
                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(UsuariosActivity.this);
                                    ReciclerViewUsuariosDeportes.setLayoutManager(layoutManager);
                                    ReciclerViewUsuariosDeportes.setAdapter(UsuariosAdapter);
                                    UsuariosAdapter.setOnItemClickListener(new UsuariosAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(int position, String sTokenVisitado) {
                                            Intent ObjIntent = new Intent (UsuariosActivity.this, PerfilActivity.class);
                                            ObjIntent.putExtra("TokenInvitado", gsToken);
                                            ObjIntent.putExtra("Token", sTokenVisitado);
                                            ObjIntent.putExtra("Origen", "Servicios");
                                            startActivity(ObjIntent);
                                        }
                                    });
                                    LinearLayoutArteDiseno.setVisibility(View.GONE);
                                    LinearLayoutVehiculos.setVisibility(View.GONE);
                                    LinearLayoutBelleza.setVisibility(View.GONE);
                                    LinearLayoutDeportes.setVisibility(View.VISIBLE);
                                    LinearLayoutEducacion.setVisibility(View.GONE);
                                    LinearLayoutEntretenimiento.setVisibility(View.GONE);
                                    LinearLayoutMedicina.setVisibility(View.GONE);
                                    LinearLayoutServiciosTecnicos.setVisibility(View.GONE);
                                } else {
                                    LinearLayoutArteDiseno.setVisibility(View.GONE);
                                    LinearLayoutVehiculos.setVisibility(View.GONE);
                                    LinearLayoutBelleza.setVisibility(View.GONE);
                                    LinearLayoutDeportes.setVisibility(View.GONE);
                                    LinearLayoutEducacion.setVisibility(View.GONE);
                                    LinearLayoutEntretenimiento.setVisibility(View.GONE);
                                    LinearLayoutMedicina.setVisibility(View.GONE);
                                    LinearLayoutServiciosTecnicos.setVisibility(View.GONE);
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
                    break;
                case "CTG5" :
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            if (response.isSuccessful()) {
                                LstUsuariosEducacion = (List<Usuario>) response.body();
                                if (LstUsuariosEducacion.size() > 0) {
                                    UsuariosAdapter = new UsuariosAdapter(LstUsuariosEducacion);
                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(UsuariosActivity.this);
                                    ReciclerViewUsuariosEducacion.setLayoutManager(layoutManager);
                                    ReciclerViewUsuariosEducacion.setAdapter(UsuariosAdapter);
                                    UsuariosAdapter.setOnItemClickListener(new UsuariosAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(int position, String sTokenVisitado) {
                                            Intent ObjIntent = new Intent (UsuariosActivity.this, PerfilActivity.class);
                                            ObjIntent.putExtra("TokenInvitado", gsToken);
                                            ObjIntent.putExtra("Token", sTokenVisitado);
                                            ObjIntent.putExtra("Origen", "Servicios");
                                            startActivity(ObjIntent);
                                        }
                                    });
                                    LinearLayoutArteDiseno.setVisibility(View.GONE);
                                    LinearLayoutVehiculos.setVisibility(View.GONE);
                                    LinearLayoutBelleza.setVisibility(View.GONE);
                                    LinearLayoutDeportes.setVisibility(View.GONE);
                                    LinearLayoutEducacion.setVisibility(View.VISIBLE);
                                    LinearLayoutEntretenimiento.setVisibility(View.GONE);
                                    LinearLayoutMedicina.setVisibility(View.GONE);
                                    LinearLayoutServiciosTecnicos.setVisibility(View.GONE);
                                } else {
                                    LinearLayoutArteDiseno.setVisibility(View.GONE);
                                    LinearLayoutVehiculos.setVisibility(View.GONE);
                                    LinearLayoutBelleza.setVisibility(View.GONE);
                                    LinearLayoutDeportes.setVisibility(View.GONE);
                                    LinearLayoutEducacion.setVisibility(View.GONE);
                                    LinearLayoutEntretenimiento.setVisibility(View.GONE);
                                    LinearLayoutMedicina.setVisibility(View.GONE);
                                    LinearLayoutServiciosTecnicos.setVisibility(View.GONE);
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
                    break;
                case "CTG6" :
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            if (response.isSuccessful()) {
                                LstUsuariosEntretenimiento = (List<Usuario>) response.body();
                                if (LstUsuariosEntretenimiento.size() > 0) {
                                    UsuariosAdapter = new UsuariosAdapter(LstUsuariosEntretenimiento);
                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(UsuariosActivity.this);
                                    ReciclerViewUsuariosEntretenimiento.setLayoutManager(layoutManager);
                                    ReciclerViewUsuariosEntretenimiento.setAdapter(UsuariosAdapter);
                                    UsuariosAdapter.setOnItemClickListener(new UsuariosAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(int position, String sTokenVisitado) {
                                            Intent ObjIntent = new Intent (UsuariosActivity.this, PerfilActivity.class);
                                            ObjIntent.putExtra("TokenInvitado", gsToken);
                                            ObjIntent.putExtra("Token", sTokenVisitado);
                                            ObjIntent.putExtra("Origen", "Servicios");
                                            startActivity(ObjIntent);
                                        }
                                    });
                                    LinearLayoutArteDiseno.setVisibility(View.GONE);
                                    LinearLayoutVehiculos.setVisibility(View.GONE);
                                    LinearLayoutBelleza.setVisibility(View.GONE);
                                    LinearLayoutDeportes.setVisibility(View.GONE);
                                    LinearLayoutEducacion.setVisibility(View.GONE);
                                    LinearLayoutEntretenimiento.setVisibility(View.VISIBLE);
                                    LinearLayoutMedicina.setVisibility(View.GONE);
                                    LinearLayoutServiciosTecnicos.setVisibility(View.GONE);
                                } else {
                                    LinearLayoutArteDiseno.setVisibility(View.GONE);
                                    LinearLayoutVehiculos.setVisibility(View.GONE);
                                    LinearLayoutBelleza.setVisibility(View.GONE);
                                    LinearLayoutDeportes.setVisibility(View.GONE);
                                    LinearLayoutEducacion.setVisibility(View.GONE);
                                    LinearLayoutEntretenimiento.setVisibility(View.GONE);
                                    LinearLayoutMedicina.setVisibility(View.GONE);
                                    LinearLayoutServiciosTecnicos.setVisibility(View.GONE);
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
                    break;
                case "CTG7" :
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            if (response.isSuccessful()) {
                                LstUsuariosMedicina = (List<Usuario>) response.body();
                                if (LstUsuariosMedicina.size() > 0) {
                                    UsuariosAdapter = new UsuariosAdapter(LstUsuariosMedicina);
                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(UsuariosActivity.this);
                                    ReciclerViewUsuariosMedicina.setLayoutManager(layoutManager);
                                    ReciclerViewUsuariosMedicina.setAdapter(UsuariosAdapter);
                                    UsuariosAdapter.setOnItemClickListener(new UsuariosAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(int position, String sTokenVisitado) {
                                            Intent ObjIntent = new Intent (UsuariosActivity.this, PerfilActivity.class);
                                            ObjIntent.putExtra("TokenInvitado", gsToken);
                                            ObjIntent.putExtra("Token", sTokenVisitado);
                                            ObjIntent.putExtra("Origen", "Servicios");
                                            startActivity(ObjIntent);
                                        }
                                    });
                                    LinearLayoutArteDiseno.setVisibility(View.GONE);
                                    LinearLayoutVehiculos.setVisibility(View.GONE);
                                    LinearLayoutBelleza.setVisibility(View.GONE);
                                    LinearLayoutDeportes.setVisibility(View.GONE);
                                    LinearLayoutEducacion.setVisibility(View.GONE);
                                    LinearLayoutEntretenimiento.setVisibility(View.GONE);
                                    LinearLayoutMedicina.setVisibility(View.VISIBLE);
                                    LinearLayoutServiciosTecnicos.setVisibility(View.GONE);
                                } else {
                                    LinearLayoutArteDiseno.setVisibility(View.GONE);
                                    LinearLayoutVehiculos.setVisibility(View.GONE);
                                    LinearLayoutBelleza.setVisibility(View.GONE);
                                    LinearLayoutDeportes.setVisibility(View.GONE);
                                    LinearLayoutEducacion.setVisibility(View.GONE);
                                    LinearLayoutEntretenimiento.setVisibility(View.GONE);
                                    LinearLayoutMedicina.setVisibility(View.GONE);
                                    LinearLayoutServiciosTecnicos.setVisibility(View.GONE);
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
                    break;
                case "CTG8" :
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            if (response.isSuccessful()) {
                                LstUsuariosServiciosTecnicos = (List<Usuario>) response.body();
                                if (LstUsuariosServiciosTecnicos.size() > 0) {
                                    UsuariosAdapter = new UsuariosAdapter(LstUsuariosServiciosTecnicos);
                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(UsuariosActivity.this);
                                    ReciclerViewUsuariosServiciosTecnicos.setLayoutManager(layoutManager);
                                    ReciclerViewUsuariosServiciosTecnicos.setAdapter(UsuariosAdapter);
                                    UsuariosAdapter.setOnItemClickListener(new UsuariosAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(int position, String sTokenVisitado) {
                                            Intent ObjIntent = new Intent (UsuariosActivity.this, PerfilActivity.class);
                                            ObjIntent.putExtra("TokenInvitado", gsToken);
                                            ObjIntent.putExtra("Token", sTokenVisitado);
                                            ObjIntent.putExtra("Origen", "Servicios");
                                            startActivity(ObjIntent);
                                        }
                                    });
                                    LinearLayoutArteDiseno.setVisibility(View.GONE);
                                    LinearLayoutVehiculos.setVisibility(View.GONE);
                                    LinearLayoutBelleza.setVisibility(View.GONE);
                                    LinearLayoutDeportes.setVisibility(View.GONE);
                                    LinearLayoutEducacion.setVisibility(View.GONE);
                                    LinearLayoutEntretenimiento.setVisibility(View.GONE);
                                    LinearLayoutMedicina.setVisibility(View.GONE);
                                    LinearLayoutServiciosTecnicos.setVisibility(View.VISIBLE);
                                } else {
                                    LinearLayoutArteDiseno.setVisibility(View.GONE);
                                    LinearLayoutVehiculos.setVisibility(View.GONE);
                                    LinearLayoutBelleza.setVisibility(View.GONE);
                                    LinearLayoutDeportes.setVisibility(View.GONE);
                                    LinearLayoutEducacion.setVisibility(View.GONE);
                                    LinearLayoutEntretenimiento.setVisibility(View.GONE);
                                    LinearLayoutMedicina.setVisibility(View.GONE);
                                    LinearLayoutServiciosTecnicos.setVisibility(View.GONE);
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
                    break;
            }
            progressDialog.dismiss();
        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_buscador, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                UsuariosAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent ObjIntent = new Intent(UsuariosActivity.this, MainActivity.class);
        ObjIntent.putExtra("Token", gsToken);
        startActivity(ObjIntent);
        finish();
    }
}
