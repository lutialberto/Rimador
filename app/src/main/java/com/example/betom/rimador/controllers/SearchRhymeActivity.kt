package com.example.betom.rimador.controllers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.betom.rimador.R
import com.example.betom.rimador.models.Word
import com.example.betom.rimador.services.WordService
import com.example.betom.rimador.wordHandlers.InputWordCorrector
import com.example.betom.rimador.wordHandlers.StringCodifier
import com.example.betom.rimador.utilities.URL_FIND_WORD_ASSONANT_RHYME
import com.example.betom.rimador.utilities.URL_FIND_WORD_CONSONANT_RHYME
import com.example.betom.rimador.utilities.WAITING_FOR_INPUT
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

        searchChosenText.text= WAITING_FOR_INPUT
        searchParameterText.text=""
        matchesCountText.text=""
        coincidenceText.text=""

        enableSpinner(false)
    }

    private fun enableSpinner(enable:Boolean){
        if(enable){
            searchSpinner.visibility=View.VISIBLE
        }else{
            searchSpinner.visibility=View.INVISIBLE
        }
        searchConsonantRhymeButton.isEnabled=!enable
        searchAssonantRhymeButton.isEnabled=!enable
    }

    private fun searchRhyme(consonantRhyme:Boolean,rhymeUrl:String,resourceString:String) {
        enableSpinner(true)

        val inputWord = Word(newWordText.text.toString())
        val list = inputWord.getRhyme(consonantRhyme)
        val stringList = inputWord.intoString(!consonantRhyme, list)

        val codifier = StringCodifier()
        val url = "$rhymeUrl${codifier.getDBText(stringList)}"

        val wordCorrector = InputWordCorrector()
        if (wordCorrector.validateWord(this, inputWord)) {

            WordService.findWords(this, url) { complete ->
                if (complete) {
                    wordAdapter.notifyDataSetChanged()

                    searchChosenText.text = resourceString
                    searchParameterText.text = stringList
                    matchesCountText.text = WordService.words.size.toString()
                    coincidenceText.text = getString(R.string.coincidence)
                } else {
                    Toast.makeText(this, "Algo salio mal", Toast.LENGTH_SHORT).show()
                }
                enableSpinner(false)
            }
        } else {
            Log.d("ERROR", "Error: ${inputWord.errorMessage}")
            enableSpinner(false)
        }
    }

    fun searchAssonantRhymeClicked(view :View) {
        searchRhyme(false, URL_FIND_WORD_ASSONANT_RHYME,getString(R.string.rima_asonante))
    }

    fun searchConsonantRhymeClicked(view: View) {
        searchRhyme(true, URL_FIND_WORD_CONSONANT_RHYME,getString(R.string.rima_consonante))
    }
}
