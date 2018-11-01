package com.example.betom.rimador.wordHandlers

import com.example.betom.rimador.models.Syllable
import com.example.betom.rimador.utilities.DEFAULT_MAX_SYLLABLES
import com.example.betom.rimador.utilities.DEFAULT_MIN_SYLLABLES
import com.example.betom.rimador.utilities.NUMBER_OF_WORDS_TO_BE_CREATED
import java.util.*
import kotlin.collections.ArrayList

class WordCreator {

    private var maxSyllables:Int = DEFAULT_MAX_SYLLABLES
    private var minSyllables:Int = DEFAULT_MIN_SYLLABLES

    /*
    * get a random integer number between 2 other numbers
    * */
    private fun IntRange.random() =
            Random().nextInt((endInclusive + 1) - start) +  start

    /*
    * 1. gets exactly a list of n words
    * 2. for each word -> determine the number of syllables at random between min and max syllables
    * 3. make each syllable individually
    * 4. finish the word and start with the next one
    * */
    fun getWords():ArrayList<String>{
        val words=ArrayList<String>()

        for (i in 0 until NUMBER_OF_WORDS_TO_BE_CREATED){
            //definir cant silabas
            //loopear generando una silaba a la ves
            val syllablesLength=(minSyllables..maxSyllables).random()

            val syllables=ArrayList<Syllable>()
            for(j in 0 until syllablesLength){
                val lastSyllable=if(syllables.size==0) Syllable() else syllables.last()
                val newSyllable=makeNewSyllable(lastSyllable)
                syllables.add(newSyllable)
            }
            words.add(intoString(syllables))
        }

        return words
    }

    private fun makeNewSyllable(lastSyllable: Syllable): Syllable {
        if(lastSyllable.toString().isEmpty()){
            //es 1ra silaba
        }else{
            //tengo que usar la anterior
        }
        return Syllable()
    }

    /*
    * the toString method for syllables array list
    * */
    private fun intoString(elements:ArrayList<Syllable>):String {
            var str = ""
            for (e in elements) {
                str += e.toString()
            }
            return str
    }
}