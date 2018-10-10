package com.example.betom.rimador.controllers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.betom.rimador.R
import com.example.betom.rimador.models.Word
import com.example.betom.rimador.services.AuthService
import kotlinx.android.synthetic.main.activity_search_rhyme.*

class SearchRhymeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_rhyme)
    }

    fun searchAssonantRhymeClicked() {
        if(newWordText.length()>20)
            Toast.makeText(this,"La palabra no puede superar las 20 letras", Toast.LENGTH_LONG).show()
        else {
            val filterWord = Word(newWordText.text.toString().trim())
            if (!filterWord.hasErrors()) {

                AuthService.addWord(this,filterWord){complete ->
                    if(complete){
                        Log.d("TRANSACCION","salio bien")
                    }
                }

            }
            else{
                Toast.makeText(this,filterWord.errorMessage, Toast.LENGTH_SHORT).show()
                Log.d("ERROR","Error: ${filterWord.errorMessage}.")
            }
        }
    }

    fun searchConsonantRhymeClicked() {

    }

}
