package com.example.betom.rimador.controllers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import com.example.betom.rimador.R
import com.example.betom.rimador.models.Word
import com.example.betom.rimador.services.WordService
import com.example.betom.rimador.services.InputWordCorrector
import kotlinx.android.synthetic.main.activity_search_rhyme.*

class SearchRhymeActivity : AppCompatActivity() {

    private lateinit var wordAdapter: ArrayAdapter<Word>

    private fun setupAdapters(){
        wordAdapter= ArrayAdapter(this,android.R.layout.simple_list_item_1,WordService.words)
        wordsListView.adapter= this.wordAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_rhyme)
        setupAdapters()
    }

    fun searchAssonantRhymeClicked(view :View) {
        val inputWord=Word(newWordText.text.toString())
        val wordCorrector= InputWordCorrector()
        if(wordCorrector.validateWord(this,inputWord))
            WordService.addWord(this,inputWord){ complete ->
                if(complete){
                    Log.d("TRANSACCION","salio bien")
                }
            }
    }

    fun searchConsonantRhymeClicked(view: View) {
        val inputWord=Word(newWordText.text.toString())
        val consonantSufix=inputWord.getRhyme(true).toString()
        val wordCorrector= InputWordCorrector()
        if(wordCorrector.validateWord(this,inputWord))
            WordService.getConsonantRhymeWords(this,consonantSufix){ complete ->
                if(complete){
                    wordAdapter.notifyDataSetChanged()
                }
            }
        else{
            Log.d("ERROR","Error: ${inputWord.errorMessage}")
        }
    }

}
