/*
* Fecha         : 20180726
* Desarrollador : Manuel Enrique Osorio Ochoa
* Proposito     : Clase para la ventana principal de la aplicación
* © (Copyright) : OSS
* */
package com.sistemservicesonline.oss;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.View;
import android.widget.Toast;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.ListView;
import android.widget.TextView;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AlertDialog;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.NavigationView;

import com.sistemservicesonline.oss.App_Code.Utilidades;
import com.sistemservicesonline.oss.App_Code.GestionUsuarios.Usuario;
import com.sistemservicesonline.oss.Funcionales.Usuarios.EditarPerfilActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    //region declaracion de variables
    TextView
            TextViewNombreCompleto,
            TextViewEmail;

    View
            Header;

    ListView
            ListViewUsuarios;

    private String gsToken = "";
    private boolean bRegistro = false;
    private DrawerLayout drawerLayout;
    int mNotificacionId = 01;

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

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        Header = navigationView.getHeaderView(0);
        TextViewNombreCompleto = Header.findViewById(R.id.TextViewNombreUsuario);
        TextViewEmail = Header.findViewById(R.id.TextViewEmail);

        gsToken = getIntent().getExtras().getString("Token") != null ? getIntent().getExtras().getString("Token").toString() : "";
        bRegistro = Boolean.parseBoolean(getIntent().getExtras().getString("bRegistro"));
        if (bRegistro) {
            Intent ObjIntent = new Intent(MainActivity.this, EditarPerfilActivity.class);
            ObjIntent.putExtra("Token", gsToken);
            startActivity(ObjIntent);
        } else {
            CargarInformacionUsuario(gsToken);
        }
    }

    // Fecha         : 20180726
    // Desarrollador : Manuel Enrique Osorio Ochoa
    // Proposito     : CargarInformacionUsuario para cargar la informacion referente al usuario que ingresó.
    public void CargarInformacionUsuario(String sToken) {
        Utilidades ObjUtilidades = new Utilidades();
        Usuario ObjUsuario = null;

        try {
            if (!sToken.equals("")) {
                ObjUsuario = ObjUtilidades.ConsultarUsuario(sToken);

                if (ObjUsuario != null) {
                    String sNombreCompleto = ObjUsuario.sNombreCompleto != null ? ObjUsuario.sNombreCompleto.toString() : "";
                    TextViewNombreCompleto.setText(sNombreCompleto);
                    TextViewEmail.setText(ObjUsuario.sEmail != null ? ObjUsuario.sEmail.toString() : "");
                }
            } else {
                Toast.makeText(getApplicationContext(), "La sesión esta inactiva.", Toast.LENGTH_SHORT).show();
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
                        }

                        drawerLayout.closeDrawer(GravityCompat.START);

                        return true;
                    }
                }
        );
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            getMenuInflater().inflate(R.menu.nav_menu, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }*/

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
