package com.example.betom.rimador.controllers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.betom.rimador.R
import com.example.betom.rimador.adapters.SyllableRecyclerAdapter
import com.example.betom.rimador.models.Word
import kotlinx.android.synthetic.main.activity_separate_word.*

class DivideWordActivity : AppCompatActivity() {

    private lateinit var adapter : SyllableRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_separate_word)

        setupSyllableAdapter()
    }

    private fun setupSyllableAdapter(){
        val rimador=Word("rimador")

        adapter= SyllableRecyclerAdapter(this,rimador.intoSyllables())
        syllablesListView.adapter=adapter

        val layoutManager= LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        syllablesListView.layoutManager=layoutManager
    }

    fun separateSyllablesClicked(view: View){
        if(enterWordText.length()>20)
            Toast.makeText(this,"La palabra no puede superar las 20 letras", Toast.LENGTH_LONG).show()
        else{
            val enteredWord= Word(enterWordText.text.toString())

            if(!enteredWord.hasErrors()) {

                adapter= SyllableRecyclerAdapter(this,enteredWord.intoSyllables())
                syllablesListView.adapter=adapter

                enterWordText.text.clear()

                Log.d("ESTVOC","estructura de la palabra $enteredWord: ${enteredWord.getAssonatingStructure()}")
                Log.d("RIMA","rima consonante de $enteredWord -> ${enteredWord.getRhyme(true)}")
                Log.d("RIMA","rima asonante de $enteredWord -> ${enteredWord.getRhyme(false)}")
            }
            else{
                Toast.makeText(this,enteredWord.errorMessage, Toast.LENGTH_SHORT).show()
                Log.d("ERROR","Error: ${enteredWord.errorMessage}.")
            }
        }
    }
}
