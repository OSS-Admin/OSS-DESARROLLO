package com.sistemservicesonline.oss.appcode;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comentario {

    @SerializedName("Codigo")
    @Expose
    private String codigo;
    @SerializedName("CodigoUsuario")
    @Expose
    private String codigoUsuario;
    @SerializedName("CodigoUsuarioResponsable")
    @Expose
    private String codigoUsuarioResponsable;
    @SerializedName("NombreResponsable")
    @Expose
    private String nombreResponsable;
    @SerializedName("Descripcion")
    @Expose
    private String descripcion;
    @SerializedName("Calificacion")
    @Expose
    private Float calificacion;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(String codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public String getCodigoUsuarioResponsable() {
        return codigoUsuarioResponsable;
    }

    public void setCodigoUsuarioResponsable(String codigoUsuarioResponsable) {
        this.codigoUsuarioResponsable = codigoUsuarioResponsable;
    }

    public String getNombreResponsable() {
        return nombreResponsable;
    }

    public void setNombreResponsable(String nombreResponsable) {
        this.nombreResponsable = nombreResponsable;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Float getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Float calificacion) {
        this.calificacion = calificacion;
    }

}