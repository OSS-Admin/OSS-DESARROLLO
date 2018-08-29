package com.sistemservicesonline.oss.interfaces;

import com.sistemservicesonline.oss.appcode.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("OSS/Usuarios/")
    Call<List<Usuario>> ConsultarUsuarios();

    @GET("OSS/Usuarios/{CodigoUsuario}")
    Call<List<Usuario>> ConsultarUsuario(@Path("CodigoUsuario") String CodigoUsuario);
}
