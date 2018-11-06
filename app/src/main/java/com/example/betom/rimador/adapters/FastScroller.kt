package com.example.betom.rimador.adapters

import android.content.Context
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.betom.rimador.services.WordService
import com.reddit.indicatorfastscroll.FastScrollItemIndicator
import com.reddit.indicatorfastscroll.FastScrollerThumbView
import com.reddit.indicatorfastscroll.FastScrollerView

class FastScroller(context:Context,wordRecyclerView:RecyclerView,fastScrollerView:FastScrollerView,fastScrollerThumbView: FastScrollerThumbView) {
    lateinit var wordRecyclerAdapter: WordRecycleAdapter

    init {
        setupAdapters(wordRecyclerView,context)
        setupFastScroll(context,wordRecyclerView,fastScrollerView,fastScrollerThumbView)
    }

    private fun setupAdapters(wordRecyclerView: RecyclerView,context: Context){
        wordRecyclerAdapter= WordRecycleAdapter(context, WordService.words)
        wordRecyclerView.adapter= this.wordRecyclerAdapter

        val layoutManager= LinearLayoutManager(context)
        wordRecyclerView.layoutManager=layoutManager
    }

    private fun setupFastScroll(context: Context,wordRecyclerView: RecyclerView,fastScrollerView: FastScrollerView,fastScrollerThumbView: FastScrollerThumbView){
        //setup item divider for word recycler
        wordRecyclerView.addItemDecoration(DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL))

        //
        fastScrollerView.apply {
            fastScrollerView.setupWithRecyclerView(
                    wordRecyclerView,
                    { position ->
                        val word = WordService.words[position] // Get your model object
                        // or fetch the section at [position] from your database
                        FastScrollItemIndicator.Text(
                                word.toString().substring(0, 1).toUpperCase() // Grab the first letter and capitalize it
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
                wordRecyclerView.stopScroll()
                wordRecyclerView.scrollToPosition(itemPosition)
            }
        }

        //setup thumb scroll as a complement the scroll view
        fastScrollerThumbView.apply {
            setupWithFastScroller(fastScrollerView)
        }
    }

}