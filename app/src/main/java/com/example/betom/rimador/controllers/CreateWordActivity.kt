package com.example.betom.rimador.controllers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.betom.rimador.R
import com.example.betom.rimador.adapters.WordRecycleAdapter
import com.example.betom.rimador.models.Word
import com.example.betom.rimador.services.WordService
import com.example.betom.rimador.wordHandlers.WordCreator
import com.reddit.indicatorfastscroll.FastScrollItemIndicator
import com.reddit.indicatorfastscroll.FastScrollerThumbView
import com.reddit.indicatorfastscroll.FastScrollerView
import kotlinx.android.synthetic.main.activity_create_word.*

class CreateWordActivity : AppCompatActivity() {

    private lateinit var wordRecyclerAdapter: WordRecycleAdapter
    private lateinit var fastScrollerView: FastScrollerView
    private lateinit var fastScrollerThumbView: FastScrollerThumbView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_word)

        setupAdapters()
        setupFastScroll()

    }

    fun generateWordsButtonClicked(view: View){
        val wordCreator= WordCreator()
        val minimum=if(minSyllablesText.text.isNotEmpty())
            minSyllablesText.text.toString().toInt()
        else
            0
        val maximum=if(maxSyllablesText.text.isNotEmpty())
            maxSyllablesText.text.toString().toInt()
        else
            0
        if(minimum.toString().toInt()>0 && maximum >= minimum){
            wordCreator.minSyllables=minimum
            wordCreator.setMaxSyllables(maximum)
        }else{
            Log.d("ERROR","both min and max values have to be bigger than zero and max bigger or equal to min")
            Toast.makeText(this,"valores erroneos, pone otros",Toast.LENGTH_SHORT).show()
        }

        Log.d("GENERATE","minimum -> |$minimum|, maximum -> |$maximum|")
        WordService.words.clear()
        WordService.words.addAll(wordCreator.getWords().map { Word(it) } as ArrayList<Word>)
        WordService.words.sortBy { it.toString() }

        wordRecyclerAdapter.notifyDataSetChanged()
    }

    private fun setupAdapters(){
        wordRecyclerAdapter= WordRecycleAdapter(this, WordService.words)
        generatedWordsListView.adapter= this.wordRecyclerAdapter

        val layoutManager= LinearLayoutManager(this)
        generatedWordsListView.layoutManager=layoutManager

    }

    private fun setupFastScroll(){
        //innitialize fast scroll and recycler
        fastScrollerView = findViewById(R.id.generatedWordsFastscroller)
        val recyclerView : RecyclerView = findViewById(R.id.generatedWordsListView)

        //setup item divider for word recycler
        recyclerView.addItemDecoration(DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL))

        //
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

        //change the conventional way to scroll for a jump to x position of the recycler
        fastScrollerView.useDefaultScroller = false
        fastScrollerView.itemIndicatorSelectedCallbacks += object : FastScrollerView.ItemIndicatorSelectedCallback {
            override fun onItemIndicatorSelected(
                    indicator: FastScrollItemIndicator,
                    indicatorCenterY: Int,
                    itemPosition: Int
            ) {
                // Handle scrolling
                recyclerView.stopScroll()
                generatedWordsListView.scrollToPosition(itemPosition)
            }
        }

        //setup thumb scroll as a complement the scroll view
        fastScrollerThumbView = findViewById(R.id.generatedWordsFastscroller_thumb)
        fastScrollerThumbView.apply {
            setupWithFastScroller(fastScrollerView)
        }
    }

}
