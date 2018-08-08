package com.sistemservicesonline.oss.App_Code;

import android.os.StrictMode;
import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {

    public Connection ConexionDB (){
        Connection ObjConnection = null;

        try{

            StrictMode.ThreadPolicy ObjPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(ObjPolicy);

            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            ObjConnection = DriverManager.getConnection("jdbc:jtds:sqlServer://BuscaEmpleo.mssql.somee.com/BuscaEmpleo;databaseName=BuscaEmpleo;user=Mosorio_SQLLogin_1;password=glzyhtkie7;");
        } catch (Exception e){

        }

        return ObjConnection;
    }

}
