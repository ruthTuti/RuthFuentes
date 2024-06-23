package ruth.fuentes.crud

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import java.util.UUID

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rcvtikets)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Mandar a llamar a todos los elementos que tengo en la vista
        val txtCorreo = findViewById<EditText>(R.id.txtCorreo)
        val txtContrasena = findViewById<EditText>(R.id.txtContrasena)
        val btnIniciar = findViewById<Button>(R.id.btnIniciar)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)



        // prograrmar los botones


        btnIniciar.setOnClickListener {
            val pantallaLogin = Intent(this, activityLogin::class.java)
            startActivity(pantallaLogin)

        btnRegistrar.setOnClickListener {


            if (txtCorreo.text.isEmpty() || txtContrasena.text.isEmpty()) {
                Toast.makeText(this, "Debe completar todos los campos ", Toast.LENGTH_SHORT).show()
                txtCorreo.setText("")
                txtContrasena.setText("")


            } else {
                CoroutineScope(Dispatchers.IO).launch {

                    //esto es para evitar que la aplicacion se crashee al no ingresar uno o todos los datos o algun que otro posible error

                    try {

                        val objConexion = ClaseConexion().cadenaConexion()

                        if (objConexion != null) {
                            val addUsuario =
                                objConexion.prepareStatement("insert into tbUsuario (UUID,usuario, contrasena) VALUES(?, ?, ?)")
                            addUsuario.setString(1, UUID.randomUUID().toString())
                            addUsuario.setString(2, txtCorreo.text.toString())
                            addUsuario.setString(3, txtContrasena.text.toString())
                            addUsuario.executeUpdate()

                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Ya tienes tu usuario creado :D",
                                    Toast.LENGTH_SHORT
                                ).show()
                                txtCorreo.setText("")
                                txtContrasena.setText("")

                                val pantallaLogin =
                                    Intent(this@MainActivity, activityLogin::class.java)
                                startActivity(pantallaLogin)

                            }
                        }else{
                            withContext(Dispatchers.Main){
                                Toast.makeText(this@MainActivity, "Tienes un error de conexion", Toast.LENGTH_SHORT).show()

                            }
                        }

                        }catch (e:Exception){
                            withContext(Dispatchers.Main){
                                Toast.makeText(
                                    this@MainActivity,
                                    "error:${e.message}",
                                    Toast.LENGTH_LONG
                                ).show()




                            }

                        }
                    }
                }


            }
        }
    }
}