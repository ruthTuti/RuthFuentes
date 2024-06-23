package RecyclerViewHelpers



import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ruth.fuentes.crud.R

class ViewHolder (view: View): RecyclerView.ViewHolder(view){
    // en el viewHolder mando a llamar los elementos de la card
    val txtNombreCard= view.findViewById<TextView>(R.id.txtNombreCard)
    val imgEditar: ImageView = view.findViewById(R.id.imgEditar)
    val imgBorrar: ImageView = view.findViewById(R.id.imgborrar)

}



