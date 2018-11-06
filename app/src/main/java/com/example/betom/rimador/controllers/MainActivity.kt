package com.example.betom.rimador.controllers

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.betom.rimador.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun goToWordAnalyzerButtonClicked(view: View){
        val wordAnalizerIntent=Intent(this,BreakDownWordActivity::class.java)
        startActivity(wordAnalizerIntent)
    }

    fun goToSearchRhymeButtonClicked(view: View){
        val searchRhymeIntent=Intent(this,SearchRhymeActivity::class.java)
        startActivity(searchRhymeIntent)
    }

    fun goToGenerateWordsButtonClicked(view: View){
        val generateWordsIntent=Intent(this,CreateWordActivity::class.java)
        startActivity(generateWordsIntent)
    }
}
