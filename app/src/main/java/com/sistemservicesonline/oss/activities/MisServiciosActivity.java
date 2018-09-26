package com.sistemservicesonline.oss.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.sistemservicesonline.oss.R;
import com.sistemservicesonline.oss.adapters.ServiciosAdapter;
import com.sistemservicesonline.oss.appcode.Servicio;
import com.sistemservicesonline.oss.interfaces.ApiService;
import com.sistemservicesonline.oss.services.APIServiceClient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MisServiciosActivity extends AppCompatActivity {

    SwipeRefreshLayout
            swipeRefreshLayout;

    ProgressDialog
            progressDialog;

    RecyclerView
            ReciclerViewServiciosSolicitados
            , ReciclerViewServiciosSolicitudes;

    LinearLayout
            LinearLayoutServiciosSolicitados
            , LinearLayoutServiciosSolicitudes;

    ServiciosAdapter
            ServiciosAdapter;

    RadioButton
            RadioButtonSolicitud
            , RadioButtonAceptado
            , RadioButtonIniciado
            , RadioButtonSuspendido
            , RadioButtonTerminado
            , RadioButtonCancelado;

    TextView
            TextViewCodigoUsuarioServicio;

    MaterialEditText
            EditTextFechaServicio
            , EditTextHoraServicio
            , EditTextFechaInicial
            , EditTextHoraInicial
            , EditTextFechaFinal
            , EditTextHoraFinal
            , EditTextDescripcion;

    Button
            ButtonGuardar;

    private String gsToken = "";
    private String gsCodigoServicio = "";
    private String gsFechaServicio = "";
    private String gsHoraServicio = "";
    private String gsFechaInicio = "";
    private String gsHoraInicio= "";
    private String gsFechaFinal = "";
    private String gsHoraFinal = "";
    private String gsDescripcion = "";
    private String gsEstadoServicio = "";
    private static final String CERO = "0";
    private static final String BARRA = "-";

    public final Calendar c = Calendar.getInstance();
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    final int hour = c.get(Calendar.HOUR_OF_DAY);
    final int minute = c.get(Calendar.MINUTE);

    List<Servicio> LstUsuario = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_servicios);

        gsToken = getIntent().getExtras().getString("Token") != null ? getIntent().getExtras().getString("Token").toString() : "";

        InicializarControles();
        ConsultarServiciosSolicitados();
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

            progressDialog = new ProgressDialog(MisServiciosActivity.this);
            progressDialog.setMessage("Cargando...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);

            LinearLayoutServiciosSolicitados = findViewById(R.id.LinearLayoutServiciosSolicitados);
            LinearLayoutServiciosSolicitudes = findViewById(R.id.LinearLayoutServiciosSolicitudes);

            ReciclerViewServiciosSolicitados = findViewById(R.id.ReciclerViewServiciosSolicitados);
            ReciclerViewServiciosSolicitudes = findViewById(R.id.ReciclerViewServiciosSolicitudes);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void ConsultarServiciosSolicitados () {
        try {
            ApiService apiService = APIServiceClient.getClient().create(ApiService.class);
            Call call = apiService.ConsultarServiciosSolicitados(gsToken);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        LstUsuario = (List<Servicio>) response.body();
                        if (LstUsuario.size() > 0) {
                            ServiciosAdapter = new ServiciosAdapter(LstUsuario);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MisServiciosActivity.this);
                            ReciclerViewServiciosSolicitados.setLayoutManager(layoutManager);
                            ReciclerViewServiciosSolicitados.setAdapter(ServiciosAdapter);
                            ServiciosAdapter.setOnItemClickListener(new ServiciosAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position, String sCodigo) {
                                    gsCodigoServicio = sCodigo;
                                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(MisServiciosActivity.this, R.style.DialogTheme);
                                    View mView = getLayoutInflater().inflate(R.layout.alert_servicios, null);

                                    TextViewCodigoUsuarioServicio = mView.findViewById(R.id.TextViewCodigoUsuarioServicio);

                                    EditTextFechaServicio = mView.findViewById(R.id.EditTextFechaServicio);
                                    EditTextFechaServicio.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            obtenerFecha("FechaServicio");
                                        }
                                    });

                                    EditTextHoraServicio = mView.findViewById(R.id.EditTextHoraServicio);
                                    EditTextHoraServicio.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            obtenerHora("HoraServicio");
                                        }
                                    });

                                    EditTextFechaInicial = mView.findViewById(R.id.EditTextFechaInicial);
                                    EditTextFechaInicial.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            obtenerFecha("FechaInicial");
                                        }
                                    });
                                    EditTextHoraInicial = mView.findViewById(R.id.EditTextHoraInicial);
                                    EditTextHoraInicial.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            obtenerHora("HoraInicial");
                                        }
                                    });
                                    EditTextFechaFinal = mView.findViewById(R.id.EditTextFechaFinal);
                                    EditTextFechaFinal.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            obtenerFecha("FechaFinal");
                                        }
                                    });
                                    EditTextHoraFinal = mView.findViewById(R.id.EditTextHoraFinal);
                                    EditTextHoraFinal.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            obtenerHora("HoraFinal");
                                        }
                                    });
                                    EditTextDescripcion = mView.findViewById(R.id.EditTextDescripcion);
                                    RadioButtonSolicitud = mView.findViewById(R.id.RadioButtonSolicitud);
                                    RadioButtonAceptado = mView.findViewById(R.id.RadioButtonAceptado);
                                    RadioButtonIniciado = mView.findViewById(R.id.RadioButtonIniciado);
                                    RadioButtonSuspendido = mView.findViewById(R.id.RadioButtonSuspendido);
                                    RadioButtonTerminado = mView.findViewById(R.id.RadioButtonTerminado);
                                    RadioButtonCancelado = mView.findViewById(R.id.RadioButtonCancelado);

                                    ButtonGuardar = mView.findViewById(R.id.ButtonGuardar);
                                    ButtonGuardar.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            EditarServicio();
                                        }
                                    });

                                    mBuilder.setView(mView).setTitle("OSS");
                                    AlertDialog dialog = mBuilder.create();
                                    dialog.show();

                                    ConsultarServicio();
                                }
                            });
                        } else {
                            LinearLayoutServiciosSolicitados.setVisibility(View.GONE);
                        }

                        ConsultarServiciosSolicitudes();
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
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void ConsultarServiciosSolicitudes () {
        try {
            ApiService apiService = APIServiceClient.getClient().create(ApiService.class);
            Call call = apiService.ConsultarServiciosSolicitudes(gsToken);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        LstUsuario = (List<Servicio>) response.body();
                        if (LstUsuario.size() > 0) {
                            ServiciosAdapter = new ServiciosAdapter(LstUsuario);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MisServiciosActivity.this);
                            ReciclerViewServiciosSolicitudes.setLayoutManager(layoutManager);
                            ReciclerViewServiciosSolicitudes.setAdapter(ServiciosAdapter);
                            ServiciosAdapter.setOnItemClickListener(new ServiciosAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position, String sCodigo) {
                                    gsCodigoServicio = sCodigo;
                                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(MisServiciosActivity.this, R.style.DialogTheme);
                                    View mView = getLayoutInflater().inflate(R.layout.alert_servicios, null);

                                    TextViewCodigoUsuarioServicio = mView.findViewById(R.id.TextViewCodigoUsuarioServicio);

                                    EditTextFechaServicio = mView.findViewById(R.id.EditTextFechaServicio);
                                    EditTextFechaServicio.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            obtenerFecha("FechaServicio");
                                        }
                                    });

                                    EditTextHoraServicio = mView.findViewById(R.id.EditTextHoraServicio);
                                    EditTextHoraServicio.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            obtenerHora("HoraServicio");
                                        }
                                    });

                                    EditTextFechaInicial = mView.findViewById(R.id.EditTextFechaInicial);
                                    EditTextFechaInicial.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            obtenerFecha("FechaInicial");
                                        }
                                    });
                                    EditTextHoraInicial = mView.findViewById(R.id.EditTextHoraInicial);
                                    EditTextHoraInicial.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            obtenerHora("HoraInicial");
                                        }
                                    });
                                    EditTextFechaFinal = mView.findViewById(R.id.EditTextFechaFinal);
                                    EditTextFechaFinal.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            obtenerFecha("FechaFinal");
                                        }
                                    });
                                    EditTextHoraFinal = mView.findViewById(R.id.EditTextHoraFinal);
                                    EditTextHoraFinal.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            obtenerHora("HoraFinal");
                                        }
                                    });
                                    EditTextDescripcion = mView.findViewById(R.id.EditTextDescripcion);
                                    RadioButtonSolicitud = mView.findViewById(R.id.RadioButtonSolicitud);
                                    RadioButtonAceptado = mView.findViewById(R.id.RadioButtonAceptado);
                                    RadioButtonIniciado = mView.findViewById(R.id.RadioButtonIniciado);
                                    RadioButtonSuspendido = mView.findViewById(R.id.RadioButtonSuspendido);
                                    RadioButtonTerminado = mView.findViewById(R.id.RadioButtonTerminado);
                                    RadioButtonCancelado = mView.findViewById(R.id.RadioButtonCancelado);

                                    ButtonGuardar = mView.findViewById(R.id.ButtonGuardar);
                                    ButtonGuardar.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            EditarServicio();
                                        }
                                    });

                                    mBuilder.setView(mView).setTitle("OSS");
                                    AlertDialog dialog = mBuilder.create();
                                    dialog.show();

                                    ConsultarServicio();
                                }
                            });
                        } else {
                            LinearLayoutServiciosSolicitudes.setVisibility(View.GONE);
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
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void ConsultarServicio () {
        try {
            ApiService apiService = APIServiceClient.getClient().create(ApiService.class);
            Call call = apiService.ConsultarServicio(gsCodigoServicio);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        LstUsuario = (List<Servicio>) response.body();
                        if (LstUsuario.size() > 0) {
                            for (int i = 0; i < LstUsuario.size(); i++) {
                                if (!LstUsuario.get(i).getEstado().equals("ESTS5") && !LstUsuario.get(i).getEstado().equals("ESTS6")) {
                                    EditTextFechaServicio.setEnabled(true);
                                    EditTextHoraServicio.setEnabled(true);
                                    EditTextFechaInicial.setEnabled(true);
                                    EditTextHoraInicial.setEnabled(true);
                                    EditTextFechaFinal.setEnabled(true);
                                    EditTextHoraFinal.setEnabled(true);
                                    EditTextDescripcion.setEnabled(true);
                                    RadioButtonSolicitud.setEnabled(true);
                                    RadioButtonAceptado.setEnabled(true);
                                    RadioButtonIniciado.setEnabled(true);
                                    RadioButtonSuspendido.setEnabled(true);
                                    RadioButtonTerminado.setEnabled(true);
                                    RadioButtonCancelado.setEnabled(true);
                                } else {
                                    EditTextFechaServicio.setEnabled(false);
                                    EditTextHoraServicio.setEnabled(false);
                                    EditTextFechaInicial.setEnabled(false);
                                    EditTextHoraInicial.setEnabled(false);
                                    EditTextFechaFinal.setEnabled(false);
                                    EditTextHoraFinal.setEnabled(false);
                                    EditTextDescripcion.setEnabled(false);
                                    RadioButtonSolicitud.setEnabled(false);
                                    RadioButtonAceptado.setEnabled(false);
                                    RadioButtonIniciado.setEnabled(false);
                                    RadioButtonSuspendido.setEnabled(false);
                                    RadioButtonTerminado.setEnabled(false);
                                    RadioButtonCancelado.setEnabled(false);
                                }

                                TextViewCodigoUsuarioServicio.setText(LstUsuario.get(i).getCodigoUsuarioServicio().toString());
                                EditTextFechaServicio.setText(LstUsuario.get(i).getFechaServicio() != null && !LstUsuario.get(i).getFechaServicio().split(" ")[0].equals("1900-01-01") ? LstUsuario.get(i).getFechaServicio().toString().split(" ")[0] : "");
                                EditTextHoraServicio.setText(LstUsuario.get(i).getFechaServicio() != null && !LstUsuario.get(i).getFechaServicio().toString().split(" ")[1].equals("00:00") ? LstUsuario.get(i).getFechaServicio().toString().split(" ")[1] : "");
                                EditTextFechaInicial.setText(LstUsuario.get(i).getFechaInicio() != null && !LstUsuario.get(i).getFechaInicio().split(" ")[0].equals("1900-01-01") ? LstUsuario.get(i).getFechaInicio().toString().split(" ")[0] : "");
                                EditTextHoraInicial.setText(LstUsuario.get(i).getFechaInicio() != null && !LstUsuario.get(i).getFechaInicio().toString().split(" ")[1].equals("00:00") ? LstUsuario.get(i).getFechaInicio().toString().split(" ")[1] : "");
                                EditTextFechaFinal.setText(LstUsuario.get(i).getFechaFinal() != null && !LstUsuario.get(i).getFechaFinal().split(" ")[0].equals("1900-01-01") ? LstUsuario.get(i).getFechaFinal().toString().split(" ")[0] : "");
                                EditTextHoraFinal.setText(LstUsuario.get(i).getFechaFinal() != null && !LstUsuario.get(i).getFechaFinal().toString().split(" ")[1].equals("00:00") ? LstUsuario.get(i).getFechaFinal().toString().split(" ")[1] : "");
                                EditTextDescripcion.setText(LstUsuario.get(i).getDescripcion() != null ? LstUsuario.get(i).getDescripcion().toString() : " ");
                                RadioButtonSolicitud.setChecked(LstUsuario.get(i).getEstado().equals("ESTS1") ? true : false);
                                RadioButtonAceptado.setChecked(LstUsuario.get(i).getEstado().equals("ESTS2") ? true : false);
                                RadioButtonIniciado.setChecked(LstUsuario.get(i).getEstado().equals("ESTS3") ? true : false);
                                RadioButtonSuspendido.setChecked(LstUsuario.get(i).getEstado().equals("ESTS4") ? true : false);
                                RadioButtonTerminado.setChecked(LstUsuario.get(i).getEstado().equals("ESTS5") ? true : false);
                                RadioButtonCancelado.setChecked(LstUsuario.get(i).getEstado().equals("ESTS6") ? true : false);
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
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void EditarServicio () {
        try {
            if (RadioButtonSolicitud.isChecked()) gsEstadoServicio = "ESTS1";
            if (RadioButtonAceptado.isChecked()) gsEstadoServicio = "ESTS2";
            if (RadioButtonIniciado.isChecked()) gsEstadoServicio = "ESTS3";
            if (RadioButtonSuspendido.isChecked()) gsEstadoServicio = "ESTS4";
            if (RadioButtonTerminado.isChecked()) gsEstadoServicio = "ESTS5";
            if (RadioButtonCancelado.isChecked()) gsEstadoServicio = "ESTS6";

            gsFechaServicio = EditTextFechaServicio.getText() != null ? EditTextFechaServicio.getText().toString() : "";
            gsHoraServicio = EditTextHoraServicio.getText() != null ? EditTextHoraServicio.getText().toString() : "";
            gsFechaInicio = EditTextFechaInicial.getText() != null ? EditTextFechaInicial.getText().toString() : "";
            gsHoraInicio= EditTextHoraInicial.getText() != null ? EditTextHoraInicial.getText().toString() : "";
            gsFechaFinal = EditTextFechaFinal.getText() != null ? EditTextFechaFinal.getText().toString() : "";
            gsHoraFinal = EditTextHoraFinal.getText() != null ? EditTextHoraFinal.getText().toString() : "";
            gsDescripcion = EditTextDescripcion.getText() != null ? EditTextDescripcion.getText().toString() : "";

            if (!gsEstadoServicio.isEmpty()) {
                // Solicitud - Aceptado
                if (gsEstadoServicio.equals("ESTS1") || gsEstadoServicio.equals("ESTS2")) {
                    if (!gsDescripcion.equals("") && !gsFechaServicio.equals("") && !gsHoraServicio.equals("")) {
                        if (gsFechaInicio.equals("") && gsHoraInicio.equals("") && gsFechaFinal.equals("") && gsHoraFinal.equals("")) {
                            Guardar();
                        } else {
                            Toast.makeText(getApplicationContext(), "Los campos Fecha y Hora inicio o final no deben estar diligenciados, por favor verifique", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "El campo Descripción se debe diligenciar, por favor verifique", Toast.LENGTH_SHORT).show();
                    }
                }

                // Iniciado - Suspendido
                if (gsEstadoServicio.equals("ESTS3") || gsEstadoServicio.equals("ESTS4")) {
                    if (!gsDescripcion.equals("")&& !gsFechaServicio.equals("") && !gsHoraServicio.equals("") && !gsFechaInicio.equals("") && !gsHoraInicio.equals("")) {
                        if (gsFechaFinal.equals("") && gsHoraFinal.equals("")) {
                            Guardar();
                        } else {
                            Toast.makeText(getApplicationContext(), "Los campos Fecha y Hora final no deben estar diligenciados, por favor verifique", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Los campos Fecha de incio, Hora de inicio y Descripción se deben diligenciar, por favor verifique", Toast.LENGTH_SHORT).show();
                    }
                }

                // Terminado
                if (gsEstadoServicio.equals("ESTS5")) {
                    if (!gsDescripcion.equals("")&& !gsFechaServicio.equals("") && !gsHoraServicio.equals("") && !gsFechaInicio.equals("") && !gsHoraInicio.equals("") && !gsFechaFinal.equals("") && !gsHoraFinal.equals("")) {
                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MisServiciosActivity.this, R.style.AlertDialogTheme);
                        mBuilder.setCancelable(false).setTitle("OSS").setMessage("¿Esta seguro de Terminar el Servicio, una vez Terminado no podra reabrir el servicio?")
                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Guardar();
                                    }
                                })
                                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                });
                        AlertDialog dialog = mBuilder.create();
                        dialog.show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Los campos Fecha de incio, Hora de inicio, Fecha final, Hora final y Descripción se deben diligenciar, por favor verifique", Toast.LENGTH_SHORT).show();
                    }
                }

                // Cancelado
                if (gsEstadoServicio.equals("ESTS6")) {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(MisServiciosActivity.this, R.style.AlertDialogTheme);
                    mBuilder.setCancelable(false).setTitle("OSS").setMessage("¿Esta seguro de Cancelar el Servicio, una vez cancelado no podra reabrir el servicio?")
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Guardar();
                                }
                            })
                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                    AlertDialog dialog = mBuilder.create();
                    dialog.show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void Guardar() {
        Servicio ObjServicio = new Servicio();
        ObjServicio.setCodigo(gsCodigoServicio);
        ObjServicio.setFechaServicio(gsFechaServicio.replace("-", "") + " " + gsHoraServicio);
        ObjServicio.setFechaInicio(gsFechaInicio.replace("-", "") + " " + gsHoraInicio);
        ObjServicio.setDescripcion(gsDescripcion);
        ObjServicio.setEstado(gsEstadoServicio);

        if (gsEstadoServicio.equals("ESTS5") || gsEstadoServicio.equals("ESTS6")) {
            ObjServicio.setFechaFinal(gsFechaFinal.replace("-", "") + " " + gsHoraFinal);
        }

        ApiService apiService = APIServiceClient.getClient().create(ApiService.class);
        Call call = apiService.ActualizarServicio(gsCodigoServicio, ObjServicio);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    if (gsEstadoServicio.equals("ESTS5") || gsEstadoServicio.equals("ESTS6")) {
                        ApiService apiService = APIServiceClient.getClient().create(ApiService.class);
                        call = apiService.EliminarServicio(gsCodigoServicio);
                        call.enqueue(new Callback() {
                            @Override
                            public void onResponse(Call call, Response response) {
                                if (response.isSuccessful()) {
                                    Intent ObjIntent = new Intent(MisServiciosActivity.this, MisServiciosActivity.class);
                                    ObjIntent.putExtra("Token", gsToken);
                                    startActivity(ObjIntent);
                                }
                            }

                            @Override
                            public void onFailure(Call call, Throwable t) {
                                progressDialog.dismiss();
                                Log.i("", t.toString());
                                Toast.makeText(getApplicationContext(), "Por favor verifica tu conexión a internet.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Intent ObjIntent = new Intent(MisServiciosActivity.this, MisServiciosActivity.class);
                        ObjIntent.putExtra("Token", gsToken);
                        startActivity(ObjIntent);
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

    private void obtenerFecha(final String sControl){
        DatePickerDialog recogerFecha = new DatePickerDialog(this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final int mesActual = month + 1;
                String diaFormateado = (dayOfMonth < 10) ? CERO + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                String mesFormateado = (mesActual < 10) ? CERO + String.valueOf(mesActual) : String.valueOf(mesActual);

                switch (sControl) {
                    case "FechaServicio" :
                        EditTextFechaServicio.setText(year + BARRA + mesFormateado + BARRA + diaFormateado);
                        break;
                    case "FechaInicial" :
                        EditTextFechaInicial.setText(year + BARRA + mesFormateado + BARRA + diaFormateado);
                        break;
                    case "FechaFinal" :
                        EditTextFechaFinal.setText(year + BARRA + mesFormateado + BARRA + diaFormateado);
                        break;

                }

            }
        },anio, mes, dia);
        recogerFecha.show();
    }

    private void obtenerHora(final String sControl) {

        TimePickerDialog mTimePicker = new TimePickerDialog(this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                switch (sControl) {
                    case "HoraServicio" :
                        EditTextHoraServicio.setText(selectedHour + ":" + selectedMinute);
                        break;
                    case "HoraInicial" :
                        EditTextHoraInicial.setText(selectedHour + ":" + selectedMinute);
                        break;
                    case "HoraFinal" :
                        EditTextHoraFinal.setText(selectedHour + ":" + selectedMinute);
                        break;
                }
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.show();
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
                ServiciosAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent ObjIntent = new Intent(MisServiciosActivity.this, MainActivity.class);
        ObjIntent.putExtra("Token", gsToken);
        startActivity(ObjIntent);
        finish();
    }
}
