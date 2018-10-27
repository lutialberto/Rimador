package com.example.betom.rimador.controllers

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.betom.rimador.R
import com.example.betom.rimador.adapters.WordRecycleAdapter
import com.example.betom.rimador.models.Word
import com.example.betom.rimador.services.WordService
import com.example.betom.rimador.utilities.BROADCAST_SEARCH_COMPLETED
import com.example.betom.rimador.wordHandlers.InputWordCorrector
import com.example.betom.rimador.wordHandlers.StringCodifier
import com.example.betom.rimador.utilities.URL_FIND_WORD_ASSONANT_RHYME
import com.example.betom.rimador.utilities.URL_FIND_WORD_CONSONANT_RHYME
import com.example.betom.rimador.utilities.WAITING_FOR_INPUT
import com.reddit.indicatorfastscroll.FastScrollItemIndicator
import com.reddit.indicatorfastscroll.FastScrollerThumbView
import com.reddit.indicatorfastscroll.FastScrollerView
import kotlinx.android.synthetic.main.activity_search_rhyme.*

class SearchRhymeActivity : AppCompatActivity() {

    private lateinit var wordRecyclerAdapter: WordRecycleAdapter
    private lateinit var fastScrollerView: FastScrollerView
    private lateinit var fastScrollerThumbView: FastScrollerThumbView

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
        wordRecyclerAdapter= WordRecycleAdapter(this,WordService.words)
        wordsListView.adapter= this.wordRecyclerAdapter

        val layoutManager=LinearLayoutManager(this)
        wordsListView.layoutManager=layoutManager

        fastScrollerView = findViewById(R.id.fastscroller)
        val recyclerView : RecyclerView = findViewById(R.id.wordsListView)
        fastScrollerView.apply {
            fastScrollerView.setupWithRecyclerView(
                    recyclerView,
                    { position ->
                        val letter = WordService.words[position] // Get your model object
                        // or fetch the section at [position] from your database
                        FastScrollItemIndicator.Text(
                                letter.toString().substring(0, 1).toUpperCase() // Grab the first letter and capitalize it
                        ) // Return a text indicator
                    }
            )
        }

        fastScrollerView.useDefaultScroller = false
        fastScrollerView.itemIndicatorSelectedCallbacks += object : FastScrollerView.ItemIndicatorSelectedCallback {
            override fun onItemIndicatorSelected(
                    indicator: FastScrollItemIndicator,
                    indicatorCenterY: Int,
                    itemPosition: Int
            ) {
                // Handle scrolling
                recyclerView.stopScroll()
                wordsListView.scrollToPosition(itemPosition)
            }
        }

        fastScrollerThumbView = findViewById(R.id.fastscroller_thumb)
        fastScrollerThumbView.apply {
            setupWithFastScroller(fastScrollerView)
        }
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
        if (InputWordCorrector().validateWord(this, inputWord)) {

            val list = inputWord.getRhyme(consonantRhyme)
            val stringList = inputWord.intoString(!consonantRhyme, list)

            val url = "$rhymeUrl${StringCodifier().getDBText(stringList)}"

            WordService.findWords(this, url) { complete ->
                if (complete) {
                    wordRecyclerAdapter.notifyDataSetChanged()

                    searchChosenText.text = resourceString
                    searchParameterText.text = stringList
                    matchesCountText.text = WordService.words.size.toString()
                    coincidenceText.text = getString(R.string.coincidence)

                    val searchCompleted=Intent(BROADCAST_SEARCH_COMPLETED)
                    LocalBroadcastManager.getInstance(this).sendBroadcast(searchCompleted)
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

    private fun scrollToFirstItem(c:Char){

        val firstItem=WordService.words.find { w ->
            w.toString().first()==c
        }
        wordsListView.scrollToPosition(WordService.words.indexOf(firstItem))
    }

    fun searchAssonantRhymeClicked(view :View) {
        searchRhyme(false, URL_FIND_WORD_ASSONANT_RHYME,getString(R.string.rima_asonante))
    }

    fun searchConsonantRhymeClicked(view: View) {
        searchRhyme(true, URL_FIND_WORD_CONSONANT_RHYME,getString(R.string.rima_consonante))
    }
}
