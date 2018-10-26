package com.example.betom.rimador.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.betom.rimador.R
import com.example.betom.rimador.models.Word

class WordAdapter(val context: Context, private val wordList:ArrayList<Word>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val wordView:View=LayoutInflater.from(context).inflate(R.layout.view_syllable_item,null)
        val wordText: TextView= wordView.findViewById(R.id.syllableTextView)
        val word=wordList[position]

        wordText.text=word.toString()

        return wordView
    }

    override fun getItem(position: Int): Any {
        return wordList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return wordList.count()
    }
}