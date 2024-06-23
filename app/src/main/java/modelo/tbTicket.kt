package modelo

data class tbTicket(

    val uuid_ticket : String,

    val numero : Int,
    var titulo :String,
    val descripcion : String,
    val fecha  : String,
    val estado : String,
    val fecha_fin : String,
    val email : String,
    val autor : String ,


    )
