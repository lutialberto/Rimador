package com.example.betom.rimador.controladores

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.betom.rimador.R
import com.example.betom.rimador.modelos.Palabra
import kotlinx.android.synthetic.main.activity_separar_palabra.*

class SepararPalabraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_separar_palabra)
    }

    fun separarEnSilabasClickeado(view: View){
        if(ingresarPalabraText.length()>20)
            Toast.makeText(this,"La palabra no puede superar las 20 letras", Toast.LENGTH_LONG).show()
        else{
            val palabraIngresada= Palabra(ingresarPalabraText.text.toString())

            if(!palabraIngresada.tieneErrores()) {
                palabraSeparadaText.text = palabraIngresada.separadaEnSilabas()
                ingresarPalabraText.text.clear()
                Log.d("ESTVOC","estructura de la palabra $palabraIngresada: ${palabraIngresada.getEstructuraVocal()}")
            }
            else{
                Toast.makeText(this,palabraIngresada.mensajeError, Toast.LENGTH_SHORT).show()
                Log.d("ERROR","Error: ${palabraIngresada.mensajeError}.")
            }
        }
    }
}
