package com.example.betom.rimador.models

import com.example.betom.rimador.utilities.PREFIX
import com.example.betom.rimador.utilities.SUFIX

class CreatedWord( str: String) : Word(str) {

    private val hiddenSyllables=ArrayList<Syllable>()

    fun addHiddenSyllables(syllables:ArrayList<Syllable>){
        hiddenSyllables.addAll(syllables)
    }

    fun getPronunciation():String{
        val regex=Regex("$PREFIX|$SUFIX| |]|\\[|,")
        return hiddenSyllables.map { syllable ->  syllable.toString() }.toString().replace(regex,"")
    }
}