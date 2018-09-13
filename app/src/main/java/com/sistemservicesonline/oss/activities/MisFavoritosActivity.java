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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sistemservicesonline.oss.R;
import com.sistemservicesonline.oss.adapters.EstudioAdapter;
import com.sistemservicesonline.oss.adapters.ExperienciaLaboralAdapter;
import com.sistemservicesonline.oss.adapters.FavoritosAdapter;
import com.sistemservicesonline.oss.adapters.UsuariosAdapter;
import com.sistemservicesonline.oss.appcode.ExperienciaLaboral;
import com.sistemservicesonline.oss.appcode.Favorito;
import com.sistemservicesonline.oss.appcode.Usuario;
import com.sistemservicesonline.oss.interfaces.ApiService;
import com.sistemservicesonline.oss.services.APIServiceClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MisFavoritosActivity extends AppCompatActivity {

    SwipeRefreshLayout
            swipeRefreshLayout;

    ProgressDialog
            progressDialog;

    RecyclerView
            ReciclerViewUsuarios;

    private FavoritosAdapter FavoritosAdapter;

    private String gsToken = "";

    List<Favorito> LstUsuario = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_favoritos);

        gsToken = getIntent().getExtras().getString("Token") != null ? getIntent().getExtras().getString("Token").toString() : "";

        InicializarControles();
        CargarUsuariosFavoritos();
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

            progressDialog = new ProgressDialog(MisFavoritosActivity.this);
            progressDialog.setMessage("Cargando...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            progressDialog.setCancelable(false);

            ReciclerViewUsuarios = findViewById(R.id.ReciclerViewUsuarios);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void CargarUsuariosFavoritos () {
        try {
            ApiService apiService = APIServiceClient.getClient().create(ApiService.class);
            Call call = apiService.ConsultarFavoritos(gsToken);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        LstUsuario = (List<Favorito>) response.body();
                        if (LstUsuario.size() > 0) {
                            FavoritosAdapter = new FavoritosAdapter(LstUsuario);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MisFavoritosActivity.this);
                            ReciclerViewUsuarios.setLayoutManager(layoutManager);
                            ReciclerViewUsuarios.setAdapter(FavoritosAdapter);
                            FavoritosAdapter.setOnItemClickListener(new FavoritosAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position, String sTokenVisitado) {
                                    Intent ObjIntent = new Intent (MisFavoritosActivity.this, PerfilActivity.class);
                                    ObjIntent.putExtra("TokenInvitado", gsToken);
                                    ObjIntent.putExtra("Token", sTokenVisitado);
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
}
