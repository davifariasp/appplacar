package data

import java.io.Serializable

data class Placar(
    var equipe_a:String,
    var equipe_b:String,
    var resultado:String,
    var id_partida:String,
    var resultadoLongo:String,
    var has_timer:Boolean):Serializable