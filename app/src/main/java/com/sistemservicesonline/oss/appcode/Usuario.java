package com.sistemservicesonline.oss.appcode;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Usuario {

    @SerializedName("CodigoUsuario")
    @Expose
    private String codigoUsuario;
    @SerializedName("TipoIdentificacion")
    @Expose
    private String tipoIdentificacion;
    @SerializedName("Identificacion")
    @Expose
    private String identificacion;
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
    @SerializedName("FechaNacimiento")
    @Expose
    private String fechaNacimiento;
    @SerializedName("Edad")
    @Expose
    private Integer edad;
    @SerializedName("CodigoCategoria")
    @Expose
    private String codigoCategoria;
    @SerializedName("Categoria")
    @Expose
    private String categoria;
    @SerializedName("Genero")
    @Expose
    private String genero;
    @SerializedName("Departamento")
    @Expose
    private String departamento;
    @SerializedName("Ciudad")
    @Expose
    private String ciudad;
    @SerializedName("Direccion")
    @Expose
    private String direccion;
    @SerializedName("Celular")
    @Expose
    private String celular;
    @SerializedName("Telefono")
    @Expose
    private String telefono;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("Estado")
    @Expose
    private String estado;
    @SerializedName("Usuario")
    @Expose
    private String usuario;
    @SerializedName("Activo")
    @Expose
    private Boolean activo;
    @SerializedName("PerfilProfesional")
    @Expose
    private String perfilProfesional;
    @SerializedName("Contrasena")
    @Expose
    private String contrasena;
    @SerializedName("Calificacion")
    @Expose
    private Float calificacion;

    public String getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(String codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) { this.tipoIdentificacion = tipoIdentificacion; }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
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

    public void setSegundoApellido(String segundoApellido) { this.segundoApellido = segundoApellido; }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getCodigoCategoria() { return codigoCategoria; }

    public void setCodigoCategoria(String codigoCategoria) { this.codigoCategoria = codigoCategoria; }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getPerfilProfesional() {
        return perfilProfesional;
    }

    public void setPerfilProfesional(String perfilProfesional) { this.perfilProfesional = perfilProfesional; }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Float getCalificacion() { return calificacion; }

    public void setCalificacion(Float calificacion) { this.calificacion = calificacion; }
}