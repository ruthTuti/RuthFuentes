package modelo

import java.sql.Connection
import java.sql.DriverManager

class ClaseConexion {


    fun cadenaConexion():Connection? {
        try {
            val url = "jdbc:oracle:thin:@192.168.3.8:1521:xe"
            val usuario = "SYSTEM"
            val contrasena = "Ruth"

            val connection = DriverManager.getConnection(url, usuario, contrasena)
            return connection






        }catch (e:Exception){
            println("error:$e")
            return null


        }


    }
}