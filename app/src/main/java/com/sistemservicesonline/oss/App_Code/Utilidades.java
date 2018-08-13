package com.sistemservicesonline.oss.App_Code;

import android.app.NotificationManager;

import com.sistemservicesonline.oss.App_Code.GestionUsuarios.Usuario;

import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import android.app.NotificationManager;

public class Utilidades {

    Usuario ObjUsuario = new Usuario();
    Conexion ObjConexion = new Conexion();

    public Usuario ConsultarUsuario (String sToken) {
        String sQuery = "SELECT TOP 1 U.IdUsuario AS IdUsuario, UA.CodUserAplication AS CodUserAplication, TI.Nombre AS TipoIdentificacion, U.Identificacion AS Identificacion, U.PrimerNombre AS PrimerNombre, U.SegundoNombre AS SegundoNombre, U.PrimerApellido AS PrimerApellido, U.SegundoApellido AS SegundoApellido, CONVERT(VARCHAR(16),U.FechaNacimiento,120) AS FechaNacimiento, U.Edad AS Edad, G.Nombre AS Genero, EC.Nombre AS EstadoCivil, U.Celular As Celular, U.Telefono AS Telefono, U.Email AS Email, D.Nombre AS Departamento, C.Nombre AS Ciudad, U.Direccion AS Direccion, P.Nombre AS Profesion, U.Nombrecompleto AS NombreCompleto, U.Activo AS Activo FROM Usuarios.Usuario U WITH (NOLOCK) INNER JOIN Usuarios.UsuarioAplicacion UA WITH (NOLOCK) ON UA.CodUserAplication = U.CodUsuarioAplicacion LEFT JOIN Empresas.Empresa E WITH (NOLOCK) ON E.CodigoEmpresa = U.EmpresaAsociada INNER JOIN Maestros.TiposIdentificacion TI WITH (NOLOCK) ON TI.Codigo = U.TipoIdentificacion INNER JOIN Maestros.Generos G WITH (NOLOCK) ON G.Codigo = U.Genero LEFT JOIN Maestros.EstadosCiviles EC WITH (NOLOCK) ON EC.Codigo = U.EstadoCivil INNER JOIN Maestros.Departamentos D WITH (NOLOCK) ON D.Codigo = U.Departamento INNER JOIN Maestros.Ciudades C WITH (NOLOCK) ON C.Codigo = U.Ciudad INNER JOIN Maestros.Profesiones P WITH (NOLOCK) ON P.Codigo = U.Profesion WHERE UA.CodUserAplication = '" + sToken + "'";
        SimpleDateFormat ObjSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Statement ObjStatement = ObjConexion.ConexionDB().createStatement();
            ResultSet ObjResultSet = ObjStatement.executeQuery(sQuery);
            if (ObjResultSet != null){
                while (ObjResultSet.next()){
                    ObjUsuario.sTipoIdentificacion = ObjResultSet.getString("TipoIdentificacion") != null ? ObjResultSet.getString("TipoIdentificacion").toString() : "";
                    ObjUsuario.sIdentificacion = ObjResultSet.getString("Identificacion") != null ? ObjResultSet.getString("Identificacion").toString() : "";
                    ObjUsuario.sPrimerNombre = ObjResultSet.getString("PrimerNombre") != null ? ObjResultSet.getString("PrimerNombre").toString() : "";
                    ObjUsuario.sSegundoNombre = ObjResultSet.getString("SegundoNombre") != null ? ObjResultSet.getString("SegundoNombre").toString() : "";
                    ObjUsuario.sPrimerApellido = ObjResultSet.getString("PrimerApellido") != null ? ObjResultSet.getString("PrimerApellido").toString() : "";
                    ObjUsuario.sSegundoApellido = ObjResultSet.getString("SegundoApellido") != null ? ObjResultSet.getString("SegundoApellido").toString() : "";
                    ObjUsuario.dFechaNacimiento = ObjResultSet.getString("FechaNacimiento") != null ? ObjSimpleDateFormat.parse(ObjResultSet.getString("FechaNacimiento").toString()) : ObjSimpleDateFormat.parse("");
                    ObjUsuario.iEdad = ObjResultSet.getString("Edad") != null ? Integer.parseInt(ObjResultSet.getString("Edad").toString()) : 0;
                    ObjUsuario.sDepartamento = ObjResultSet.getString("Departamento") != null ? ObjResultSet.getString("Departamento").toString() : "";
                    ObjUsuario.sCiudad = ObjResultSet.getString("Ciudad") != null ? ObjResultSet.getString("Ciudad").toString() : "";
                    ObjUsuario.sDireccion = ObjResultSet.getString("Direccion") != null ? ObjResultSet.getString("Direccion").toString() : "";
                    ObjUsuario.sCelular = ObjResultSet.getString("Celular") != null ? ObjResultSet.getString("Celular").toString() : "";
                    ObjUsuario.sTelefono = ObjResultSet.getString("Telefono") != null ? ObjResultSet.getString("Telefono").toString() : "";
                    ObjUsuario.sGenero = ObjResultSet.getString("Genero") != null ? ObjResultSet.getString("Genero").toString() : "";
                    ObjUsuario.sEstadoCivil = ObjResultSet.getString("EstadoCivil") != null ? ObjResultSet.getString("EstadoCivil").toString() : "";
                    ObjUsuario.sCodUsuarioAplicacion = ObjResultSet.getString("CodUserAplication") != null ? ObjResultSet.getString("CodUserAplication").toString() : "";
                    ObjUsuario.sEmail = ObjResultSet.getString("Email") != null ? ObjResultSet.getString("Email").toString() : "";
                    ObjUsuario.sProfesion = ObjResultSet.getString("Profesion") != null ? ObjResultSet.getString("Profesion").toString() : "";
                    ObjUsuario.sNombreCompleto = ObjResultSet.getString("NombreCompleto") != null ? ObjResultSet.getString("NombreCompleto").toString() : "";
                    ObjUsuario.bActivo = ObjResultSet.getString("Activo") != null ? Boolean.parseBoolean(ObjResultSet.getString("Activo").toString()) : false;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return ObjUsuario;
    }

    public ArrayList ConsultarUsuarios () {
        String sQuery = "SELECT * FROM Usuarios.Usuario";
        ArrayList<String> lstDatos = new ArrayList<>();

        try {
            Statement ObjStatement = ObjConexion.ConexionDB().createStatement();
            ResultSet ObjResultSet = ObjStatement.executeQuery(sQuery);
            if (ObjResultSet != null){
                while (ObjResultSet.next()){
                    lstDatos.add(ObjResultSet.getString("NombreCompleto") != null ? ObjResultSet.getString("NombreCompleto").toString() : "");
                    lstDatos.add(ObjResultSet.getString("Profesion") != null ? ObjResultSet.getString("Profesion").toString() : "");
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return lstDatos;
    }
}
