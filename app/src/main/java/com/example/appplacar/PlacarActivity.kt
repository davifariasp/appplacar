package com.example.appplacar

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import data.Placar
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream
import java.nio.charset.StandardCharsets
import java.util.Locale


class PlacarActivity: AppCompatActivity() {
    lateinit var bd:ArrayList<Placar>
    lateinit var placar:Placar
    lateinit var tvResultadoJogo: TextView

    var resultadoAnterior = ""
    var game = 0

    //Cronometro
    val START_TIME_IN_MILLIS: Long = 601000

    var mTextViewCountDown: TextView? = null

    var mCountDownTimer: CountDownTimer? = null
    var mTimerRunning = false
    var mTimeLeftInMillis = START_TIME_IN_MILLIS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_placar)


        placar= getIntent().getExtras()?.getSerializable("placar") as Placar
        bd = getIntent().getExtras()?.getSerializable("bd") as ArrayList<Placar>

        mTextViewCountDown = findViewById(R.id.tvTimer);

        if(placar.has_timer){
            startTimer()
            updateCountDownText();
        } else {
            mTextViewCountDown!!.text = ""
        }

        tvResultadoJogo= findViewById(R.id.tvPlacar)
        //Mudar o nome da partida
        val tvNomePartida=findViewById(R.id.tvNomePartida2) as TextView
        tvNomePartida.text=placar.nome_partida
    }


    fun changeStateTimer(v: View){
        Log.v("timer", "foi clicado ${mTimerRunning}")
        if (mTimerRunning) {
            pauseTimer();
        } else {
            startTimer();
        }
    }

    fun startTimer(){
        mCountDownTimer = object : CountDownTimer(mTimeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                mTimeLeftInMillis = millisUntilFinished
                updateCountDownText()
            }

            override fun onFinish() {
                mTimerRunning = false
            }
        }.start()

        mTimerRunning = true
    }

    fun pauseTimer(){
        mCountDownTimer?.cancel()
        mTimerRunning = false
    }

    fun updateCountDownText() {
        val minutes = (mTimeLeftInMillis / 1000).toInt() / 60
        val seconds = (mTimeLeftInMillis / 1000).toInt() % 60
        val timeLeftFormatted: String =
            java.lang.String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        mTextViewCountDown!!.text = timeLeftFormatted
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
        edShared.putString("resultadoLongo", "O")
        edShared.putBoolean("has_timer", placar.has_timer)

        //Escrita em Bytes de Um objeto Serializável
        var dt= ByteArrayOutputStream()
        var oos = ObjectOutputStream(dt);
        oos.writeObject(placar);

        //Salvar como "match" + numero de partidas ja salvas
        edShared.putString("match${cont}", dt.toString(StandardCharsets.ISO_8859_1.name()))
        Log.v("PDM", "Partida ${cont} ${placar.nome_partida} ${placar.resultado} ${placar.resultadoLongo} ${placar.has_timer} foi salva!")
        edShared.commit()

        //Finalizando
        val intent = Intent(this, MainActivity::class.java).apply{
                putExtra( "finalizado", true)
        }

        startActivity(intent)
    }

    //ctrl+z do resultado
    fun reverter(v:View){
        var placarAtual = placar.resultado
        placar.resultado = resultadoAnterior
        resultadoAnterior = placarAtual

        tvResultadoJogo.text=placar.resultado
    }

}