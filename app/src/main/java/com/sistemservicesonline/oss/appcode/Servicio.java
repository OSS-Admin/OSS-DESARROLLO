package com.sistemservicesonline.oss.appcode;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.sourceforge.jtds.jdbc.DateTime;

public class Servicio {
    @SerializedName("Codigo")
    @Expose
    private String codigo;
    @SerializedName("CodigoUsuario")
    @Expose
    private String codigoUsuario;
    @SerializedName("CodigoUsuarioServicio")
    @Expose
    private String codigoUsuarioServicio;
    @SerializedName("FechaServicio")
    @Expose
    private String fechaServicio;
    @SerializedName("FechaInicio")
    @Expose
    private String fechaInicio;
    @SerializedName("FechaFinal")
    @Expose
    private String fechaFinal;
    @SerializedName("Descripcion")
    @Expose
    private String descripcion;
    @SerializedName("Estado")
    @Expose
    private String estado;

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

    public String getCodigoUsuarioServicio() {
        return codigoUsuarioServicio;
    }

    public void setCodigoUsuarioServicio(String codigoUsuarioServicio) {
        this.codigoUsuarioServicio = codigoUsuarioServicio;
    }

    public String getFechaServicio() {
        return fechaServicio;
    }

    public void setFechaServicio(String fechaServicio) {
        this.fechaServicio = fechaServicio;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(String fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
