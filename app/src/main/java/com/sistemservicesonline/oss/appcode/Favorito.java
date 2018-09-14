package com.sistemservicesonline.oss.appcode;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Favorito {

    @SerializedName("CodigoUsuario")
    @Expose
    private String codigoUsuario;
    @SerializedName("CodigoUsuarioFavorito")
    @Expose
    private String codigoUsuarioFavorito;
    @SerializedName("PrimerNombre")
    @Expose
    private String primerNombre;
    @SerializedName("SegundoNombre")
    @Expose
    private String segundoNombre;
    @SerializedName("PrimerApellido")
    @Expose
    private String primerApellido;
    @SerializedName("SegundoApellido")
    @Expose
    private String segundoApellido;
    @SerializedName("PerfilProfesional")
    @Expose
    private String perfilProfesional;

    public String getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(String codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public String getCodigoUsuarioFavorito() {
        return codigoUsuarioFavorito;
    }

    public void setCodigoUsuarioFavorito(String codigoUsuarioFavorito) {
        this.codigoUsuarioFavorito = codigoUsuarioFavorito;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getPerfilProfesional() {
        return perfilProfesional;
    }

    public void setPerfilProfesional(String perfilProfesional) {
        this.perfilProfesional = perfilProfesional;
    }

}