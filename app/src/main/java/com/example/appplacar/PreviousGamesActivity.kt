package com.example.appplacar

import adapters.CustomAdapter
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import data.Placar

class PreviousGamesActivity : AppCompatActivity() {
    lateinit var bd:ArrayList<Placar>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_previous_games)

        bd = getIntent().getExtras()?.getSerializable("bd") as ArrayList<Placar>

        // Trazendo o Recycler View
        val recyclerview = findViewById<RecyclerView>(R.id.rcPreviousGames)

        // Tipo de Layout Manager será Linear
        recyclerview.layoutManager = LinearLayoutManager(this)

        // ArrayList enviado ao Adapter
        val adapter = CustomAdapter(bd)

        // Setando o Adapter no Recyclerview
        recyclerview.adapter = adapter
    }
}