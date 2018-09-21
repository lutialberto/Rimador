package com.example.betom.rimador.controladores

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.betom.rimador.R
import com.example.betom.rimador.servicios.Silabeador
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun silabearClikeado(view: View){
        if(ingresarPalabraText.length()>20)
            Toast.makeText(this,"La palabra no puede superar las 20 letras",Toast.LENGTH_LONG).show()
        else{
            val palabraSeparada=Silabeador.separarEnSilabas("${ingresarPalabraText.text.toString().toLowerCase().trim()} ")
            if(palabraSeparada.sePuedeSeparar()) {
                palabraSeparadaText.text = palabraSeparada.toString()
                ingresarPalabraText.text.clear()
            }
            else{
                Toast.makeText(this,palabraSeparada.mensajeError,Toast.LENGTH_SHORT).show()
                Log.d("ERROR","Error: ${palabraSeparada.mensajeError}")
            }
        }
    }
}
