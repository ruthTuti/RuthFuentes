package RecyclerViewHelpers

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import modelo.tbTicket
import ruth.fuentes.crud.R
import ruth.fuentes.crud.detalle_ticket

class Adaptador(var Datos : List<tbTicket>): RecyclerView.Adapter<ViewHolder>() {
    fun actualizarLista(nuevaLista: List<tbTicket>) {
        Datos = nuevaLista
        notifyDataSetChanged() // Notificar al adaptador sobre los cambios
    }

    fun actualicePantalla(uuid: String, titulo: String) {
        val index = Datos.indexOfFirst { it.uuid_ticket == uuid }
        Datos[index].titulo = titulo
        notifyDataSetChanged()
    }

    /////////////////// TODO: Eliminar datos
    fun eliminarDatos(titulo: String, posicion: Int) {
        // se actualiza la lista de datos y esto notifica al adaptador
        val listaDatos = Datos.toMutableList()
        listaDatos.removeAt(posicion)

        GlobalScope.launch(Dispatchers.IO) {
            // aca cree un objeto de la clase conexion
            val objConexion = ClaseConexion().cadenaConexion()

            // aca cree una varieble que contiene un preparestament
            val deleteTicket =
                objConexion?.prepareStatement("delete from Ticket where titulo = ?")!!
            deleteTicket.setString(1, titulo)
            deleteTicket.executeUpdate()

            val commit = objConexion.prepareStatement("commit")!!
            commit.executeUpdate()
        }
        Datos = listaDatos.toList()
        // aca es donde se notifica al adaptador de los cambios
        notifyItemRemoved(posicion)
        notifyDataSetChanged()
    }

    //////////////////////TODO: Editar datos
    fun actualizarlosDatos(nuevoTitulo: String, uuid_Ticket: String) {
        GlobalScope.launch(Dispatchers.IO) {

            //1- Creo un objeto de la clase de conexion
            val objConexion = ClaseConexion().cadenaConexion()

            //2- creo una variable que contenga un PrepareStatement
            val updateTicket =
                objConexion?.prepareStatement("update Ticket set titulo = ? where uuid_Ticket = ?")!!
            updateTicket.setString(1, nuevoTitulo)
            updateTicket.setString(2, uuid_Ticket)
            updateTicket.executeUpdate()

            withContext(Dispatchers.Main) {
                actualicePantalla(uuid_Ticket, nuevoTitulo)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        // Unir recyclerView con la card

        val vista =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_item_card, parent, false)
        return ViewHolder(vista)
        TODO("Not yet implemented")
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")

        // Controlar la card
        val item = Datos[position]
        holder.txtNombreCard.text = item.titulo


        // todo : clic al incono de eliminar
        holder.imgBorrar.setOnClickListener() {

            // Creamos el Alert dialog
            val context = holder.itemView.context

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Eliminar")
            builder.setMessage("Quieres eliminar tu ticket?")

            // aca pasamos a la parte de los botones

            builder.setPositiveButton("si") { dialog, wich ->
                eliminarDatos(item.titulo, position)
            }

            builder.setNegativeButton("no") { dialog, which ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }

        // todo: icono editar

        holder.imgEditar.setOnClickListener {
            // creo el alert dialog
            val context = holder.itemView.context

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Actualizar ")
            builder.setMessage("Quieres actualizar tu ticket?")

            // agregamos un cuadro de texto para el nuevo nombre

            val cuadro = EditText(context)
            cuadro.setHint(item.titulo)
            builder.setView(cuadro)
            // boton

            builder.setPositiveButton("Acutalizar") { dialog, wich ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }

        // todo : clic a la card

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context

            // cambiar de pantalla
            val pantalladeDetalle = Intent(context, detalle_ticket::class.java)
            // eviar a otra pantalla estos valores
            pantalladeDetalle.putExtra("UUID_TICKET", item.uuid_ticket)
            pantalladeDetalle.putExtra("num_ticket", item.numero)
            pantalladeDetalle.putExtra("titulo", item.titulo)
            pantalladeDetalle.putExtra("descripcion", item.descripcion)
            pantalladeDetalle.putExtra("autor", item.autor)
            pantalladeDetalle.putExtra("email_autor", item.email)
            pantalladeDetalle.putExtra("fecha_ticket", item.fecha)
            pantalladeDetalle.putExtra("estado", item.estado)
            pantalladeDetalle.putExtra("fecha_fin_ticket", item.fecha_fin)
            context.startActivity(pantalladeDetalle)

        }


    }
}


