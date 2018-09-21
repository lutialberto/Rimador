package com.example.betom.rimador.controladores

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.betom.rimador.R
import com.example.betom.rimador.servicios.Silabeador
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun silabearClikeado(view: View){
        silabeadaText.text=Silabeador.separar("${entradaText.text.toString().toLowerCase().trim()} ")
    }
}
