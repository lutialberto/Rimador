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

class SeparateWordActivity : AppCompatActivity() {

    private lateinit var adapter : SyllableRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_separate_word)

        setupSyllableAdapter()
    }

    private fun setupSyllableAdapter(){
        val rimador=Word("rimador")
        enteredWordLabel.text=getString(R.string.rimador)
        actionLabel.text=getString(R.string.separada_en_silabas)

        adapter= SyllableRecyclerAdapter(this,rimador.intoSyllables())
        syllablesListView.adapter=adapter

        val layoutManager= LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        syllablesListView.layoutManager=layoutManager
    }

    private fun showWordInformation(labelMessage:String, dataGetter: (Word) -> List<String>) {
        if(enterWordText.length()>20)
            Toast.makeText(this,"La palabra no puede superar las 20 letras", Toast.LENGTH_LONG).show()
        else{
            val enteredWord= Word(enterWordText.text.toString())

            if(!enteredWord.hasErrors()) {

                adapter= SyllableRecyclerAdapter(this,dataGetter.invoke(enteredWord))
                syllablesListView.adapter=adapter

                enteredWordLabel.text=enterWordText.text
                actionLabel.text=labelMessage

                enterWordText.text.clear()

            }
            else{
                Toast.makeText(this,enteredWord.errorMessage, Toast.LENGTH_SHORT).show()
                Log.d("ERROR","Error: ${enteredWord.errorMessage}.")
            }
        }
    }

    fun separateSyllablesClicked(view: View){
        showWordInformation(getString(R.string.separada_en_silabas)) { w:Word ->
            w.intoSyllables()
        }
    }

    fun vocalStructureClicked(view: View) {
        showWordInformation(getString(R.string.estructura_vocal)) { w:Word ->
            w.getAssonatingStructure()
        }
    }
}
