package com.example.betom.rimador.controllers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.example.betom.rimador.R
import com.example.betom.rimador.adapters.SyllableRecyclerAdapter
import com.example.betom.rimador.models.Word
import com.example.betom.rimador.wordHandlers.InputWordCorrector
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

    /*
    * 1. setup a new adapter for the new given information
    * 2. show the new input word and the action performed
    * */
    private fun updateView(elements: List<String>, action:String, wordLabel:String){
        adapter= SyllableRecyclerAdapter(this,elements)
        syllablesListView.adapter=adapter

        enteredWordLabel.text=wordLabel
        actionLabel.text=action
    }

    /*
    * 1. resets the word input and show the waiting new word message
    * */
    fun clearClicked(view: View){
        updateView(ArrayList(), WAITING_FOR_INPUT,getString(R.string.empty_string))
        enterWordText.text.clear()
    }

    /*
    * 1. validate the input string
    * 2. success -> separate into syllables and update view
    * 3. else -> log errors and inform to the user
    * */
    fun separateSyllablesClicked(view: View){
        val inputWord=Word(enterWordText.text.toString())
        val wordCorrector= InputWordCorrector()
        if(wordCorrector.validateWord(this,inputWord))
            updateView(inputWord.intoSyllables(),getString(R.string.separada_en_silabas),inputWord.toString())
    }

    /*
    * 1. validate the input string
    * 2. success -> get word's vocal structure and update view
    * 3. else -> log errors and inform to the user
    * */
    fun vocalStructureClicked(view: View) {
        val inputWord=Word(enterWordText.text.toString())
        val wordCorrector= InputWordCorrector()
        if(wordCorrector.validateWord(this,inputWord))
            updateView(inputWord.getAssonatingStructure(),getString(R.string.estructura_vocal),inputWord.toString())
    }

    /*
    * 1. validate the input string
    * 2. success -> get word's assonant rhyme and update view
    * 3. else -> log errors and inform to the user
    * */
    fun assonantRhymeButtonClicked(view: View){
        val inputWord=Word(enterWordText.text.toString())
        val wordCorrector= InputWordCorrector()
        if(wordCorrector.validateWord(this,inputWord))
            updateView(inputWord.getRhyme(false),getString(R.string.rima_asonante),inputWord.toString())
    }

    /*
    * 1. validate the input string
    * 2. success -> get word's consonant rhyme and update view
    * 3. else -> log errors and inform to the user
    * */
    fun consonantRhymeButtonClicked(view: View){
        val inputWord=Word(enterWordText.text.toString())
        val wordCorrector= InputWordCorrector()
        if(wordCorrector.validateWord(this,inputWord))
            updateView(inputWord.getRhyme(true),getString(R.string.rima_consonante),inputWord.toString())
    }
}
