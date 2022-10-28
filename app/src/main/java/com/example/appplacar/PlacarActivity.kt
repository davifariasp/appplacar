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
    lateinit var tvPlacarA: TextView
    lateinit var tvPlacarB: TextView

    var pa = 0 //pontos a
    var pa_anterior = 0
    var alta = false

    var pb = 0 //potos b
    var pb_anterior = 0
    var altb = false

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

        tvPlacarA= findViewById(R.id.tvPlacarA)
        tvPlacarB= findViewById(R.id.tvPlacarB)

        //Mudar o nome da partida
        val tvNomePartida=findViewById(R.id.idNomePartida) as TextView
        tvNomePartida.text=placar.id_partida
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

    fun setPointA(v: View){
        pa_anterior = pa

        pa++
        tvPlacarA.text = "${pa}"
        placar.resultado = "${pa} x ${pb}"

        alta = true
        altb = false

    }

    fun setPointB(v: View){
        pb_anterior = pb

        pb++
        tvPlacarB.text = "${pb}"
        placar.resultado = "${pa} x ${pb}"

        altb = true
        alta = false
    }

    //ctrl+z do resultado
    fun reverter(v:View){
        var aux = 0

        if(alta){
            aux = pa
            pa = pa_anterior
            pa_anterior = aux
        } else {
            aux = pb
            pb = pb_anterior
            pb_anterior = aux
        }

        tvPlacarA.text = "${pa}"
        tvPlacarB.text = "${pb}"

        placar.resultado = "${pa} x ${pb}"
    }


    fun saveGame(v: View) {
        val sharedFilename = "PreviousGames"
        val sp: SharedPreferences = getSharedPreferences(sharedFilename, Context.MODE_PRIVATE)
        placar.resultadoLongo = "O jogo ${placar.id_partida} foi de ${placar.resultado}"

        var edShared = sp.edit()

        //número de jogos armazenados
        var cont = bd.size

        //Salvar o número de jogos já armazenados
        edShared.putInt("numberMatch", cont)
        edShared.putString("nomePartida", placar.id_partida)
        edShared.putString("resultadoLongo", placar.resultadoLongo)
        edShared.putBoolean("has_timer", placar.has_timer)

        //Escrita em Bytes de Um objeto Serializável
        var dt= ByteArrayOutputStream()
        var oos = ObjectOutputStream(dt);
        oos.writeObject(placar);

        //Salvar como "match" + numero de partidas ja salvas
        edShared.putString("match${cont}", dt.toString(StandardCharsets.ISO_8859_1.name()))
        Log.v("PDM", "Partida ${cont} ${placar.resultadoLongo} foi salva!")
        edShared.commit()

        //Finalizando
        val intent = Intent(this, MainActivity::class.java).apply{
                putExtra( "finalizado", true)
        }

        startActivity(intent)
    }


    fun openPlacarH(v: View){
        val intent = Intent(this, PlacarActivity::class.java).apply{
            putExtra( "finalizado", true)
        }
        startActivity(intent)
    }

}