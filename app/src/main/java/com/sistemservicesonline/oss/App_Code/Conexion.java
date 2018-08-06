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
            ObjConnection = DriverManager.getConnection("jdbc:jtds:sqlServer://192.52.242.142/PRODUCCION;databaseName=OSS;user=sa;password=sa1_xxxx;");
        } catch (Exception e){

        }

        return ObjConnection;
    }

}
