package com.example.appplacar

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import data.Placar

class ConfigActivity : AppCompatActivity() {
    lateinit var bd:ArrayList<Placar>
    var placar: Placar = Placar("Jogo sem Config","0x0", "20/05/20 10h",false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        bd = getIntent().getExtras()?.getSerializable("bd") as ArrayList<Placar>

        initInterface()
        openConfig()

    }

    //configurando o placar
    fun saveConfig(){
        val sharedFilename = "configPlacar"
        val sp: SharedPreferences = getSharedPreferences(sharedFilename, Context.MODE_PRIVATE)
        var edShared = sp.edit()

        edShared.putString("matchname",placar.nome_partida)
        edShared.putBoolean("has_timer",placar.has_timer)
        edShared.commit()
    }

    //lendo configuração do placar
    fun openConfig()
    {
        val sharedFilename = "configPlacar"
        val sp:SharedPreferences = getSharedPreferences(sharedFilename,Context.MODE_PRIVATE)
        placar.nome_partida=sp.getString("matchname","Jogo Padrão").toString()
        placar.has_timer=sp.getBoolean("has_timer",false)
    }

    //
    fun initInterface(){
        val tv= findViewById<EditText>(R.id.editTextGameName)
        tv.setHint("Digite o nome da partida")
        val sw= findViewById<Switch>(R.id.swTimer)
        sw.isChecked= false
    }

    //
    fun updatePlacarConfig(){
        val tv= findViewById<EditText>(R.id.editTextGameName)
        val sw= findViewById<Switch>(R.id.swTimer)
        placar.nome_partida= tv.text.toString()
        placar.has_timer=sw.isChecked
    }

    //inicia a activity do placar, executado a aprtir do click do botao
    fun openPlacar(v: View){ //Executa ao click do Iniciar Jogo

        updatePlacarConfig() //Pega da Interface e joga no placar
        saveConfig() //Salva no Shared preferences

        val intent = Intent(this, PlacarActivity::class.java).apply{
            putExtra("placar", placar ).putExtra("bd", bd)
        }

        startActivity(intent)
    }
}