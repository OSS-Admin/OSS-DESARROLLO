package com.sistemservicesonline.oss.interfaces;

import com.sistemservicesonline.oss.appcode.Maestros;
import com.sistemservicesonline.oss.appcode.PerfilProfesional;
import com.sistemservicesonline.oss.appcode.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    //Usuarios
    @GET("OSS/Usuarios/")
    Call<List<Usuario>> ConsultarUsuarios();

    @GET("OSS/Usuarios/{CodigoUsuario}")
    Call<List<Usuario>> ConsultarUsuario(@Path("CodigoUsuario") String CodigoUsuario);

    @GET("OSS/Usuarios/Login/{sCorreo}/{sContrasena}")
    Call<List<Usuario>> Login(@Path("sCorreo") String sCorreo, @Path("sContrasena") String sContrasena);

    @GET("OSS/Usuarios/ValidarCorreo/{sCorreo}")
    Call<List<Usuario>> ValidarCorreo(@Path("sCorreo") String sCorreo);

    @POST("OSS/Usuarios/")
    Call<Void> RegistrarUsuario(@Body Usuario usuario);

    @PUT("OSS/Usuarios/{CodigoUsuario}")
    Call<Void> ActualizarUsuario(@Path("CodigoUsuario") String CodigoUsuario, @Body Usuario usuario);

    @DELETE("OSS/Usuarios/{CodigoUsuario}")
    Call<Void> EliminarUsuario(@Query("CodigoUsuario") String CodigoUsuario);

    //PerfilesProfesionales
    @GET("OSS/PerfilesProfesionales/CodigoUsuario/{CodigoUsuario}")
    Call<List<PerfilProfesional>> ConsultarPerfilesProfesionales(@Path("CodigoUsuario") String CodigoUsuario);

    @GET("OSS/PerfilesProfesionales/{Codigo}")
    Call<List<PerfilProfesional>> ConsultarPerfilProfesional(@Path("Codigo") String Codigo);

    @POST("OSS/PerfilesProfesionales/")
    Call<Void> RegistrarPerfilProfesional(@Body PerfilProfesional perfilProfesional);

    @PUT("OSS/PerfilesProfesionales/{Codigo}")
    Call<Void> ActualizarPerfilProfesional(@Path("Codigo") String codigo, @Body PerfilProfesional perfilProfesional);

    @DELETE("OSS/PerfilesProfesionales/{Codigo}")
    Call<Void> EliminarPerfilProfesional(@Path("Codigo") String Codigo);

    //Maestros
    @GET("OSS/Maestros/")
    Call<List<Maestros>> ConsultarMaestros();

    @GET("OSS/Maestros/{Nombre}")
    Call<List<Usuario>> ConsultarMaestro(@Path("Nombre") String Nombre);
}
