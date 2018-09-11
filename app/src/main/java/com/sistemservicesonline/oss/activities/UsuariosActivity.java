package com.sistemservicesonline.oss.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.sistemservicesonline.oss.R;

public class UsuariosActivity extends AppCompatActivity {

    private String gsToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);

        gsToken = getIntent().getExtras().getString("Token") != null ? getIntent().getExtras().getString("Token").toString() : "";


        InicializarControles();
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
    }

    @Override
    public void onBackPressed() {
        Intent ObjIntent = new Intent(UsuariosActivity.this, MainActivity.class);
        ObjIntent.putExtra("Token", gsToken);
        startActivity(ObjIntent);
        finish();
    }
}
