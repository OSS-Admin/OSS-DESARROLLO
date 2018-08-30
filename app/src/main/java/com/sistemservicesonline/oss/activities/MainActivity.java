/*
* Fecha         : 20180726
* Desarrollador : Manuel Enrique Osorio Ochoa
* Proposito     : Clase para la ventana principal de la aplicación
* © (Copyright) : OSS
* */
package com.sistemservicesonline.oss.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.TextView;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AlertDialog;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.NavigationView;

import com.sistemservicesonline.oss.appcode.Usuario;
import com.sistemservicesonline.oss.R;
import com.sistemservicesonline.oss.interfaces.ApiService;
import com.sistemservicesonline.oss.services.APIServiceClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    //region declaracion de variables
    TextView
            TextViewNombreCompleto,
            TextViewEmail;

    View
            Header;

    private String gsToken = "";
    private boolean bRegistro = false;
    private DrawerLayout drawerLayout;

    List<Usuario> LstUsuario = new ArrayList<>();

    //endregion declaracion de variables

    //region Metodos para el funcionamiento del activity

     // Fecha         : 20180726
     // Desarrollador : Manuel Enrique Osorio Ochoa
     // Proposito     : onCreate para inicializar todos los componentes del activity para su correcto funcionamiento.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbar(); // Setear Toolbar como action bar
        InicializarControles();

        gsToken = getIntent().getExtras().getString("Token") != null ? getIntent().getExtras().getString("Token").toString() : "";
        bRegistro = Boolean.parseBoolean(getIntent().getExtras().getString("bRegistro"));

        ApiService apiService = APIServiceClient.getClient().create(ApiService.class);
        Call call = apiService.ConsultarUsuario(gsToken);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    LstUsuario = (List<Usuario>) response.body();
                    if (bRegistro) {
                        Intent ObjIntent = new Intent(MainActivity.this, EditarPerfilActivity.class);
                        ObjIntent.putExtra("Token", gsToken);
                        startActivity(ObjIntent);
                    } else {
                        if (LstUsuario.size() > 0) {
                            CargarInformacionUsuario(LstUsuario);
                        } else {
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
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
    }

    private void InicializarControles() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        Header = navigationView.getHeaderView(0);
        TextViewNombreCompleto = Header.findViewById(R.id.TextViewNombreUsuario);
        TextViewEmail = Header.findViewById(R.id.TextViewEmail);
    }

    // Fecha         : 20180726
    // Desarrollador : Manuel Enrique Osorio Ochoa
    // Proposito     : CargarInformacionUsuario para cargar la informacion referente al usuario que ingresó.
    public void CargarInformacionUsuario(List<Usuario> LstUsuario) {
        try {
            if (LstUsuario != null) {
                for (int i = 0; i < LstUsuario.size(); i++) {
                    String sPrimerNombre = LstUsuario.get(i).getPrimerNombre() != null ? LstUsuario.get(i).getPrimerNombre().toString() : "";
                    String sSegundoNombre = LstUsuario.get(i).getSegundoNombre() != null ? LstUsuario.get(i).getSegundoNombre().toString() : "";
                    String sPrimerApellido = LstUsuario.get(i).getPrimerApellido() != null ? LstUsuario.get(i).getPrimerApellido().toString() : "";
                    String sSegundoApellido = LstUsuario.get(i).getSegundoApellido() != null ? LstUsuario.get(i).getSegundoApellido().toString() : "";
                    String sNombreCompleto = sSegundoNombre != "" ? sPrimerNombre + " " + sSegundoNombre + " " + sPrimerApellido + " " + sSegundoApellido : sPrimerNombre + " " + sPrimerApellido + " " + sSegundoApellido;
                    TextViewNombreCompleto.setText(sNombreCompleto);
                    TextViewEmail.setText(LstUsuario.get(i) != null ? LstUsuario.get(i).getEmail().toString() : "");
                }
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Fecha         : 20180726
    // Desarrollador : Manuel Enrique Osorio Ochoa
    // Proposito     : setToolbar para cargar la barra superior en el activity.
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ícono del drawer toggle
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }

    }

    // Fecha         : 20180726
    // Desarrollador : Manuel Enrique Osorio Ochoa
    // Proposito     : setupDrawerContent para obtener el item seleccionado y asi redireccionar hacia los activitys que se seleccionen.
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        int id = menuItem.getItemId();
                        gsToken = getIntent().getExtras().getString("Token") != null ? getIntent().getExtras().getString("Token").toString() : "";
                        if (id == R.id.nav_cuenta) {
                            Intent ObjIntent = new Intent(getApplicationContext(), PerfilActivity.class);
                            ObjIntent.putExtra("Token", gsToken);
                            startActivity(ObjIntent);
                        } else if (id == R.id.nav_buscarusuarios) {
                            Intent ObjIntent = new Intent(getApplicationContext(), UsuariosActivity.class);
                            ObjIntent.putExtra("Token", gsToken);
                            startActivity(ObjIntent);
                        } else if (id == R.id.nav_misservicios) {
                            Intent ObjIntent = new Intent(getApplicationContext(), MisServiciosActivity.class);
                            ObjIntent.putExtra("Token", gsToken);
                            startActivity(ObjIntent);
                        } else if (id == R.id.nav_misfavoritos) {
                            Intent ObjIntent = new Intent(getApplicationContext(), MisFavoritosActivity.class);
                            ObjIntent.putExtra("Token", gsToken);
                            startActivity(ObjIntent);
                        } else if (id == R.id.nav_conversaciones) {
                            Intent ObjIntent = new Intent(getApplicationContext(), ConversacionesActivity.class);
                            ObjIntent.putExtra("Token", gsToken);
                            startActivity(ObjIntent);
                        } else if (id == R.id.nav_configuracion) {
                            Intent ObjIntent = new Intent(getApplicationContext(), ConfiguracionesActivity.class);
                            ObjIntent.putExtra("Token", gsToken);
                            startActivity(ObjIntent);
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    }
                }
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Fecha         : 20180726
    // Desarrollador : Manuel Enrique Osorio Ochoa
    // Proposito     : onBackPressed para el cuadro de dialogo el cual pregunta si en realidad quieres salir de la aplicación.
    @Override
    public void onBackPressed() {
        AlertDialog.Builder mensaje = new AlertDialog.Builder(this);
        mensaje.setTitle("¿Desea Salir de la Aplicacion?");
        mensaje.setCancelable(false);
        mensaje.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        mensaje.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        mensaje.show();
    }
    //endregion Metodos
}
