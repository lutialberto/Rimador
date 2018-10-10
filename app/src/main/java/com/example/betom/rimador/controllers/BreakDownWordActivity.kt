package com.example.betom.rimador.controllers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
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

    private fun showWordInformation(labelMessage:String, dataGetter: (Word) -> List<String>) {
        if(enterWordText.length()>20)
            Toast.makeText(this,"La palabra no puede superar las 20 letras", Toast.LENGTH_LONG).show()
        else{
            val enteredWord= Word(enterWordText.text.toString())
            if(!enteredWord.hasErrors())
                updateView(dataGetter.invoke(enteredWord),labelMessage,enterWordText.text.toString())
            else{
                Toast.makeText(this,enteredWord.errorMessage, Toast.LENGTH_SHORT).show()
                Log.d("ERROR","Error: ${enteredWord.errorMessage}.")
            }
        }
    }

    fun clearClicked(view: View){
        updateView(ArrayList<String>(), WAITING_FOR_INPUT,getString(R.string.empty_string))
        enterWordText.text.clear()
    }

    fun separateSyllablesClicked(view: View){
        val inputWord=Word(enterWordText.text.toString())
        val wordCorrector=InputWordCorrector()
        if(wordCorrector.showWordInformation(this,inputWord))
            updateView(inputWord.intoSyllables(),getString(R.string.separada_en_silabas),inputWord.toString())
    }

    fun vocalStructureClicked(view: View) {
        showWordInformation(getString(R.string.estructura_vocal)) { w:Word ->
            w.getAssonatingStructure()
        }
    }

    fun assonantRhymeButtonClicked(view: View){
        showWordInformation(getString(R.string.rima_asonante)) {w:Word ->
            w.getRhyme(false)
        }
    }

    fun consonantRhymeButtonClicked(view: View){
        showWordInformation(getString(R.string.rima_consonante)) {w:Word ->
            w.getRhyme(true)
        }
    }

}
