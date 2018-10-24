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
import com.example.betom.rimador.services.StringCodifier
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
    }

    fun searchAssonantRhymeClicked(view :View) {
        val inputWord=Word(newWordText.text.toString())
        val list=inputWord.getRhyme(false)
        val stringList=inputWord.intoString(true,list)

        val codifier=StringCodifier()
        val url="$URL_FIND_WORD_ASSONANT_RHYME${codifier.getDBText(stringList)}"

        val wordCorrector= InputWordCorrector()
        if(wordCorrector.validateWord(this,inputWord))
            WordService.findWords(this,url){ complete ->
                if(complete){
                    wordAdapter.notifyDataSetChanged()

                    searchChosenText.text= getString(R.string.rima_asonante)
                    searchParameterText.text=stringList
                    matchesCountText.text= WordService.words.size.toString()
                    coincidenceText.text=getString(R.string.coincidence)
                }
            }
        else{
            Log.d("ERROR","Error: ${inputWord.errorMessage}")
        }
    }

    fun searchConsonantRhymeClicked(view: View) {
        val inputWord=Word(newWordText.text.toString())
        val list=inputWord.getRhyme(true)
        val stringList=inputWord.intoString(false,list)

        val codifier=StringCodifier()
        val url="$URL_FIND_WORD_CONSONANT_RHYME${codifier.getDBText(stringList)}"

        val wordCorrector= InputWordCorrector()
        if(wordCorrector.validateWord(this,inputWord))
            WordService.findWords(this,url){ complete ->
                if(complete){
                    wordAdapter.notifyDataSetChanged()

                    searchChosenText.text= getString(R.string.rima_consonante)
                    searchParameterText.text=stringList
                    matchesCountText.text= WordService.words.size.toString()
                    coincidenceText.text=getString(R.string.coincidence)
                }
            }
        else{
            Log.d("ERROR","Error: ${inputWord.errorMessage}")
        }
    }
}
