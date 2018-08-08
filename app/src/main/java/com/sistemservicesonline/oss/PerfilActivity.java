package com.sistemservicesonline.oss;

import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sistemservicesonline.oss.App_Code.GestionUsuarios.Usuario;
import com.sistemservicesonline.oss.App_Code.Utilidades;
import com.sistemservicesonline.oss.Funcionales.Usuarios.EditarPerfilActivity;

public class PerfilActivity extends AppCompatActivity {
    TextView
              TextViewNombreUsuario
            , TextViewEmail
            , TxvNombreCompleto
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

    private String gsToken = "";
    private String sTokenInvitado = "";

    private DrawerLayout drawerLayout;
    RatingBar ratingBar;
    View Header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        setToolbar(); // Setear Toolbar como action bar

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        Header = navigationView.getHeaderView(0);
        TextViewNombreUsuario = Header.findViewById(R.id.TextViewNombreUsuario);
        TextViewEmail = Header.findViewById(R.id.TextViewEmail);

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
        /*Fin ImageView Controls*/

        /*Inicio LinearLayout Controls*/
        RelativeLayoutComentarios = findViewById(R.id.RelativeLayoutComentarios);
        /*Fin LinearLayout Controls*/

        sTokenInvitado = getIntent().getExtras().getString("TokenInvitado") != null ? getIntent().getExtras().getString("TokenInvitado").toString() : "";
        if (sTokenInvitado != "") {
            ImageViewEditarPerfil.setVisibility(View.GONE);
        } else {
            ImageViewEditarPerfil.setVisibility(View.VISIBLE);
        }

        gsToken = getIntent().getExtras().getString("Token") != null ? getIntent().getExtras().getString("Token").toString() : "";
        CargarInformacionUsuario(gsToken);
    }

    public void CargarInformacionUsuario(String sToken) {
        Utilidades ObjUtilidades = new Utilidades();
        Usuario ObjUsuario = null;

        try {
            if (!sToken.equals("")) {
                ObjUsuario = ObjUtilidades.ConsultarUsuario(sToken);

                if (ObjUsuario != null) {
                    TextViewNombreUsuario.setText(ObjUsuario.sNombreCompleto != null ? ObjUsuario.sNombreCompleto.toString() : "");
                    TextViewEmail.setText(ObjUsuario.sEmail != null ? ObjUsuario.sEmail.toString() : "");
                    TxvNombreCompleto.setText(ObjUsuario.sNombreCompleto != null ? ObjUsuario.sNombreCompleto.toString() : "");
                    TxvCorreoElectronico.setText(ObjUsuario.sEmail != null ? ObjUsuario.sEmail.toString() : "");
                    TxvCelular.setText(ObjUsuario.sCelular != null ? ObjUsuario.sCelular.toString() : "");
                    TxvTelefono.setText(ObjUsuario.sTelefono != null ? ObjUsuario.sTelefono.toString() : "");
                    TxvCiudad.setText(ObjUsuario.sCiudad != null ? ObjUsuario.sCiudad.toString() : "");
                }
            } else {
                Toast.makeText(getApplicationContext(), "La sesión esta inactiva.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


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

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        int id = menuItem.getItemId();

                        gsToken = getIntent().getExtras().getString("Token") != null ? getIntent().getExtras().getString("Token").toString() : "";
                        if (id == R.id.nav_home) {
                            Intent ObjIntent = new Intent(getApplicationContext(), MainActivity.class);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //codigo adicional
        this.finish();
    }
}
