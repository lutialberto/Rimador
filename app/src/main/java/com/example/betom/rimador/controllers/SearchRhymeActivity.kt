package com.example.betom.rimador.controllers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.betom.rimador.R
import com.example.betom.rimador.models.Word
import com.example.betom.rimador.services.AuthService
import com.example.betom.rimador.services.InputWordCorrector
import kotlinx.android.synthetic.main.activity_break_down_word.*
import kotlinx.android.synthetic.main.activity_search_rhyme.*

class SearchRhymeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_rhyme)
    }

    fun searchAssonantRhymeClicked(view :View) {
        val inputWord=Word(newWordText.text.toString())
        val wordCorrector= InputWordCorrector()
        if(wordCorrector.showWordInformation(this,inputWord))
            AuthService.addWord(this,inputWord){complete ->
                if(complete){
                    Log.d("TRANSACCION","salio bien")
                }
            }
    }

    fun searchConsonantRhymeClicked() {

    }

}
