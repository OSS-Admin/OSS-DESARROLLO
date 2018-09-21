package com.sistemservicesonline.oss.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.sistemservicesonline.oss.adapters.ComentariosAdapter;
import com.sistemservicesonline.oss.adapters.EstudioAdapter;
import com.sistemservicesonline.oss.adapters.ExperienciaLaboralAdapter;
import com.sistemservicesonline.oss.adapters.PerfilProfesionalAdapter;
import com.sistemservicesonline.oss.appcode.Comentario;
import com.sistemservicesonline.oss.appcode.Estudio;
import com.sistemservicesonline.oss.appcode.ExperienciaLaboral;
import com.sistemservicesonline.oss.appcode.Favorito;
import com.sistemservicesonline.oss.appcode.PerfilProfesional;
import com.sistemservicesonline.oss.appcode.Servicio;
import com.sistemservicesonline.oss.appcode.Usuario;
import com.sistemservicesonline.oss.R;
import com.sistemservicesonline.oss.interfaces.ApiService;
import com.sistemservicesonline.oss.services.APIServiceClient;

import net.sourceforge.jtds.jdbc.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilActivity extends AppCompatActivity {

    CircleImageView
            CircleImageViewNoFavorito
            , CircleImageViewFavorito;

    TextView
              TxvNombreCompleto
            , TxvCalificacion
            , TxvEstado
            , TxvCorreoElectronico
            , TextViewDepartamento
            , TxvCelular
            , TxvTelefono
            , TxvCiudad
            , TextViewPerfilProfesional
            , TextViewExperienciasLaborales
            , TextViewEstudios
            , TextViewNombreCompletocoment
            , TextViewEnviarComentario
            , TextViewVerMasComentarios
            , TextViewVerMenosComentarios;

    MaterialEditText
            EditTextDescripcionComentario
            , EditTextFechaServicio
            , EditTextHoraServicio
            , EditTextDescripcion;

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
            ButtonContactar
            , ButtonContratar
            , ButtonLlamar;

    LinearLayout
            LinearLayoutFechaServicio;

    private String gsToken = "";
    private String gsOrigen = "";
    private String gsTokenInvitado = "";
    private String gsDescripcionComentario = "";
    private Float gsCalificacionComentario;
    private String gsTopComentarios = "5";
    private String gsCelularUsuario = "";
    private String gsFechaServicio = "";
    private String gsHoraServicio = "";
    private String gsDescripcionServicio = "";
    private String gsEstadoServicio = "";
    private static final int SOLICITUD_PERMISO_LLAMADA = 1;
    private static final String CERO = "0";
    private static final String BARRA = "-";

    private Intent ObjIntentLlamada;
    private RatingBar RatingBarCalificacionComentario;
    private PerfilProfesionalAdapter PerfilProfesionalAdapter;
    private ExperienciaLaboralAdapter ExperienciaLaboralAdapter;
    private EstudioAdapter EstudioAdapter;
    private ComentariosAdapter ComentariosAdapter;

    public final Calendar c = Calendar.getInstance();
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR) - 18;
    final int hour = c.get(Calendar.HOUR_OF_DAY);
    final int minute = c.get(Calendar.MINUTE);
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");

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
        gsOrigen = getIntent().getExtras().getString("Origen") != null ? getIntent().getExtras().getString("Origen").toString() : "";

        InicializarControles();
        if (!gsToken.equals("")) {
            ImageViewEditarPerfil.setVisibility(View.VISIBLE);
            ButtonContactar.setVisibility(View.GONE);
            RelativeLayoutComentarios.setVisibility(View.GONE);
            ObtenerUsuario();
        }
    }

    //Metodo para inicializar todos los controles que existen en el acitivy.
    //Desarrollador: Manuel E. Osorio Ochoa
    private void InicializarControles() {
        try {
            final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
            progressDialog.setCancelable(false);

            swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeRefreshLayout.setRefreshing(false);
                }
            });

            CircleImageViewNoFavorito = findViewById(R.id.CircleImageViewNoFavorito);
            CircleImageViewNoFavorito.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressDialog.show();
                    GuardarFavorito();
                }
            });
            CircleImageViewFavorito = findViewById(R.id.CircleImageViewFavorito);
            CircleImageViewFavorito.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressDialog.show();
                    EliminarFavorito();
                }
            });

            RatingBarCalificacionComentario = (RatingBar) findViewById(R.id.RatingBarCalificacionComentario);

            /*Inicio TextView Controls*/
            TxvNombreCompleto = findViewById(R.id.TextViewNombreCompleto);
            TxvCalificacion = findViewById(R.id.TextViewCalificacion);
            TxvEstado = findViewById(R.id.TextViewEstado);
            TxvCorreoElectronico = findViewById(R.id.TextViewCorreoElectronico);
            TextViewDepartamento = findViewById(R.id.TextViewDepartamento);
            TxvCelular = findViewById(R.id.TextViewCelular);
            TxvTelefono = findViewById(R.id.TextViewTelefono);
            TxvCiudad = findViewById(R.id.TextViewCiudad);
            TextViewPerfilProfesional = findViewById(R.id.TextViewPerfilProfesional);
            TextViewExperienciasLaborales = findViewById(R.id.TextViewExperienciasLaborales);
            TextViewEstudios = findViewById(R.id.TextViewEstudios);
            TextViewNombreCompletocoment = findViewById(R.id.TextViewNombreCompletocoment);
            TextViewEnviarComentario = findViewById(R.id.TextViewEnviarComentario);
            TextViewEnviarComentario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressDialog.show();
                    GuardarComentario();
                }
            });
            TextViewVerMasComentarios = findViewById(R.id.TextViewVerMasComentarios);
            TextViewVerMasComentarios.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressDialog.show();
                    gsTopComentarios = "250";
                    TextViewVerMasComentarios.setVisibility(View.GONE);
                    TextViewVerMenosComentarios.setVisibility(View.VISIBLE);
                    CargarComentariosUsuario();
                }
            });
            TextViewVerMenosComentarios = findViewById(R.id.TextViewVerMenosComentarios);
            TextViewVerMenosComentarios.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressDialog.show();
                    gsTopComentarios = "5";
                    TextViewVerMenosComentarios.setVisibility(View.GONE);
                    TextViewVerMasComentarios.setVisibility(View.VISIBLE);
                    CargarComentariosUsuario();
                }
            });
            /*Fin TextView Controls*/

            /*Inicio EditText Controls*/
            EditTextDescripcionComentario = findViewById(R.id.EditTextDescripcionComentario);
            /*Fin EditText Controls*/

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
            ButtonContactar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(PerfilActivity.this);
                    View mView = getLayoutInflater().inflate(R.layout.alert_contactar, null);

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
                    EditTextDescripcion = mView.findViewById(R.id.EditTextDescripcion);
                    LinearLayoutFechaServicio = mView.findViewById(R.id.LinearLayoutFechaServicio);

                    ButtonContratar = mView.findViewById(R.id.ButtonContratar);
                    ButtonContratar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SolicitarServicio();
                        }
                    });
                    ButtonLlamar = mView.findViewById(R.id.ButtonLlamar);
                    ButtonLlamar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            RealizarLlamada();
                        }
                    });
                    mBuilder.setView(mView).setTitle("OSS");
                    AlertDialog dialog = mBuilder.create();
                    dialog.show();
                }
            });
            /*Fin Button Controls*/
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //Metodo para cargar los datos del usuario visitado o el usuario propio iniciado.
    //Desarrollador: Manuel E. Osorio Ochoa
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
                                TxvCalificacion.setText(LstUsuario.get(i) != null ? String.valueOf(LstUsuario.get(i).getCalificacion()) : "0");
                                TxvEstado.setText(LstUsuario.get(i) != null ? LstUsuario.get(i).getEstado() : "");
                                TxvCorreoElectronico.setText(LstUsuario.get(i) != null ? LstUsuario.get(i).getEmail() : "");
                                gsCelularUsuario = LstUsuario.get(i) != null ? LstUsuario.get(i).getCelular() : "";
                                TxvCelular.setText(gsCelularUsuario);
                                TxvTelefono.setText(LstUsuario.get(i) != null ? LstUsuario.get(i).getTelefono() : "");
                                TextViewDepartamento.setText(LstUsuario.get(i) != null ? LstUsuario.get(i).getDepartamento() : "");
                                TxvCiudad.setText(LstUsuario.get(i) != null ? LstUsuario.get(i).getCiudad() : "");
                            }

                            if (!gsTokenInvitado.equals("")) {
                                ImageViewEditarPerfil.setVisibility(View.GONE);
                                ButtonContactar.setVisibility(View.VISIBLE);
                                RelativeLayoutComentarios.setVisibility(View.VISIBLE);
                                if (gsOrigen.equals("Favorito")) { CircleImageViewNoFavorito.setVisibility(View.GONE); CircleImageViewFavorito.setVisibility(View.VISIBLE); }
                                ObtenerUsuarioInvitado();
                            } else {
                                CircleImageViewNoFavorito.setVisibility(View.GONE);
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

    //Metodo para cargar los datos del usuario invitado y asi mostrarlos al momento de realizar un comentario.
    //Desarrollador: Manuel E. Osorio Ochoa
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

    //Metodo para cargar los perfiles profesionales del usuario.
    //Desarrollador: Manuel E. Osorio Ochoa
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
                            if (gsTokenInvitado.equals("")) {
                                PerfilProfesionalAdapter.setOnItemClickListener(new PerfilProfesionalAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position, String sCodigo) {
                                        Intent ObjIntent = new Intent(PerfilActivity.this, PerfilProfesionalActivity.class);
                                        ObjIntent.putExtra("Token", gsToken);
                                        ObjIntent.putExtra("Codigo", sCodigo);
                                        startActivity(ObjIntent);
                                    }
                                });
                            }
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

    //Metodo para cargar experiencias laborales del usuario.
    //Desarrollador: Manuel E. Osorio Ochoa
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
                            if (gsTokenInvitado.equals("")) {
                                ExperienciaLaboralAdapter.setOnItemClickListener(new ExperienciaLaboralAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position, String sCodigo) {
                                        Intent ObjIntent = new Intent(PerfilActivity.this, ExperienciaLaboralActivity.class);
                                        ObjIntent.putExtra("Token", gsToken);
                                        ObjIntent.putExtra("Codigo", sCodigo);
                                        startActivity(ObjIntent);

                                    }
                                });
                            }
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

    //Metodo para cargar los estudios que ha hecho en su vida el usuario y asi mostrarlos en el perfil.
    //Desarrollador: Manuel E. Osorio Ochoa
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
                            if (gsTokenInvitado.equals("")) {
                                EstudioAdapter.setOnItemClickListener(new EstudioAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position, String sCodigo) {
                                        Intent ObjIntent = new Intent(PerfilActivity.this, EstudiosActivity.class);
                                        ObjIntent.putExtra("Token", gsToken);
                                        ObjIntent.putExtra("Codigo", sCodigo);
                                        startActivity(ObjIntent);
                                    }
                                });
                            }
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

    private void SolicitarServicio() {
        try {
            ApiService apiService = APIServiceClient.getClient().create(ApiService.class);
            Servicio ObjServicio = new Servicio();

            gsFechaServicio = EditTextFechaServicio.getText() != null ? EditTextFechaServicio.getText().toString() : "";
            gsHoraServicio = EditTextHoraServicio.getText() != null ? EditTextHoraServicio.getText().toString() : "";
            gsDescripcionServicio = EditTextDescripcion.getText() != null ? EditTextDescripcion.getText().toString() : "";
            gsEstadoServicio = "";

            if (!gsFechaServicio.isEmpty() && !gsHoraServicio.isEmpty() || !gsDescripcionServicio.isEmpty()) {
                ObjServicio.setCodigoUsuario(gsTokenInvitado);
                ObjServicio.setCodigoUsuarioServicio(gsToken);
                ObjServicio.setFechaServicio(gsFechaServicio.replace("-", "") + " " + gsHoraServicio);
                ObjServicio.setDescripcion(gsEstadoServicio);
                ObjServicio.setEstado("ESTS1");

                Call call = apiService.RegistrarServicio(ObjServicio);
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        if (response.isSuccessful()) {
                            Intent ObjIntent = new Intent(PerfilActivity.this, MisServiciosActivity.class);
                            ObjIntent.putExtra("Token", gsTokenInvitado);
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
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //Metodo para guardar un favorito en la lista personalzada del usuario.
    //Desarrollador: Manuel E. Osorio Ochoa
    private void GuardarFavorito () {
        try {
            Favorito ObjFavorito = new Favorito();
            ObjFavorito.setCodigoUsuario(gsTokenInvitado);
            ObjFavorito.setCodigoUsuarioFavorito(gsToken);

            ApiService apiService = APIServiceClient.getClient().create(ApiService.class);
            Call call = apiService.RegistrarFavorito(ObjFavorito);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        CircleImageViewNoFavorito.setVisibility(View.GONE);
                        CircleImageViewFavorito.setVisibility(View.VISIBLE);
                    }
                    progressDialog.dismiss();
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

    //Metodo para eliminar un usuario favorito de tu lista personalizada.
    //Desarrollador: Manuel E. Osorio Ochoa
    private void EliminarFavorito() {
        try {
            ApiService apiService = APIServiceClient.getClient().create(ApiService.class);
            Call call = apiService.EliminarFavorito(gsTokenInvitado, gsToken);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        CircleImageViewNoFavorito.setVisibility(View.VISIBLE);
                        CircleImageViewFavorito.setVisibility(View.GONE);
                    }
                    progressDialog.dismiss();
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

    //Metodo para cargar los comentarios ya registrados del usuario.
    //Desarrollador: Manuel E. Osorio Ochoa
    private void CargarComentariosUsuario () {
        try {
            ApiService apiService = APIServiceClient.getClient().create(ApiService.class);
            Call call = apiService.ConsultarComentarios(gsToken, gsTopComentarios);
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
                            TextViewVerMasComentarios.setVisibility(View.GONE);
                            TextViewVerMenosComentarios.setVisibility(View.GONE);
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

    //Metodo para registrar un comentario al usuario visitado.
    //Desarrollador: Manuel E. Osorio Ochoa
    private void GuardarComentario () {
        try {
            gsDescripcionComentario = EditTextDescripcionComentario.getText() != null ? EditTextDescripcionComentario.getText().toString() : "";
            gsCalificacionComentario = RatingBarCalificacionComentario.getRating();

            if (!gsDescripcionComentario.isEmpty() && gsCalificacionComentario > 0) {
                Comentario ObjComentario = new Comentario();
                ObjComentario.setCodigoUsuario(gsToken);
                ObjComentario.setCodigoUsuarioResponsable(gsTokenInvitado);
                ObjComentario.setDescripcion(gsDescripcionComentario);
                ObjComentario.setCalificacion(gsCalificacionComentario);

                ApiService apiService = APIServiceClient.getClient().create(ApiService.class);
                Call call = apiService.RegistrarComentario(ObjComentario);
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        if (response.isSuccessful()) {
                            EditTextDescripcionComentario.setText("");
                            RatingBarCalificacionComentario.setRating(0);
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
            } else {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Existen campos por diligenciar, por favor verifique..", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //Metodo para realizar llamada al usuario visitado.
    //Desarrollador: Manuel E. Osorio Ochoa
    private void RealizarLlamada() {
        ObjIntentLlamada = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + gsCelularUsuario));
        if (ActivityCompat.checkSelfPermission(PerfilActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(ObjIntentLlamada);
        } else {
            ActivityCompat.requestPermissions(PerfilActivity.this, new String[] {Manifest.permission.CALL_PHONE}, SOLICITUD_PERMISO_LLAMADA);
        }
    }

    //Metodo para resivir la respues te los permisos del usuario.
    //Desarrollador: Manuel E. Osorio Ochoa
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case SOLICITUD_PERMISO_LLAMADA :
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(ObjIntentLlamada);
                }
                break;
        }
    }

    //Metodo para ir a el activity anterior y cerrar la actual.
    //Desarrollador: Manuel E. Osorio Ochoa
    @Override
    public void onBackPressed() {
        Intent ObjIntent = null;
        switch (gsOrigen) {
            case "Servicios" :
                ObjIntent = new Intent(PerfilActivity.this, UsuariosActivity.class);
                ObjIntent.putExtra("Token", !gsTokenInvitado.isEmpty() ? gsTokenInvitado : gsToken);
                startActivity(ObjIntent);
                finish();
                break;
            case "MisServicios" :
                ObjIntent = new Intent(PerfilActivity.this, MisServiciosActivity.class);
                ObjIntent.putExtra("Token", !gsTokenInvitado.isEmpty() ? gsTokenInvitado : gsToken);
                startActivity(ObjIntent);
                finish();
                break;
            case "Favorito" :
                ObjIntent = new Intent(PerfilActivity.this, MisFavoritosActivity.class);
                ObjIntent.putExtra("Token", !gsTokenInvitado.isEmpty() ? gsTokenInvitado : gsToken);
                startActivity(ObjIntent);
                finish();
                break;
            case "" :
                ObjIntent = new Intent(PerfilActivity.this, MainActivity.class);
                ObjIntent.putExtra("Token", !gsTokenInvitado.isEmpty() ? gsTokenInvitado : gsToken);
                startActivity(ObjIntent);
                finish();
                break;
        }
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
                }
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.show();
    }
}
