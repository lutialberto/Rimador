package com.example.betom.rimador.controllers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.example.betom.rimador.R
import com.example.betom.rimador.adapters.SyllableRecyclerAdapter
import com.example.betom.rimador.models.Word
import com.example.betom.rimador.services.InputWordCorrector
import com.example.betom.rimador.utilities.WAITING_FOR_INPUT
import kotlinx.android.synthetic.main.activity_break_down_word.*

class BreakDownWordActivity : AppCompatActivity() {

    private lateinit var adapter : SyllableRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_break_down_word)

        setupSyllableAdapter()
    }

    private fun setupSyllableAdapter(){
        val rimador=Word("rimador")

        updateView(rimador.intoSyllables(),getString(R.string.separada_en_silabas),getString(R.string.rimador))

        val layoutManager= LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        syllablesListView.layoutManager=layoutManager
    }

    private fun updateView(elements: List<String>, action:String, wordLabel:String){
        adapter= SyllableRecyclerAdapter(this,elements)
        syllablesListView.adapter=adapter

        enteredWordLabel.text=wordLabel
        actionLabel.text=action
    }

    fun clearClicked(view: View){
        updateView(ArrayList(), WAITING_FOR_INPUT,getString(R.string.empty_string))
        enterWordText.text.clear()
    }

    fun separateSyllablesClicked(view: View){
        val inputWord=Word(enterWordText.text.toString())
        val wordCorrector=InputWordCorrector()
        if(wordCorrector.validateWord(this,inputWord))
            updateView(inputWord.intoSyllables(),getString(R.string.separada_en_silabas),inputWord.toString())
    }

    fun vocalStructureClicked(view: View) {
        val inputWord=Word(enterWordText.text.toString())
        val wordCorrector=InputWordCorrector()
        if(wordCorrector.validateWord(this,inputWord))
            updateView(inputWord.getAssonatingStructure(),getString(R.string.separada_en_silabas),inputWord.toString())
    }

    fun assonantRhymeButtonClicked(view: View){
        val inputWord=Word(enterWordText.text.toString())
        val wordCorrector=InputWordCorrector()
        if(wordCorrector.validateWord(this,inputWord))
            updateView(inputWord.getRhyme(false),getString(R.string.separada_en_silabas),inputWord.toString())
    }

    fun consonantRhymeButtonClicked(view: View){
        val inputWord=Word(enterWordText.text.toString())
        val wordCorrector=InputWordCorrector()
        if(wordCorrector.validateWord(this,inputWord))
            updateView(inputWord.getRhyme(true),getString(R.string.separada_en_silabas),inputWord.toString())
    }

}
