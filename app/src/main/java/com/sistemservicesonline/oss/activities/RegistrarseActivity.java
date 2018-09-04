package com.sistemservicesonline.oss.activities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.sistemservicesonline.oss.appcode.Maestros;
import com.sistemservicesonline.oss.appcode.Usuario;
import com.sistemservicesonline.oss.R;
import com.sistemservicesonline.oss.interfaces.ApiService;
import com.sistemservicesonline.oss.services.APIServiceClient;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrarseActivity extends AppCompatActivity {

    Button
            ButtonRegistrarse;

    MaterialEditText
            EditTextPrimerNombre,
            EditTextSegundoNombre,
            EditTextPrimerApellido,
            EditTextSegundoApellido,
            EditTextIdentificacion,
            EditTextFechaNacimiento,
            EditTextCelular,
            EditTextCorreoElectronico,
            EditTextContrasena,
            EditTextConfirmarContrasena;

    MaterialBetterSpinner
            MaterialBetterSpinnerTipoIdentificacion,
            MaterialBetterSpinnerCategoria,
            MaterialBetterSpinnerGenero;

    TextView
            TextViewCambiarFoto;

    ImageView
            ImageViewFotoPerfil,
            ImageViewCambiarFechaNacimiento;

    private String gsToken = "";
    private static final String CERO = "0";
    private static final String BARRA = "-";

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();
    SimpleDateFormat ObjSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR) - 18;
    final int iCodigoFotoSeleccionada = 10;
    final int iCodigoFotoTomada = 20;

    String gsEdad = "";

    List<Usuario> LstUsuario = new ArrayList<>();
    List<Maestros> LstMaestros = new ArrayList<>();
    List<String> LstMaestrosCodigos = new ArrayList<>();
    ArrayList<String> LstTiposIdentificacion = new ArrayList<>();
    ArrayList<String> LstGeneros = new ArrayList<>();
    ArrayList<String> LstCategorias = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        InicializarControles();
        ObtenerMaestros();
    }

    private void InicializarControles() {
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            ImageViewFotoPerfil = findViewById(R.id.ImageViewFotoPerfil);
            ImageViewCambiarFechaNacimiento = findViewById(R.id.ImageViewCambiarFechaNacimiento);
            ImageViewCambiarFechaNacimiento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    obtenerFecha();
                }
            });

            TextViewCambiarFoto = findViewById(R.id.TextViewCambiarFoto);
            TextViewCambiarFoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CargarImagenPerfil();
                }
            });

            /*Buttons*/
            ButtonRegistrarse = findViewById(R.id.ButtonRegistrar);
            ButtonRegistrarse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ValidarRegistroUsuario();
                }
            });

            EditTextIdentificacion = findViewById(R.id.EditTextIdentificacion);
            EditTextPrimerNombre = findViewById(R.id.EditTextPrimerNombre);
            EditTextSegundoNombre = findViewById(R.id.EditTextSegundoNombre);
            EditTextPrimerApellido = findViewById(R.id.EditTextPrimerApellido);
            EditTextSegundoApellido = findViewById(R.id.EditTextSegundoApellido);
            EditTextFechaNacimiento = findViewById(R.id.EditTextFechaNacimiento);
            EditTextCelular = findViewById(R.id.EditTextCelular);
            EditTextCorreoElectronico = findViewById(R.id.EditTextCorreoElectronico);
            EditTextContrasena = findViewById(R.id.EditTextContrasena);
            EditTextConfirmarContrasena = findViewById(R.id.EditTextConfirmarContrasena);

            /*Inicio MaterialBetterSpinner*/
            MaterialBetterSpinnerTipoIdentificacion = findViewById(R.id.MaterialBetterSpinnerTipoIdentificacion);
            MaterialBetterSpinnerTipoIdentificacion.setAdapter(new ArrayAdapter<String>(RegistrarseActivity.this, android.R.layout.simple_dropdown_item_1line, LstTiposIdentificacion));

            MaterialBetterSpinnerCategoria = findViewById(R.id.MaterialBetterSpinnerCategoria);
            MaterialBetterSpinnerCategoria.setAdapter(new ArrayAdapter<String>(RegistrarseActivity.this, android.R.layout.simple_dropdown_item_1line, LstCategorias));

            MaterialBetterSpinnerGenero = findViewById(R.id.MaterialBetterSpinnerGenero);
            MaterialBetterSpinnerGenero.setAdapter(new ArrayAdapter<String>(RegistrarseActivity.this, android.R.layout.simple_dropdown_item_1line, LstCategorias));
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void ObtenerMaestros () {
        try {
            ApiService apiService = APIServiceClient.getClient().create(ApiService.class);
            Call call = apiService.ConsultarMaestros();
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        LstMaestros = (List<Maestros>) response.body();
                        if (LstMaestros.size() > 0) {
                            for (int i = 0; i < LstMaestros.size(); i++) {
                                LstMaestrosCodigos.add(LstMaestros.get(i).getCodigo() != null && LstMaestros.get(i).getNombre() != null ? LstMaestros.get(i).getCodigo().toString() + "-" + LstMaestros.get(i).getNombre().toString() : "");
                                if (LstMaestros.get(i).getTipo().equals("TipoIdentificacion")) {
                                    LstTiposIdentificacion.add(LstMaestros.get(i).getNombre() != null ? LstMaestros.get(i).getNombre().toString() : "");
                                } else if (LstMaestros.get(i).getTipo().equals("Categorias")) {
                                    LstCategorias.add(LstMaestros.get(i).getNombre() != null ? LstMaestros.get(i).getNombre().toString() : "");
                                } else if (LstMaestros.get(i).getTipo().equals("Generos")) {
                                    LstGeneros.add(LstMaestros.get(i).getNombre() != null ? LstMaestros.get(i).getNombre().toString() : "");
                                }
                            }
                            MaterialBetterSpinnerTipoIdentificacion.setAdapter(new ArrayAdapter<String>(RegistrarseActivity.this, android.R.layout.simple_dropdown_item_1line, LstTiposIdentificacion));
                            MaterialBetterSpinnerCategoria.setAdapter(new ArrayAdapter<String>(RegistrarseActivity.this, android.R.layout.simple_dropdown_item_1line, LstCategorias));
                            MaterialBetterSpinnerGenero.setAdapter(new ArrayAdapter<String>(RegistrarseActivity.this, android.R.layout.simple_dropdown_item_1line, LstGeneros));
                        } else {
                            Toast.makeText(getApplicationContext(),response.message().toString(),Toast.LENGTH_SHORT).show();
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

    private static String ObtenerCodigoMaestros (List<String> LstMaestrosCodigos, String sNombreMaestro) {
        try {
            if (!LstMaestrosCodigos.equals("") && !sNombreMaestro.equals("")){
                for (int i = 0; i < LstMaestrosCodigos.size(); i++) {
                    String sMaestro = LstMaestrosCodigos.get(i) != null ? LstMaestrosCodigos.get(i).toString() : "";
                    String sNombre = sMaestro.split("-")[1] != null ? sMaestro.split("-")[1].toString() : "";
                    if (sNombreMaestro.equals(sNombre)) {
                        String sCodigo = sMaestro.split("-")[0] != null ? sMaestro.split("-")[0].toString() : "";
                        return sCodigo;
                    }
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return "";
    }

    private void ValidarRegistroUsuario() {
        try {
            String sTipoIdentificacion = ObtenerCodigoMaestros(LstMaestrosCodigos, MaterialBetterSpinnerTipoIdentificacion.getText() != null ? MaterialBetterSpinnerTipoIdentificacion.getText().toString() : "");
            String sIdentificacion = EditTextIdentificacion.getText() != null ? EditTextIdentificacion.getText().toString() : "";
            String sPrimerNombre = EditTextPrimerNombre.getText() != null ? EditTextPrimerNombre.getText().toString() : "";
            String sPrimerApellido = EditTextPrimerApellido.getText() != null ? EditTextPrimerApellido.getText().toString() : "";
            String sSegundoApellido = EditTextSegundoApellido.getText() != null ? EditTextSegundoApellido.getText().toString() : "";
            String sFechaNacimiento = EditTextFechaNacimiento.getText() != null ? EditTextFechaNacimiento.getText().toString().replace("-", "/") : "";
            String sCategoria = ObtenerCodigoMaestros(LstMaestrosCodigos, MaterialBetterSpinnerCategoria.getText() != null ? MaterialBetterSpinnerCategoria.getText().toString()  : "");
            String sGenero = ObtenerCodigoMaestros(LstMaestrosCodigos, MaterialBetterSpinnerGenero.getText() != null ? MaterialBetterSpinnerGenero.getText().toString()  : "");
            String sCelular = EditTextCelular.getText() != null ? EditTextCelular.getText().toString() : "";
            String sEmail = EditTextCorreoElectronico.getText() != null ? EditTextCorreoElectronico.getText().toString() : "";
            String sContrasena = EditTextContrasena.getText() != null ? EditTextContrasena.getText().toString() : "";
            String sContrasenaConfirmada = EditTextConfirmarContrasena.getText() != null ? EditTextConfirmarContrasena.getText().toString() : "";

            if (sTipoIdentificacion.equals("")) { Toast.makeText(getApplicationContext(), "Existen campos por diligenciar, por favor verifique.", Toast.LENGTH_SHORT).show(); return; }
            if (sIdentificacion.equals("")) { Toast.makeText(getApplicationContext(), "Existen campos por diligenciar, por favor verifique.", Toast.LENGTH_SHORT).show(); return; }
            if (sPrimerNombre.equals("")) { Toast.makeText(getApplicationContext(), "Existen campos por diligenciar, por favor verifique.", Toast.LENGTH_SHORT).show(); return; }
            if (sPrimerApellido.equals("")) { Toast.makeText(getApplicationContext(), "Existen campos por diligenciar, por favor verifique.", Toast.LENGTH_SHORT).show(); return; }
            if (sSegundoApellido.equals("")) { Toast.makeText(getApplicationContext(), "Existen campos por diligenciar, por favor verifique.", Toast.LENGTH_SHORT).show(); return; }
            if (sFechaNacimiento.equals("")) { Toast.makeText(getApplicationContext(), "Existen campos por diligenciar, por favor verifique.", Toast.LENGTH_SHORT).show(); return; }
            if (sCategoria.equals("")) { Toast.makeText(getApplicationContext(), "Existen campos por diligenciar, por favor verifique.", Toast.LENGTH_SHORT).show(); return; }
            if (sGenero.equals("")) { Toast.makeText(getApplicationContext(), "Existen campos por diligenciar, por favor verifique.", Toast.LENGTH_SHORT).show(); return; }
            if (sCelular.equals("")) { Toast.makeText(getApplicationContext(), "Existen campos por diligenciar, por favor verifique.", Toast.LENGTH_SHORT).show(); return; }
            if (sEmail.equals("")) { Toast.makeText(getApplicationContext(), "Existen campos por diligenciar, por favor verifique.", Toast.LENGTH_SHORT).show(); return; }
            if (sContrasena.equals("")) { Toast.makeText(getApplicationContext(), "Existen campos por diligenciar, por favor verifique.", Toast.LENGTH_SHORT).show(); return; }
            if (sContrasenaConfirmada.equals("")) { Toast.makeText(getApplicationContext(), "Existen campos por diligenciar, por favor verifique.", Toast.LENGTH_SHORT).show(); return; }

            boolean bEmailValido = ValidarEmail(sEmail);
            if (!bEmailValido) {
                Toast.makeText(getApplicationContext(), "El correo electrónico es invalido.", Toast.LENGTH_SHORT).show();
                EditTextCorreoElectronico.requestFocus();
                return;
            } else {
                EditTextCorreoElectronico.clearFocus();
            }

            if (sContrasena.equals(sContrasenaConfirmada)) {
                ApiService apiService = APIServiceClient.getClient().create(ApiService.class);
                Call call = apiService.ValidarCorreo(sEmail);
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        if (response.isSuccessful()) {
                            LstUsuario = (List<Usuario>) response.body();
                            if (!LstUsuario.equals(null) && LstUsuario.size() > 0) {
                                Toast.makeText(getApplicationContext(), "El correo electrónico ingresado ya existe en OSS.", Toast.LENGTH_SHORT).show();
                            } else {
                                RegistroUsuario();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Log.d("", t.getMessage());
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error en RegistrarUsuario() : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void RegistroUsuario () {
        try {
            gsToken = UUID.randomUUID().toString().toUpperCase();
            String sTipoIdentificacion = ObtenerCodigoMaestros(LstMaestrosCodigos, MaterialBetterSpinnerTipoIdentificacion.getText() != null ? MaterialBetterSpinnerTipoIdentificacion.getText().toString() : "");
            String sIdentificacion = EditTextIdentificacion.getText() != null ? EditTextIdentificacion.getText().toString() : "";
            String sPrimerNombre = EditTextPrimerNombre.getText() != null ? EditTextPrimerNombre.getText().toString() : "";
            String sSegundoNombre = EditTextSegundoNombre.getText() != null ? EditTextSegundoNombre.getText().toString() : "";
            String sPrimerApellido = EditTextPrimerApellido.getText() != null ? EditTextPrimerApellido.getText().toString() : "";
            String sSegundoApellido = EditTextSegundoApellido.getText() != null ? EditTextSegundoApellido.getText().toString() : "";
            String sFechaNacimiento = EditTextFechaNacimiento.getText() != null ? EditTextFechaNacimiento.getText().toString().replace("-", "/") : "";
            int iEdad = gsEdad != "" ? Integer.parseInt(gsEdad.split(" ")[0].toString())  : 0;
            String sCategoria = ObtenerCodigoMaestros(LstMaestrosCodigos, MaterialBetterSpinnerCategoria.getText() != null ? MaterialBetterSpinnerCategoria.getText().toString()  : "");
            String sGenero = ObtenerCodigoMaestros(LstMaestrosCodigos, MaterialBetterSpinnerGenero.getText() != null ? MaterialBetterSpinnerGenero.getText().toString()  : "");
            String sCelular = EditTextCelular.getText() != null ? EditTextCelular.getText().toString() : "";
            String sEmail = EditTextCorreoElectronico.getText() != null ? EditTextCorreoElectronico.getText().toString() : "";
            String sEstado = ObtenerCodigoMaestros(LstMaestrosCodigos, "Disponible");
            Boolean bActivo = true;
            String sContrasena = EncriptarContrasena(EditTextContrasena.getText() != null ? EditTextContrasena.getText().toString() : "", "MD5");

            Usuario ObjUsuario = new Usuario();
            ObjUsuario.setCodigoUsuario(gsToken);
            ObjUsuario.setTipoIdentificacion(sTipoIdentificacion);
            ObjUsuario.setIdentificacion(sIdentificacion);
            ObjUsuario.setPrimerNombre(sPrimerNombre);
            ObjUsuario.setSegundoNombre(sSegundoNombre);
            ObjUsuario.setPrimerApellido(sPrimerApellido);
            ObjUsuario.setSegundoApellido(sSegundoApellido);
            ObjUsuario.setFechaNacimiento(sFechaNacimiento);
            ObjUsuario.setEdad(iEdad);
            ObjUsuario.setCategoria(sCategoria);
            ObjUsuario.setGenero(sGenero);
            ObjUsuario.setCelular(sCelular);
            ObjUsuario.setEmail(sEmail);
            ObjUsuario.setEstado(sEstado);
            ObjUsuario.setActivo(bActivo);
            ObjUsuario.setContrasena(sContrasena);
            ApiService apiService = APIServiceClient.getClient().create(ApiService.class);
            Call call = apiService.RegistrarUsuario(ObjUsuario);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        Intent ObjIntent = new Intent(RegistrarseActivity.this, MainActivity.class);
                        ObjIntent.putExtra("Token", gsToken);
                        ObjIntent.putExtra("bRegistro", "true");
                        startActivity(ObjIntent);
                    } else {
                        Log.d("", response.message());
                    }
                }
                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.d("", t.getMessage());
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error en Registro() : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void CargarImagenPerfil() {
        final CharSequence[] opciones = {"Tomar Foto", "Cargar Foto", "Cancelar"};
        final AlertDialog.Builder AlertOpciones = new AlertDialog.Builder(RegistrarseActivity.this);
        AlertOpciones.setTitle("Seleccione una opción");
        AlertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")) {
                    Intent ObjIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (ObjIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(ObjIntent, iCodigoFotoTomada);
                    }
                } else {
                    if (opciones[i].equals("Cargar Foto")) {
                        Intent ObjPictureIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        ObjPictureIntent.setType("image/");
                        startActivityForResult(Intent.createChooser(ObjPictureIntent, "Seleccione la aplicación"), iCodigoFotoSeleccionada);
                    } else {
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        AlertOpciones.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case iCodigoFotoSeleccionada:
                    Uri PathFotoSeleccionada = data.getData();
                    ImageViewFotoPerfil.setImageURI(PathFotoSeleccionada);
                    break;
                case iCodigoFotoTomada:
                    Bundle extras = data.getExtras();
                    Bitmap bitmap = (Bitmap) extras.get("data");
                    ImageViewFotoPerfil.setImageBitmap(bitmap);
                    break;
            }
        }
    }

    private void obtenerFecha(){
        DatePickerDialog recogerFecha = new DatePickerDialog(this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                try {
                    final int mesActual = month + 1;
                    String diaFormateado = (dayOfMonth < 10) ? CERO + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                    String mesFormateado = (mesActual < 10) ? CERO + String.valueOf(mesActual) : String.valueOf(mesActual);
                    EditTextFechaNacimiento.setText(year + BARRA + mesFormateado + BARRA + diaFormateado);
                    gsEdad = ObtenerEdad(ObjSimpleDateFormat.parse(year + BARRA + mesFormateado + BARRA + diaFormateado));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        },anio, mes, dia);
        recogerFecha.show();
    }

    private static String ObtenerEdad(Date fechaNacimiento) {
        if (fechaNacimiento != null) {
            StringBuilder result = new StringBuilder();
            Calendar c = new GregorianCalendar();
            c.setTime(fechaNacimiento);
            int sEdad = calcularEdad(c);
            result.append(sEdad);
            result.append(sEdad != 1 ? " años" : " año");
            return result.toString();
        }
        return "";
    }

    private static int calcularEdad(Calendar fechaNac) {
        Calendar today = Calendar.getInstance();
        int diffYear = today.get(Calendar.YEAR) - fechaNac.get(Calendar.YEAR);
        int diffMonth = today.get(Calendar.MONTH) - fechaNac.get(Calendar.MONTH);
        int diffDay = today.get(Calendar.DAY_OF_MONTH) - fechaNac.get(Calendar.DAY_OF_MONTH);
        if (diffMonth < 0 || (diffMonth == 0 && diffDay < 0)) {
            diffYear = diffYear - 1;
        }
        return diffYear;
    }

    public static String EncriptarContrasena(String txt, String hashType) {
        try {
            MessageDigest md = java.security.MessageDigest.getInstance(hashType);
            byte[] array = md.digest(txt.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
                        .substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static boolean ValidarEmail(String sEmail) {
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher mather = pattern.matcher(sEmail);

        if (mather.find() == true) {
            return true;
        } else {
            return false;
        }
    }
}
