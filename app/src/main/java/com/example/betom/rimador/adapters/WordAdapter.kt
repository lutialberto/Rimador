package com.example.betom.rimador.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.betom.rimador.R
import com.example.betom.rimador.models.Word

class WordAdapter(val context: Context, private val wordList:ArrayList<Word>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val wordView:View
        val holder:ViewHolder

        if(convertView==null){
            wordView=LayoutInflater.from(context).inflate(R.layout.view_syllable_item,null)
            holder=ViewHolder()
            holder.wordText= wordView.findViewById(R.id.syllableTextView)

            wordView.tag=holder
        } else {
            holder=convertView.tag as ViewHolder
            wordView=convertView
        }

        val word=wordList[position]

        holder.wordText?.text=word.toString()

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

    private class ViewHolder {
        var wordText: TextView?=null

    }
}