package com.sistemservicesonline.oss.interfaces;

import com.sistemservicesonline.oss.appcode.Comentario;
import com.sistemservicesonline.oss.appcode.Estudio;
import com.sistemservicesonline.oss.appcode.ExperienciaLaboral;
import com.sistemservicesonline.oss.appcode.Favorito;
import com.sistemservicesonline.oss.appcode.Maestros;
import com.sistemservicesonline.oss.appcode.PerfilProfesional;
import com.sistemservicesonline.oss.appcode.Servicio;
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
    @GET("OSS/Usuarios/ConsultarUsuarios/{CodigoUsuario}/{Categoria}")
    Call<List<Usuario>> ConsultarUsuarios(@Path("CodigoUsuario") String CodigoUsuario, @Path("Categoria") String Categoria);

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

    /*----------------------------------------------------------------------------------------------------------*/

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

    /*----------------------------------------------------------------------------------------------------------*/

    //Experiencias laborales
    @GET("OSS/ExperienciasLaborales/CodigoUsuario/{CodigoUsuario}")
    Call<List<ExperienciaLaboral>> ConsultarExperienciasLaborales(@Path("CodigoUsuario") String CodigoUsuario);

    @GET("OSS/ExperienciasLaborales/{Codigo}")
    Call<List<ExperienciaLaboral>> ConsultarExperienciaLaboral(@Path("Codigo") String Codigo);

    @POST("OSS/ExperienciasLaborales/")
    Call<Void> RegistrarExperienciaLaboral(@Body ExperienciaLaboral experienciaLaboral);

    @PUT("OSS/ExperienciasLaborales/{Codigo}")
    Call<Void> ActualizarExperienciaLaboral(@Path("Codigo") String codigo, @Body ExperienciaLaboral experienciaLaboral);

    @DELETE("OSS/ExperienciasLaborales/{Codigo}")
    Call<Void> EliminarExperienciaLaboral(@Path("Codigo") String Codigo);

    /*----------------------------------------------------------------------------------------------------------*/

    //Estudios
    @GET("OSS/Estudios/CodigoUsuario/{CodigoUsuario}")
    Call<List<Estudio>> ConsultarEstudios(@Path("CodigoUsuario") String CodigoUsuario);

    @GET("OSS/Estudios/{Codigo}")
    Call<List<Estudio>> ConsultarEstudio(@Path("Codigo") String Codigo);

    @POST("OSS/Estudios/")
    Call<Void> RegistrarEstudio(@Body Estudio estudio);

    @PUT("OSS/Estudios/{Codigo}")
    Call<Void> ActualizarEstudio(@Path("Codigo") String codigo, @Body Estudio estudio);

    @DELETE("OSS/Estudios/{Codigo}")
    Call<Void> EliminarEstudio(@Path("Codigo") String Codigo);

    /*----------------------------------------------------------------------------------------------------------*/

    //Favoritos
    @GET("OSS/Usuarios/Favoritos/{CodigoUsuario}")
    Call<List<Favorito>> ConsultarFavoritos(@Path("CodigoUsuario") String CodigoUsuario);

    @POST("OSS/Usuarios/Favoritos/")
    Call<Void> RegistrarFavorito(@Body Favorito favorito);

    @DELETE("OSS/Usuarios/Favoritos/{CodigoUsuario}/{CodigoUsuarioFavorito}")
    Call<Void> EliminarFavorito(@Path("CodigoUsuario") String CodigoUsuario, @Path("CodigoUsuarioFavorito") String CodigoUsuarioFavorito);

    /*----------------------------------------------------------------------------------------------------------*/

    //Comentarios
    @GET("OSS/Usuarios/Comentarios/{CodigoUsuario}/{Top}")
    Call<List<Comentario>> ConsultarComentarios(@Path("CodigoUsuario") String CodigoUsuario, @Path("Top") String Top);

    @POST("OSS/Usuarios/Comentarios/")
    Call<Void> RegistrarComentario(@Body Comentario comentario);

    @DELETE("OSS/Usuarios/Comentarios/{Codigo}")
    Call<Void> EliminarComentario(@Path("Codigo") String Codigo);

    /*----------------------------------------------------------------------------------------------------------*/

    //Servicios
    @GET("OSS/Servicios/CodigoUsuario/{CodigoUsuario}/")
    Call<List<Servicio>> ConsultarServiciosSolicitados(@Path("CodigoUsuario") String CodigoUsuario);

    @GET("OSS/Servicios/CodigoUsuario/{CodigoUsuario}/")
    Call<List<Servicio>> ConsultarServiciosSolicitudes(@Path("CodigoUsuario") String CodigoUsuario);

    @GET("OSS/Servicios/{Codigo}")
    Call<List<Servicio>> ConsultarServicio(@Path("Codigo") String Codigo);

    @POST("OSS/Servicios/")
    Call<Void> RegistrarServicio(@Body Servicio servicio);

    @PUT("OSS/Servicios/{Codigo}")
    Call<Void> ActualizarServicio(@Path("Codigo") String codigo, @Body Servicio servicio);

    @DELETE("OSS/Servicios/{Codigo}")
    Call<Void> EliminarServicio(@Path("Codigo") String Codigo);

    /*----------------------------------------------------------------------------------------------------------*/

    //Maestros
    @GET("OSS/Maestros/")
    Call<List<Maestros>> ConsultarMaestros();

    @GET("OSS/Maestros/{Nombre}")
    Call<List<Usuario>> ConsultarMaestro(@Path("Nombre") String Nombre);
}
