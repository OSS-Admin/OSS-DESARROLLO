package com.sistemservicesonline.oss.appcode;

import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Utilidades {

    Conexion ObjConexion = new Conexion();

    public ArrayList ConsultarMaestro (String sConsulta) {
        String sQuery = "";
        ArrayList<String> lstDatos = new ArrayList<>();

        try {

            if (sConsulta.equals("TiposIdentificacion")) {
                sQuery = "SELECT TI.Codigo AS Codigo, TI.Nombre AS Nombre FROM Maestros.TiposIdentificacion TI WITH (NOLOCK) WHERE TI.Activo = 1";
            } else if (sConsulta.equals("Generos")) {
                sQuery = "SELECT G.Codigo AS Codigo, G.Nombre AS Nombre FROM Maestros.Generos G WITH (NOLOCK) WHERE G.Activo = 1";
            } else if (sConsulta.equals("Profesiones")) {
                sQuery = "SELECT P.Codigo AS Codigo, P.Nombre AS Nombre FROM Maestros.Profesiones P WITH (NOLOCK) WHERE P.Activo = 1";
            } else if (sConsulta.equals("EstadosCiviles")) {
                sQuery = "SELECT EC.Codigo AS Codigo, EC.Nombre AS Nombre FROM Maestros.EstadosCiviles EC WITH (NOLOCK) WHERE EC.Activo = 1";
            } else if (sConsulta.equals("Departamentos")) {
                sQuery = "SELECT D.Codigo AS Codigo, D.Nombre AS Nombre FROM Maestros.Departamentos D WITH (NOLOCK) WHERE D.Activo = 1";
            } else if (sConsulta.equals("Ciudades")) {
                sQuery = "SELECT C.Codigo AS Codigo, C.Nombre AS Nombre FROM Maestros.Ciudades C WITH (NOLOCK) WHERE C.Activo = 1";
            }

            Statement ObjStatement = ObjConexion.ConexionDB().createStatement();
            ResultSet ObjResultSet = ObjStatement.executeQuery(sQuery);
            if (ObjResultSet != null){
                while (ObjResultSet.next()){
                    lstDatos.add(ObjResultSet.getString("Nombre") != null ? ObjResultSet.getString("Nombre").toString() : "");
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return lstDatos;
    }

    public static String ObtenerEdad(Date fechaNacimiento) {
        if (fechaNacimiento != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            StringBuilder result = new StringBuilder();
            if (fechaNacimiento != null) {
                result.append(sdf.format(fechaNacimiento));
                result.append(" (");
                Calendar c = new GregorianCalendar();
                c.setTime(fechaNacimiento);
                result.append(calcularEdad(c));
                result.append(" años)");
            }
            return result.toString();
        }
        return "";
    }

    private static int calcularEdad(Calendar fechaNac) {
        Calendar today = Calendar.getInstance();
        int diffYear = today.get(Calendar.YEAR) - fechaNac.get(Calendar.YEAR);
        int diffMonth = today.get(Calendar.MONTH) - fechaNac.get(Calendar.MONTH);
        int diffDay = today.get(Calendar.DAY_OF_MONTH) - fechaNac.get(Calendar.DAY_OF_MONTH);
        // Si está en ese año pero todavía no los ha cumplido
        if (diffMonth < 0 || (diffMonth == 0 && diffDay < 0)) {
            diffYear = diffYear - 1;
        }
        return diffYear;
    }
}
