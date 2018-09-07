package com.sistemservicesonline.oss.appcode;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExperienciaLaboral {

    @SerializedName("Codigo")
    @Expose
    private String codigo;
    @SerializedName("Empresa")
    @Expose
    private String empresa;
    @SerializedName("Departamento")
    @Expose
    private String departamento;
    @SerializedName("Ciudad")
    @Expose
    private String ciudad;
    @SerializedName("Cargo")
    @Expose
    private String cargo;
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

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
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