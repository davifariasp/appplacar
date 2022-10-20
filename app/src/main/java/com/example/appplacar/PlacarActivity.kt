package com.example.appplacar

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import data.Placar
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.nio.charset.StandardCharsets

class PlacarActivity: AppCompatActivity() {
    lateinit var bd:ArrayList<Placar>
    lateinit var placar:Placar
    lateinit var tvResultadoJogo: TextView

    var resultadoAnterior = ""
    var game = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_placar)

        placar= getIntent().getExtras()?.getSerializable("placar") as Placar
        bd = getIntent().getExtras()?.getSerializable("bd") as ArrayList<Placar>

        tvResultadoJogo= findViewById(R.id.tvPlacar)
        //Mudar o nome da partida
        val tvNomePartida=findViewById(R.id.tvNomePartida2) as TextView
        tvNomePartida.text=placar.nome_partida
    }

    //ao clicar no texto do placar ele é alterado
    fun alteraPlacar (v: View){
        game++

        //salvando resultado anterior
        resultadoAnterior = placar.resultado

        if ((game % 2) != 0) {
            placar.resultado = ""+game+" vs "+ (game-1)
        }else{
            placar.resultado = ""+(game-1)+" vs "+ (game-1)
        }

        tvResultadoJogo.text=placar.resultado
    }

    fun saveGame(v: View) {
        val sharedFilename = "PreviousGames"
        val sp: SharedPreferences = getSharedPreferences(sharedFilename, Context.MODE_PRIVATE)
        placar.resultadoLongo = "O jogo ${placar.nome_partida} foi de ${placar.resultado}"

        var edShared = sp.edit()

        //número de jogos armazenados
        var cont = bd.size

        //Salvar o número de jogos já armazenados
        edShared.putInt("numberMatch", cont)
        edShared.putString("nomePartida", placar.nome_partida)
        edShared.putString("resultado", placar.resultado)
        edShared.putString("resultadoLongo", placar.resultadoLongo)
        edShared.putBoolean("has_timer", placar.has_timer)

        //Escrita em Bytes de Um objeto Serializável
        var dt= ByteArrayOutputStream()
        var oos = ObjectOutputStream(dt);
        oos.writeObject(placar);

        //Salvar como "match" + numero de partidas ja salvas
        edShared.putString("match${cont}", dt.toString(StandardCharsets.ISO_8859_1.name()))
        Log.v("PDM", "Partida ${cont} ${placar.nome_partida} ${placar.resultado} ${placar.resultadoLongo} ${placar.has_timer} foi salva!")
        edShared.commit()
    }

    //ctrl+z do resultado
    fun reverter(v:View){
        var placarAtual = placar.resultado
        placar.resultado = resultadoAnterior
        resultadoAnterior = placarAtual

        tvResultadoJogo.text=placar.resultado
    }
}