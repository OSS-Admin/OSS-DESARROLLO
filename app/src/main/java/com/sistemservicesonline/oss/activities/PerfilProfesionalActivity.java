package com.sistemservicesonline.oss.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.sistemservicesonline.oss.R;
import com.sistemservicesonline.oss.appcode.PerfilProfesional;
import com.sistemservicesonline.oss.appcode.Usuario;
import com.sistemservicesonline.oss.interfaces.ApiService;
import com.sistemservicesonline.oss.services.APIServiceClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilProfesionalActivity extends AppCompatActivity {

    ImageView
            ImageViewEliminar;

    MaterialEditText
            EditTextCargoTitulo
            , EditTextDescripcion;

    Button
            ButtonGuardar;

    ProgressDialog
            progressDialog;

    private String gsToken = "";
    private String gsNuevo = "";
    private String gsCodigo = "";
    private String gsCargo = "";
    private String gsDescripcion = "";

    List<PerfilProfesional> LstPerfilProfesional = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_profesional);

        gsToken = getIntent().getExtras().getString("Token") != null ? getIntent().getExtras().getString("Token").toString() : "";
        gsCodigo = getIntent().getExtras().getString("Codigo") != null ? getIntent().getExtras().getString("Codigo").toString() : "";

        InicializarControles();

        if (!gsCodigo.isEmpty()) {
            GestionPerfilProfesional("Consultar");
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

        progressDialog = new ProgressDialog(PerfilProfesionalActivity.this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        EditTextCargoTitulo = findViewById(R.id.EditTextCargoTitulo);
        EditTextDescripcion = findViewById(R.id.EditTextDescripcion);

        ImageViewEliminar = findViewById(R.id.ImageViewEliminar);
        ImageViewEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { GestionPerfilProfesional("Eliminar"); }
        });
        ButtonGuardar = findViewById(R.id.ButtonGuardarPerfilProfesional);
        ButtonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gsCodigo != "") {
                    GestionPerfilProfesional("Editar");
                } else {
                    GestionPerfilProfesional("Nuevo");
                }
            }
        });
    }

    private void GestionPerfilProfesional (String sAccion) {
        try {
            progressDialog.show();
            gsCargo = EditTextCargoTitulo.getText() != null ? EditTextCargoTitulo.getText().toString() : "";
            gsDescripcion = EditTextDescripcion.getText() != null ? EditTextDescripcion.getText().toString() : "";
            ApiService apiService = APIServiceClient.getClient().create(ApiService.class);
            Call call = null;

            if (!gsToken.isEmpty() && !gsCargo.isEmpty() && !gsDescripcion.isEmpty() || sAccion.equals("Consultar")) {

                PerfilProfesional ObjPerfilProfesional  = new PerfilProfesional();
                ObjPerfilProfesional.setCodigo(gsCodigo);
                ObjPerfilProfesional.setTitulo(gsCargo);
                ObjPerfilProfesional.setDescripcion(gsDescripcion);
                ObjPerfilProfesional.setCodigoUsuario(gsToken);

                switch (sAccion) {
                    case "Consultar" :
                        call = apiService.ConsultarPerfilProfesional(gsCodigo);
                        call.enqueue(new Callback() {
                            @Override
                            public void onResponse(Call call, Response response) {
                                if (response.isSuccessful()) {
                                    LstPerfilProfesional = (List<PerfilProfesional>) response.body();
                                    if (LstPerfilProfesional.size() > 0) {
                                        for (int i = 0; i < LstPerfilProfesional.size(); i++) {
                                            EditTextCargoTitulo.setText(LstPerfilProfesional.get(i) != null ? LstPerfilProfesional.get(i).getTitulo() : "");
                                            EditTextDescripcion.setText(LstPerfilProfesional.get(i) != null ? LstPerfilProfesional.get(i).getDescripcion() : "");
                                        }
                                        progressDialog.dismiss();
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
                    case "Nuevo":
                        call = apiService.RegistrarPerfilProfesional(ObjPerfilProfesional);
                        call.enqueue(new Callback() {
                            @Override
                            public void onResponse(Call call, Response response) {
                                if (response.isSuccessful()) {
                                    Intent ObjIntent = new Intent (PerfilProfesionalActivity.this, EditarPerfilActivity.class);
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
                        break;
                    case "Editar":
                        if (!gsCodigo.isEmpty()) {
                            call = apiService.ActualizarPerfilProfesional(gsCodigo, ObjPerfilProfesional);
                            call.enqueue(new Callback() {
                                @Override
                                public void onResponse(Call call, Response response) {
                                    if (response.isSuccessful()) {
                                        Intent ObjIntent = new Intent (PerfilProfesionalActivity.this, EditarPerfilActivity.class);
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
                        }
                        break;
                    case "Eliminar":
                        if (!gsCodigo.isEmpty()) {
                            call = apiService.EliminarPerfilProfesional(gsCodigo);
                            call.enqueue(new Callback() {
                                @Override
                                public void onResponse(Call call, Response response) {
                                    if (response.isSuccessful()) {
                                        Intent ObjIntent = new Intent (PerfilProfesionalActivity.this, EditarPerfilActivity.class);
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

    @Override
    public void onBackPressed() {
        Intent ObjIntent = new Intent(PerfilProfesionalActivity.this, EditarPerfilActivity.class);
        ObjIntent.putExtra("Token", gsToken);
        startActivity(ObjIntent);
        finish();
    }
}
