package com.example.betom.rimador.models

import com.example.betom.rimador.wordHandlers.WordSeparator
import com.example.betom.rimador.utilities.*

class Word (str:String) {

    val str: String = str.trim()
    var syllables=ArrayList<Syllable>()
    var errorMessage: String
    private var tonicSyllablePosition: Int

    init {
        errorMessage= NO_ERRORS
        tonicSyllablePosition=TONIC_SYLLABLE_DEFAULT_VALUE
        if(correctSize()) {
            val wordSeparator = WordSeparator()
            wordSeparator.separarEnSilabas(this)
            tonicSyllablePosition=getTonicSyllable()
        }
        else
            errorMessage= ERROR_WORD_SIZE_ABOVE_LIMIT
    }

    private fun correctSize():Boolean{
        return str.length<= MAX_WORD_SIZE
    }

    fun addSyllable(syllable: Syllable){
        syllables.add(syllable)
    }

    override fun toString(): String {
        return str
    }

    /*
    * preguntar siempre con el metodo tiene errores antes de llamar este metodo
    * para evitar resultados inesperados
    *
    * separa las syllables con '-': salida -> sa-li-da
    * */
    fun intoSyllables(): List<String>{
        val separatedWord=ArrayList<String>()
        if(!hasErrors()) {
            for (syllable in syllables) {
                separatedWord.add(syllable.toString())
            }
        }
        return separatedWord
    }

    fun hasErrors():Boolean{
        return errorMessage!= NO_ERRORS
    }

    //---------------------------------------------------------------------------

    /*
    * preguntar siempre con el metodo tiene errores antes de llamar este metodo
    * para evitar resultados inesperados
    *
    * busca el esqueleto de la palabra marcando la vocal tonica:
    * caso1: salida -> a-'i'-a
    * caso2: siquiera -> i-'ie'-a
    * caso3: ahora -> a-'o'-a
    * caso4: güeva -> 'ue'-a
    * caso5: estación -> e-a-'ió'
    * */
    fun getAssonatingStructure():ArrayList<String> {
        val structure = ArrayList<String>()
        if(!hasErrors()) {
            for ( syllable in syllables) {

                var vowels = syllable.vowels

                /*arregla caso 2*/
                if (vowels.length >= 2 && syllable.syllabicPrefix.isNotEmpty() && (syllable.syllabicPrefix.last() == 'q' || syllable.syllabicPrefix.last() == 'g'))
                    vowels = when (vowels.subSequence(0, 2)) {
                        "ué", "uí", "ue", "ui" -> vowels.subSequence(1, vowels.length).toString()
                        else -> vowels
                    }

                /*arregla caso 4*/
                vowels.replace('ü', 'u') //me saco la ü de encima

                structure.add(vowels)
            }
        }
        return structure
    }

    fun getFirstLetter():String{
        val l=toString().first().toString()
        return when(l){
                "á" -> "a"
                "é" -> "e"
                "í" -> "i"
                "ó" -> "o"
                "ú" -> "u"
                else -> l
        }
    }

    /*
    * busco en todas las syllables si en su grupo vocal hay alguna con tilde
    * y chequeo que solo puede haber una tilde por palabra
    * */
    private fun getAccentMarkSyllablePosition():Int {
        var position= WORD_WITHOUT_ACCENT_MARK
        for ((i, syllable) in syllables.withIndex()) {
            for (vocal in syllable.vowels) {
                if (vocal == 'á' || vocal == 'é' || vocal == 'í' || vocal == 'ó' || vocal == 'ú')
                    if (position == WORD_WITHOUT_ACCENT_MARK)
                        position = i
                    else {
                        errorMessage = ERROR_TO_MANY_ACCENT_MARKS
                        return -1
                    }
            }
        }
        return position
    }

    /*
    * preguntar siempre con el metodo tiene errores antes de llamar este metodo
    * para evitar resultados inesperados
    *
    * busco su silaba con tilde, si no tiene entonces determino si es aguda o grave
    * */
    private fun getTonicSyllable():Int {
        if(!hasErrors()) {
            if(tonicSyllablePosition== TONIC_SYLLABLE_DEFAULT_VALUE) {
                val tonicSyllablePosition = getAccentMarkSyllablePosition()

                return if (tonicSyllablePosition == WORD_WITHOUT_ACCENT_MARK) {
                    val maxSyllables = getSyllablesSize()
                    if(maxSyllables==1)
                        0 //es monosilabo
                    else
                        when (accentMarkInLastPosition()) {
                            true -> maxSyllables - 1 //es aguda
                            false -> maxSyllables - 2 //es grave
                        //no puede ser esdrujula porque no lleva tilde
                        }
                } else {
                    tonicSyllablePosition
                }
            } else {
                return tonicSyllablePosition
            }
        } else {
            return -1
        }
    }

    /*
    * las palabras agudas llevan tilde si terminan en 'n', 's' o vocal
    * */
    private fun accentMarkInLastPosition(): Boolean {
        val lastLetter=toString().last()
        return when(lastLetter){
            'a','e','i','o','u','s','n' -> false
            else -> true
        }
    }

    /*
    * preguntar siempre con el metodo tiene errores antes de llamar este metodo
    * para evitar resultados inesperados
    *
    * si la rima consonante obtengo el grupo vocal y sufijo de la silaba tonica y las syllables
    * posteriores
    *
    * si la rima es asonante obtengo la estructura vocal desde la silaba tonica
    *
    * para ambos casos tengo que eliminar ciertos caracteres para obtener un string
    * mas limpio: '[', ']', ' ', ','
    * ánimo: consonante -> ánimo      asonante -> á-i-o
    * */
    fun getRhyme(consonantRhyme:Boolean): ArrayList<String> {
        val rhyme=ArrayList<String>()
        if(!hasErrors()) {
            val tonicSyllable = syllables[tonicSyllablePosition]
            val rhymeSyllables = syllables.subList(tonicSyllablePosition + 1, syllables.size)

            if (consonantRhyme) {
                rhyme.add(tonicSyllable.vowels.last() + tonicSyllable.syllabicSufix)
                rhymeSyllables.forEach { syllable ->
                    rhyme.add(syllable.toString())
                }
            } else {
                rhyme.add((tonicSyllable.vowels.last().toString()))
                rhymeSyllables.forEach { syllable ->
                    rhyme.add(syllable.vowels)
                }
            }
        }
        return rhyme
    }

    private fun getSyllablesSize(): Int {
        return syllables.size
    }

    fun intoString(separateElements:Boolean, elements:ArrayList<String>):String {
        if(!hasErrors()) {
            var s = ""
            val sep = if (separateElements) "-" else ""
            for (e in elements) {
                s += e + sep
            }
            return if (separateElements) s.substring(0, s.length - 1) else s
        }else
            return ""
    }
}