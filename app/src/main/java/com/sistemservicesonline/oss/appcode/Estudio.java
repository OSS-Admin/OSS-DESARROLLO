package com.sistemservicesonline.oss.appcode;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Estudio {

    @SerializedName("Codigo")
    @Expose
    private String codigo;
    @SerializedName("Institucion")
    @Expose
    private String institucion;
    @SerializedName("FechaInicial")
    @Expose
    private String fechaInicial;
    @SerializedName("FechaFinal")
    @Expose
    private String fechaFinal;
    @SerializedName("Actualmente")
    @Expose
    private Boolean actualmente;
    @SerializedName("Descripcion")
    @Expose
    private String descripcion;
    @SerializedName("CodigoUsuario")
    @Expose
    private String codigoUsuario;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getInstitucion() {
        return institucion;
    }

    public void setInstitucion(String institucion) {
        this.institucion = institucion;
    }

    public String getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(String fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public String getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(String fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public Boolean getActualmente() {
        return actualmente;
    }

    public void setActualmente(Boolean actualmente) {
        this.actualmente = actualmente;
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
