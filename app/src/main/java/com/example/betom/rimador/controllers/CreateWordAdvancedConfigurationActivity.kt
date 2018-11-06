package com.example.betom.rimador.controllers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import com.example.betom.rimador.R
import com.example.betom.rimador.utilities.DEFAULT_MAX_SYLLABLES
import com.example.betom.rimador.utilities.DEFAULT_MIN_SYLLABLES
import com.example.betom.rimador.wordHandlers.WordCreator
import kotlinx.android.synthetic.main.activity_create_word.*

class CreateWordAdvancedConfigurationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_word_advanced_configuration)
    }

    fun generateWordsAdvancedButtonClicked(view:View){
        val exceptions=ArrayList<Int>()

        val wuCheckButton: CheckBox =findViewById(R.id.addWUCheckButton)
        if(wuCheckButton.isChecked)
            exceptions.add(0)
        val yiCheckButton: CheckBox =findViewById(R.id.addYICheckButton)
        if(yiCheckButton.isChecked)
            exceptions.add(1)
        val phshckCheckButton: CheckBox =findViewById(R.id.addPHSHCKCheckButton)
        if(phshckCheckButton.isChecked)
            exceptions.add(2)
        val expqCheckButton: CheckBox =findViewById(R.id.expQCheckButton)
        if(expqCheckButton.isChecked)
            exceptions.add(3)
        val changeSyllableCheckButton: CheckBox =findViewById(R.id.replaceSyllableCheckButton)
        if(changeSyllableCheckButton.isChecked)
            exceptions.add(4)
        val wrongWordCheckButton: CheckBox =findViewById(R.id.wrongWordsCheckButton)
        if(wrongWordCheckButton.isChecked)
            exceptions.add(5)

        val wordCreator= WordCreator()
        val minimum=if(minSyllablesText.text.isNotEmpty())
            minSyllablesText.text.toString().toInt()
        else
            DEFAULT_MIN_SYLLABLES

        val maximum=if(maxSyllablesText.text.isNotEmpty())
            maxSyllablesText.text.toString().toInt()
        else
            DEFAULT_MAX_SYLLABLES

        if(minimum.toString().toInt()>= DEFAULT_MIN_SYLLABLES && maximum >= minimum && maximum<= DEFAULT_MAX_SYLLABLES){
            wordCreator.minSyllables=minimum
            wordCreator.setMaxSyllables(maximum)


        }else{
            Log.d("ERROR","both min and max values have to be between $DEFAULT_MIN_SYLLABLES and $DEFAULT_MAX_SYLLABLES")
            Toast.makeText(this,"Pone valores entre $DEFAULT_MIN_SYLLABLES y $DEFAULT_MAX_SYLLABLES", Toast.LENGTH_SHORT).show()
        }
    }
}
