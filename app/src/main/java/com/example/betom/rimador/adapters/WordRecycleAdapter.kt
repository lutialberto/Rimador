package com.example.betom.rimador.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.betom.rimador.R
import com.example.betom.rimador.models.Word

class WordRecycleAdapter(private val context: Context, private val wordList:ArrayList<Word>) : RecyclerView.Adapter<WordRecycleAdapter.Holder>() {

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindWord(wordList[position],context)
    }

    override fun getItemCount(): Int {
        return wordList.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view=LayoutInflater.from(parent.context)
                .inflate(R.layout.word_list_item,parent,false)
        return Holder(view)
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val wordText= itemView.findViewById<TextView>(R.id.wordTextView)!!

        fun bindWord(word:Word,context: Context){
            wordText.text=word.toString()
        }
    }
}