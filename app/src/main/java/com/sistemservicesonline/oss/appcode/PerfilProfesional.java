package com.sistemservicesonline.oss.appcode;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PerfilProfesional {

    @SerializedName("Codigo")
    @Expose
    private String codigo;
    @SerializedName("Titulo")
    @Expose
    private String titulo;
    @SerializedName("Descripcion")
    @Expose
    private String descripcion;
    @SerializedName("CodigoUsuario")
    @Expose
    private String codigoUsuario;

    public PerfilProfesional() {
    }

    public PerfilProfesional(String codigo, String titulo, String descripcion, String codigoUsuario) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.codigoUsuario = codigoUsuario;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(String codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

}