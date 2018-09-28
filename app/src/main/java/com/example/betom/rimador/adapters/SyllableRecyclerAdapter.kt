package com.example.betom.rimador.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.betom.rimador.R
import com.example.betom.rimador.models.Syllable

class SyllableRecyclerAdapter(val context: Context, val syllables: List<Syllable>) : RecyclerView.Adapter<SyllableRecyclerAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): Holder {
        val view=LayoutInflater.from(parent.context)
                .inflate(R.layout.view_syllable_item,parent,false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return syllables.count()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindSyllable(syllables[position])
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val syllableWords= itemView.findViewById<TextView>(R.id.syllableTextView)!!

        fun bindSyllable(syllable:Syllable) {
            syllableWords.text=syllable.toString()
        }
    }
}