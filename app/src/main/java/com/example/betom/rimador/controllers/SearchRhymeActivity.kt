package com.example.betom.rimador.controllers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.betom.rimador.R
import com.example.betom.rimador.models.Word
import com.example.betom.rimador.services.WordService
import com.example.betom.rimador.wordHandlers.InputWordCorrector
import com.example.betom.rimador.wordHandlers.StringCodifier
import com.example.betom.rimador.utilities.URL_FIND_WORD_ASSONANT_RHYME
import com.example.betom.rimador.utilities.URL_FIND_WORD_CONSONANT_RHYME
import com.example.betom.rimador.utilities.WAITING_FOR_INPUT
import com.reddit.indicatorfastscroll.FastScrollerThumbView
import com.reddit.indicatorfastscroll.FastScrollerView
import kotlinx.android.synthetic.main.activity_search_rhyme.*
import com.example.betom.rimador.adapters.FastScroller

class SearchRhymeActivity : AppCompatActivity() {

    private lateinit var fastScroller : FastScroller

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

    private fun setupAdapters(){
        val wordRecycleAdapter=findViewById<RecyclerView>(R.id.wordsListView)
        val fastScrollerView=findViewById<FastScrollerView>(R.id.wordsFastscroller)
        val fastScrollerThumbView=findViewById<FastScrollerThumbView>(R.id.wordsFastscroller_thumb)
        fastScroller=FastScroller(this,wordRecycleAdapter,fastScrollerView,fastScrollerThumbView){
            //HACE ALGO CUANDO LO CLICKEAS??
        }
    }

    /*
    * enableSpinner -> true: show spinner and disable view functionality
    * enableSpinner -> false: hide spinner and block enable view functionality
     */
    private fun enableSpinner(enableSpinner:Boolean){
        if(enableSpinner){
            searchSpinner.visibility=View.VISIBLE
        }else{
            searchSpinner.visibility=View.INVISIBLE
        }
        searchConsonantRhymeButton.isEnabled=!enableSpinner
        searchAssonantRhymeButton.isEnabled=!enableSpinner
    }

    /*
    * 1. enable spinner and disable all other functionality of the view
    * 2. extract the input word and check if it is a valid input
    * 3. get the rhyme(consonant or assonant) and aply the codifier to change the result to a correct DB format
    * 4. use the word service class to call find words why the same rhyme
    * 5. success -> notify data set changed, update view,
    * 6. else -> log errors, notify user and disable spinner
    * */
    private fun searchRhyme(consonantRhyme:Boolean,rhymeUrl:String,resourceString:String) {
        enableSpinner(true)
        val inputWord = Word(newWordText.text.toString())
        if (InputWordCorrector().validateWord(this, inputWord)) {

            val arrayListRhyme = inputWord.getRhyme(consonantRhyme)
            val stringRhyme = inputWord.intoString(!consonantRhyme, arrayListRhyme)
            val dbFormatRhyme=StringCodifier().getDBText(stringRhyme)

            val url = "$rhymeUrl$dbFormatRhyme"

            WordService.findWords(this, url) { complete ->
                if (complete) {
                    fastScroller.wordRecyclerAdapter.notifyDataSetChanged()

                    searchChosenText.text = resourceString
                    searchParameterText.text = stringRhyme
                    matchesCountText.text = WordService.words.size.toString()
                    coincidenceText.text = getString(R.string.coincidence)

                } else {
                    Toast.makeText(this, "Algo salio mal, proba de nuevo", Toast.LENGTH_SHORT).show()
                }
                enableSpinner(false)
            }
        } else {
            Log.d("ERROR", "Error: ${inputWord.errorMessage}")
            enableSpinner(false)
        }
    }

    /*
    * find and list words from the DB that has the same assonant rhyme
    * */
    fun searchAssonantRhymeClicked(view :View) {
        searchRhyme(false, URL_FIND_WORD_ASSONANT_RHYME,getString(R.string.rima_asonante))
    }

    /*
    * find and list words from the DB that has the same consonant rhyme
    * */
    fun searchConsonantRhymeClicked(view: View) {
        searchRhyme(true, URL_FIND_WORD_CONSONANT_RHYME,getString(R.string.rima_consonante))
    }
}
