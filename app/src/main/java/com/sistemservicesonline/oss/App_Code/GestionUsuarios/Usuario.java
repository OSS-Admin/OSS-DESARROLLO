package com.sistemservicesonline.oss.App_Code.GestionUsuarios;

import java.util.Date;

public class Usuario {
    public String sTipoIdentificacion;
    public String sIdentificacion;
    public String sPrimerNombre;
    public String sSegundoNombre;
    public String sPrimerApellido;
    public String sSegundoApellido;
    public Date dFechaNacimiento;
    public int iEdad;
    public String sDepartamento;
    public String sCiudad;
    public String sDireccion;
    public String sCelular;
    public String sTelefono;
    public String sGenero;
    public String sEstadoCivil;
    public String sCodUsuarioAplicacion;
    public String sEmail;
    public String sProfesion;
    public String sNombreCompleto;
    public boolean bActivo;
    public String sContrasena;

    public Usuario() {
    }

    public Usuario(String sTipoIdentificacion, String sIdentificacion, String sPrimerNombre, String sSegundoNombre, String sPrimerApellido, String sSegundoApellido, Date dFechaNacimiento, int iEdad, String sDepartamento, String sCiudad, String sDireccion, String sCelular, String sTelefono, String sGenero, String sEstadoCivil, String sCodUsuarioAplicacion, String sEmail, String sProfesion, String sNombreCompleto, boolean bActivo, String sContrasena) {
        this.sTipoIdentificacion = sTipoIdentificacion;
        this.sIdentificacion = sIdentificacion;
        this.sPrimerNombre = sPrimerNombre;
        this.sSegundoNombre = sSegundoNombre;
        this.sPrimerApellido = sPrimerApellido;
        this.sSegundoApellido = sSegundoApellido;
        this.dFechaNacimiento = dFechaNacimiento;
        this.iEdad = iEdad;
        this.sDepartamento = sDepartamento;
        this.sCiudad = sCiudad;
        this.sDireccion = sDireccion;
        this.sCelular = sCelular;
        this.sTelefono = sTelefono;
        this.sGenero = sGenero;
        this.sEstadoCivil = sEstadoCivil;
        this.sCodUsuarioAplicacion = sCodUsuarioAplicacion;
        this.sEmail = sEmail;
        this.sProfesion = sProfesion;
        this.sNombreCompleto = sNombreCompleto;
        this.bActivo = bActivo;
        this.sContrasena = sContrasena;
    }

    public String getsTipoIdentificacion() {
        return sTipoIdentificacion;
    }

    public void setsTipoIdentificacion(String sTipoIdentificacion) {
        this.sTipoIdentificacion = sTipoIdentificacion;
    }

    public String getsIdentificacion() {
        return sIdentificacion;
    }

    public void setsIdentificacion(String sIdentificacion) {
        this.sIdentificacion = sIdentificacion;
    }

    public String getsPrimerNombre() {
        return sPrimerNombre;
    }

    public void setsPrimerNombre(String sPrimerNombre) {
        this.sPrimerNombre = sPrimerNombre;
    }

    public String getsSegundoNombre() {
        return sSegundoNombre;
    }

    public void setsSegundoNombre(String sSegundoNombre) {
        this.sSegundoNombre = sSegundoNombre;
    }

    public String getsPrimerApellido() {
        return sPrimerApellido;
    }

    public void setsPrimerApellido(String sPrimerApellido) {
        this.sPrimerApellido = sPrimerApellido;
    }

    public String getsSegundoApellido() {
        return sSegundoApellido;
    }

    public void setsSegundoApellido(String sSegundoApellido) {
        this.sSegundoApellido = sSegundoApellido;
    }

    public Date getdFechaNacimiento() {
        return dFechaNacimiento;
    }

    public void setdFechaNacimiento(Date dFechaNacimiento) {
        this.dFechaNacimiento = dFechaNacimiento;
    }

    public int getiEdad() {
        return iEdad;
    }

    public void setiEdad(int iEdad) {
        this.iEdad = iEdad;
    }

    public String getsDepartamento() {
        return sDepartamento;
    }

    public void setsDepartamento(String sDepartamento) {
        this.sDepartamento = sDepartamento;
    }

    public String getsCiudad() {
        return sCiudad;
    }

    public void setsCiudad(String sCiudad) {
        this.sCiudad = sCiudad;
    }

    public String getsDireccion() {
        return sDireccion;
    }

    public void setsDireccion(String sDireccion) {
        this.sDireccion = sDireccion;
    }

    public String getsCelular() {
        return sCelular;
    }

    public void setsCelular(String sCelular) {
        this.sCelular = sCelular;
    }

    public String getsTelefono() {
        return sTelefono;
    }

    public void setsTelefono(String sTelefono) {
        this.sTelefono = sTelefono;
    }

    public String getsGenero() {
        return sGenero;
    }

    public void setsGenero(String sGenero) {
        this.sGenero = sGenero;
    }

    public String getsEstadoCivil() {
        return sEstadoCivil;
    }

    public void setsEstadoCivil(String sEstadoCivil) {
        this.sEstadoCivil = sEstadoCivil;
    }

    public String getsCodUsuarioAplicacion() {
        return sCodUsuarioAplicacion;
    }

    public void setsCodUsuarioAplicacion(String sCodUsuarioAplicacion) {
        this.sCodUsuarioAplicacion = sCodUsuarioAplicacion;
    }

    public String getsEmail() {
        return sEmail;
    }

    public void setsEmail(String sEmail) {
        this.sEmail = sEmail;
    }

    public String getsProfesion() {
        return sProfesion;
    }

    public void setsProfesion(String sProfesion) {
        this.sProfesion = sProfesion;
    }

    public String getsNombreCompleto() {
        return sNombreCompleto;
    }

    public void setsNombreCompleto(String sNombreCompleto) {
        this.sNombreCompleto = sNombreCompleto;
    }

    public boolean isbActivo() {
        return bActivo;
    }

    public void setbActivo(boolean bActivo) {
        this.bActivo = bActivo;
    }

    public String getsContrasena() {
        return sContrasena;
    }

    public void setsContrasena(String sContrasena) {
        this.sContrasena = sContrasena;
    }
}
