import com.parana.dobleyfalta.retrofit.models.jornadas.JornadaModel

data class PartidoModel(
    val idPartido: Int,
    val fecha: String?,
    val puntosLocal: Int?,
    val puntosVisitante: Int?,
    val idEquipoLocal: Int?,
    val idEquipoVisitante: Int?,
    val jornada: JornadaModel?, // 💡 Ahora es un objeto embebido
    val estadoPartido: String,
    val liga: String = "LIGA REGIONAL DE BÁSQUET",
    val cuartosLocal: List<Int>? = null,
    val cuartosVisitante: List<Int>? = null
)