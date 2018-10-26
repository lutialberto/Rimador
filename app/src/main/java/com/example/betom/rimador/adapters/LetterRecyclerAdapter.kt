package com.example.betom.rimador.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.betom.rimador.R

class LetterRecycleAdapter(val context: Context, private val letterList: CharArray) : RecyclerView.Adapter<LetterRecycleAdapter.Holder>() {

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindWord(letterList[position],context)
    }

    override fun getItemCount(): Int {
        return letterList.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view=LayoutInflater.from(parent.context)
                .inflate(R.layout.letter_list_item,parent,false)
        return Holder(view)
    }

    //acomodar este
    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val letterText= itemView.findViewById<TextView>(R.id.letterText)!!

        fun bindWord(c:Char,context: Context){
            letterText.text=c.toString()
        }
    }
}