package com.example.appplacar

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import data.Placar
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    //abrindo "bd"
    fun openBd():ArrayList<Placar> {
        val sharedFilename = "PreviousGames"
        val sp: SharedPreferences = getSharedPreferences(sharedFilename, Context.MODE_PRIVATE)

        val data = ArrayList<Placar>()
        var cont = 0

        for (i in sp.all){
            var meuObjString:String= sp.getString("match${cont}","").toString()

            if (meuObjString.length >=1) {
                var dis = ByteArrayInputStream(meuObjString.toByteArray(Charsets.ISO_8859_1))
                var oos = ObjectInputStream(dis)

                var placar:Placar=oos.readObject() as Placar

                Log.v("Placar", placar.toString())
                //colocando dados
                data.add(placar)
            }

            cont++
        }
        return data
    }

    //configurar um novo jogo
    fun openConfig(v: View){
        var bd = openBd()

        val intent = Intent(this, ConfigActivity::class.java).apply{
            putExtra("bd", bd)
        }
        startActivity(intent)

    }

    //abrir histórico
    fun openPreviousGames(v: View) {
        var bd = openBd()

        val intent = Intent(this, PreviousGamesActivity::class.java).apply {
            putExtra("bd", bd)
        }

        startActivity(intent)
    }

}