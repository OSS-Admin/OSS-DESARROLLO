package com.sistemservicesonline.oss.activities;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
            , TxvCiudad;

    ImageView
              ImageViewEditarPerfil;

    RelativeLayout
            RelativeLayoutComentarios;

    SwipeRefreshLayout
            swipeRefreshLayout;

    Button
            ButtonContactar;

    private String gsToken = "";
    private String sTokenInvitado = "";

    RatingBar ratingBar;
    View Header;

    List<Usuario> LstUsuario = new ArrayList<>();
    List<Usuario> LstUsuarioInvitado = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        gsToken = getIntent().getExtras().getString("Token") != null ? getIntent().getExtras().getString("Token").toString() : "";
        sTokenInvitado = getIntent().getExtras().getString("TokenInvitado") != null ? getIntent().getExtras().getString("TokenInvitado").toString() : "";

        InicializarControles();
        if (!gsToken.equals("")) {
            ImageViewEditarPerfil.setVisibility(View.VISIBLE);
            ButtonContactar.setVisibility(View.GONE);
            RelativeLayoutComentarios.setVisibility(View.GONE);
            ObtenerUsuario(gsToken, false);
        }

        if (!sTokenInvitado.equals("")) {
            ImageViewEditarPerfil.setVisibility(View.GONE);
            ObtenerUsuario(gsToken, true);
        }
    }

    private void ObtenerUsuario (String sToken, final Boolean bInvitado) {
        try {
            ApiService apiService = APIServiceClient.getClient().create(ApiService.class);
            Call call = apiService.ConsultarUsuario(sToken);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        if (!bInvitado) {
                            LstUsuario = (List<Usuario>) response.body();
                            if (LstUsuario.size() > 0) {
                                CargarInformacionUsuario(LstUsuario);
                            } else {
                                Toast.makeText(getApplicationContext(),response.message().toString(),Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            LstUsuarioInvitado = (List<Usuario>) response.body();
                            if (LstUsuario.size() > 0) {
                                CargarInformacionUsuarioInvitado(LstUsuarioInvitado);
                            } else {
                                Toast.makeText(getApplicationContext(),response.message().toString(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),response.message().toString(),Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call call, Throwable t) {
                    Toast.makeText(getApplicationContext(),t.toString(),Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void InicializarControles() {
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
            /*Fin TextView Controls*/

            /*Inicio ImageView Controls*/
            ImageViewEditarPerfil = findViewById(R.id.ImageViewEditarPerfil);
            ImageViewEditarPerfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent ObjIntent = new Intent (PerfilActivity.this, EditarPerfilActivity.class);
                    ObjIntent.putExtra("Token", gsToken);
                    startActivityForResult(ObjIntent, 0);
                }
            });
            /*Fin ImageView Controls*/

            /*Inicio LinearLayout Controls*/
            RelativeLayoutComentarios = findViewById(R.id.RelativeLayoutComentarios);
            /*Fin LinearLayout Controls*/

            /*Inicio Button Controls*/
            ButtonContactar = findViewById(R.id.ButtonContactar);
            /*Fin Button Controls*/
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void CargarInformacionUsuario(List<Usuario> LstUsuario) {
        try {
            if (LstUsuario != null) {
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
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void CargarInformacionUsuarioInvitado(List<Usuario> LstUsuarioInvitado) {
        try {
            if (LstUsuarioInvitado != null) {
                for (int i = 0; i < LstUsuarioInvitado.size(); i++) {
                    //TxvNombreCompleto.setText(LstUsuarioInvitado.get(i) != null ? LstUsuarioInvitado.get(i).getNombreCompleto() : "");
                }
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
