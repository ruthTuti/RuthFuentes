package ruth.fuentes.crud

import RecyclerViewHelpers.Adaptador
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import modelo.tbTicket
import java.util.UUID

class tickets : AppCompatActivity() {





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_tickets)


        val txttuti1 = findViewById<EditText>(R.id.txttuti1)
        val txttuti2 = findViewById<EditText>(R.id.txttuti2)
        val txttuti3 = findViewById<EditText>(R.id.txttuti3)
        val txttuti4 = findViewById<EditText>(R.id.txttuti4)
        val txttuti5 = findViewById<EditText>(R.id.txttuti5)
        val txttuti6 = findViewById<EditText>(R.id.txttuti6)
        val txttuti7 = findViewById<EditText>(R.id.txttuti7)
        val txttuti8 = findViewById<EditText>(R.id.txttuti8)
        val rcvtike = findViewById<RecyclerView>(R.id.rcvtike)

        rcvtike.layoutManager= LinearLayoutManager(this)


        fun tbTicket(
            uuid_ticket: String?,
            numero: Int,
            titulo: String?,
            descripcion: String?,
            fecha: String,
            estado: String?,
            fecha_fin: String?,
         ): tbTicket {


            fun artiket(): List<tbTicket> {

                // creo un objeto de la clase conexion
                val objConexion = ClaseConexion().cadenaConexion()

                // 2- creo un statement

                val statement = objConexion?.createStatement()
                val resultSet = statement?.executeQuery("SELECT * FROM tbTicket")!!

                val ListaTicket = mutableListOf<tbTicket>()
                while (resultSet.next()) {
                    val uuid_Ticket = resultSet.getString("uuid_Ticket")
                    val numero = resultSet.getInt("numero")
                    val titulo = resultSet.getString("titulo")
                    val descripcion = resultSet.getString("descripcion ")
                    val fecha = resultSet.getInt("fecha")
                    val estado = resultSet.getString("estado")
                    val fecha_fin = resultSet.getString("fecha_fin")

                    val todojunto = tbTicket(
                        uuid_Ticket, numero, titulo, descripcion,
                        fecha.toString(), estado, fecha_fin
                    )

                    ListaTicket.add((todojunto))


                }
                return ListaTicket
            }

            // Asignar el adaptador al recyclerview

            CoroutineScope(Dispatchers.IO).launch {

                val TicketDB = artiket()
                withContext(Dispatchers.Main) {
                    val adapter = Adaptador(TicketDB)
                    rcvtike.adapter = adapter

                }


            }

            val btngenerar = findViewById<Button>(R.id.btngenerar)
            btngenerar.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    val objConexion = ClaseConexion().cadenaConexion()

                    try {
                        // validacion de los campos vacios

                        if (txttuti1.text.isEmpty() || txttuti2.text.isEmpty() || txttuti3.text.isEmpty() ||
                            txttuti4.text.isEmpty() || txttuti5.text.isEmpty() || txttuti6.text.isEmpty() ||
                            txttuti7.text.isEmpty() || txttuti8.text.isEmpty()
                        ) {

                            withContext(Dispatchers.Main) {

                                Toast.makeText(
                                    this@tickets,
                                    "Complete los campos ",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                            return@launch
                        }

                        val addTicket =
                            objConexion?.prepareStatement("insert into Ticket (uuid_ticket,numero,titulo,descripcion,autor,email,fecha,estado,fechafin)values (?,?,?,?,?,?,?,?)")!!

                        addTicket.setString(1, UUID.randomUUID().toString())
                        addTicket.setInt(2, txttuti1.text.toString().toInt())
                        addTicket.setString(3, txttuti2.text.toString())
                        addTicket.setString(4, txttuti3.text.toString())
                        addTicket.setString(5, txttuti4.text.toString())
                        addTicket.setString(6, txttuti5.text.toString())
                        addTicket.setString(7, txttuti7.text.toString())
                        addTicket.setString(8, txttuti7.text.toString())
                        addTicket.setString(9, txttuti8.text.toString())

                        addTicket.executeUpdate()

                        // aca se renueva la lista

                        val nuevasMascotas = artiket()
                        withContext(Dispatchers.Main) {
                            (rcvtike.adapter as? Adaptador)?.actualizarLista(nuevasMascotas)
                        }

                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@tickets, "Ticket creado", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@tickets, "Error: ${e.message}", Toast.LENGTH_LONG)
                                .show()
                            println("Error: ${e.message}")
                            e.printStackTrace()

                        }


                    }
                }
            }


        }
    }
}
