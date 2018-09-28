package com.example.betom.rimador.services

import android.util.Log
import com.example.betom.rimador.models.Word
import com.example.betom.rimador.models.Syllable
import com.example.betom.rimador.utilities.*

class WordSeparator {

    private lateinit var acumulated: String
    private lateinit var lastSyllable: Syllable
    private lateinit var word: Word

    fun separarEnSilabas (p:Word){
        acumulated=""
        lastSyllable=Syllable()
        this.word= p
        var state=0
        Log.d("SEP","str a separarEnSilabas -> |${word.str}|")

        val stateMatrix= changeStateMatrixInit()
        val actionMatrix= actionMatrixInit()

        val cadena="${word.str.toLowerCase().trim()}$END_OF_WORD_DELIMITER"
        Log.d("SEP","str a separarEnSilabas -> |$cadena|")

        for (letter in cadena){
            if(state!=-1) {
                val letterSet= findLetterSet(letter)
                Log.d("SEP","estado actual, grupo -> |$state|$letterSet|$letter|")
                //dado la letra y estado actual llamo a la funcion correspondiente de la matriz
                actionMatrix[LETTER_SETS_COUNT*state+letterSet](letter)
                //dado la letra y estado actual consigo el proximo estado
                state=stateMatrix[LETTER_SETS_COUNT*state+letterSet]
                Log.d("SEP","estado nuevo -> |$state|")
            }
        }
        Log.d("SEP","str separada -> |${word.intoSyllables()}|")
    }

    private fun findLetterSet(letter: Char): Int {
        return when(letter){
            'i','u','ü' -> 0  //vd: vocales debiles
            'a','e','o','á','é','í','ó','ú' -> 1  //vf: vocales fuertes
            'b','f','g','p','t' -> 2  //cig: consonantes que forman grupo con 'r' y 'l'
            'd' -> 3  //consonantes que forman grupo con 'r'
            'c' -> 4  //consonantes que forman grupo con 'l', 'r' y 'h'
            'l' -> 5  //consonantes que forman grupo con 'l' y cig
            'r' -> 6  //consonantes que forman grupo con 'r' y cig
            'h' -> 7  //consonantes que forman grupo con 'c'
            'j','k','m','n','q','s','v','w','x','y','z' -> 8 //cn: no forman grupo
            ' ' -> 9  //caracter de fin de word
            else -> 10  //error
        }
    }

    private fun changeStateMatrixInit(): Array<Int>{
        return arrayOf(
            //  0   1   2   3   4   5   6   7   8  9 10
            // vd  vf  cig  d   c   l   r   h  cn fin err
                2,  3,  1,  1,  1,  1,  1,  1,  1, -1, -1,  //0
                2,  3,  1,  1,  1,  1,  1,  1,  1, -1, -1,  //1
                2,  3,  5,  6,  7,  8,  9,  2, 10,  0, -1,  //2
                2,  3,  5,  6,  7,  8,  9,  4, 10,  0, -1,  //3
                2,  3,  5,  6,  7,  8,  9, 10, 10,  0, -1,  //4
                2,  3,  5,  6,  7, 12, 13, 10, 10,  0, -1,  //5
                2,  3,  5,  6,  7,  8, 13, 10, 10,  0, -1,  //6
                2,  3,  5,  6,  7, 12, 13, 10, 10,  0, -1,  //7
                2,  3,  5,  6,  7, 12,  9, 10, 10,  0, -1,  //8
                2,  3,  5,  6,  7,  8, 13, 10, 10,  0, -1,  //9
                2,  3,  5,  6,  7,  8,  9, 10, 10,  0, -1,  //10
                2,  3,  5,  6,  7,  8,  9, 10, 10,  0, -1,  //11
                2,  3,  5,  6,  7, 12,  9, 10, 10,  0, -1,  //12
                2,  3,  5,  6,  7,  8, 13, 10, 10,  0, -1   //13
        )
    }

    private fun actionMatrixInit(): Array<(Char) -> Unit> {
        return arrayOf(
                //estado 0
                construyendo,      //0
                construyendo,      //1
                construyendo,      //2
                construyendo,      //3
                construyendo,      //4
                construyendo,      //5
                construyendo,      //6
                construyendo,      //7
                construyendo,      //8
                inicErrorEntrada,  //9
                errorOtro,         //10
                //estado 1
                inicDeterminado,   //0
                inicDeterminado,   //1
                construyendo,      //2
                construyendo,      //3
                construyendo,      //4
                construyendo,      //5
                construyendo,      //6
                construyendo,      //7
                construyendo,      //8
                inicErrorFin,      //9
                errorOtro,         //10
                //estado 2
                construyendo,      //0
                construyendo,      //1
                vocDeterminado,    //2
                vocDeterminado,    //3
                vocDeterminado,   //4
                vocDeterminado,    //5
                vocDeterminado,    //6
                construyendo,      //7
                vocDeterminado,    //8
                vocFin,            //9
                errorOtro,         //10
                //estado 3
                construyendo,      //0
                vocHiato,          //1
                vocDeterminado,    //2
                vocDeterminado,    //3
                vocDeterminado,   //4
                vocDeterminado,    //5
                vocDeterminado,    //6
                construyendo,      //7
                vocDeterminado,    //8
                vocFin,            //9
                errorOtro,         //10
                //estado 4
                construyendo,      //0
                vocHiatoH,         //1
                vocDeterminadoH,   //2
                vocDeterminadoH,   //3
                vocDeterminadoH,   //4
                vocDeterminadoH,   //5
                vocDeterminadoH,   //6
                vocDeterminadoH,   //7
                vocDeterminadoH,   //8
                vocFin,            //9
                errorOtro,         //10
                //estado 5
                sepDeterminado,    //0
                sepDeterminado,    //1
                construyendo,      //2
                construyendo,      //3
                construyendo,      //4
                construyendo,      //5
                construyendo,      //6
                construyendo,      //7
                construyendo,      //8
                sepFin,            //9
                errorOtro,         //10
                //estado 6
                sepDeterminado,    //0
                sepDeterminado,    //1
                construyendo,      //2
                construyendo,      //3
                construyendo,      //4
                construyendo,      //5
                construyendo,      //6
                construyendo,      //7
                construyendo,      //8
                sepFin,            //9
                errorOtro,         //10
                //estado 7
                sepDeterminado,    //0
                sepDeterminado,    //1
                construyendo,      //2
                construyendo,      //3
                construyendo,      //4
                construyendo,      //5
                construyendo,      //6
                construyendo,      //7
                construyendo,      //8
                sepFin,            //9
                errorOtro,         //10
                //estado 8
                sepDeterminado,    //0
                sepDeterminado,    //1
                construyendo,      //2
                construyendo,      //3
                construyendo,      //4
                construyendo,      //5
                construyendo,      //6
                construyendo,      //7
                construyendo,      //8
                sepFin,            //9
                errorOtro,         //10
                //estado 9
                sepDeterminado,    //0
                sepDeterminado,    //1
                construyendo,      //2
                construyendo,      //3
                construyendo,      //4
                construyendo,      //5
                construyendo,      //6
                construyendo,      //7
                construyendo,      //8
                sepFin,            //9
                errorOtro,         //10
                //estado 10
                sepDeterminado,    //0
                sepDeterminado,    //1
                construyendo,      //2
                construyendo,      //3
                construyendo,      //4
                construyendo,      //5
                construyendo,      //6
                construyendo,      //7
                construyendo,      //8
                sepFin,            //9
                errorOtro,         //10
                //estado 11
                sepDeterminadoGC,  //0
                sepDeterminadoGC,  //1
                construyendo,      //2
                construyendo,      //3
                construyendo,      //4
                construyendo,      //5
                construyendo,      //6
                construyendo,      //7
                construyendo,      //8
                sepFin,            //9
                errorOtro,         //10
                //estado 12
                sepDeterminadoGC,  //0
                sepDeterminadoGC,  //1
                construyendo,      //2
                construyendo,      //3
                construyendo,      //4
                construyendo,      //5
                construyendo,      //6
                construyendo,      //7
                construyendo,      //8
                sepFin,            //9
                errorOtro,         //10
                //estado 13
                sepDeterminadoGC,  //0
                sepDeterminadoGC,  //1
                construyendo,      //2
                construyendo,      //3
                construyendo,      //4
                construyendo,      //5
                construyendo,      //6
                construyendo,      //7
                construyendo,      //8
                sepFin,            //9
                errorOtro         //10
        )
    }

    /*
    * se determino que lo que hay en acumulated es un prefijo silabico
    * guardo el acumulated como prefijo de la silaba
    * empiezo a cumular de nuevo
    * */
    private val inicDeterminado = {vocal: Char ->
        lastSyllable.syllabicPrefix= acumulated
        acumulated=vocal.toString()
    }

    /*
    * se trata analizar una word sin vocales
    * */
    private val inicErrorFin = { _: Char ->
        word.errorMessage = ERROR_WORD_WITHOUT_VOCALS
    }

    /*
    * se trata de analizar una word que tiene simbolos que no son letras en minuscula
    * */
    private val errorOtro = { unknownSymbol: Char ->
        word.errorMessage = "$ERROR_UNKNOWN_SYMBOL$unknownSymbol"
    }

    /*
    * se trata de analizar una "word" sin simbolos
    * */
    private val inicErrorEntrada = { _: Char ->
        word.errorMessage = ERROR_EMPTY_WORD
    }

    /*
    * aun no se reconocio ningun patron
    * guardo una letra nueva en el acumulated
    * */
    private val construyendo = { letter: Char ->
        acumulated+=letter
    }

    /*
    * se reconocio un hiato sin h de por medio
    * formo un grupo silabico con lo acumulated
    * guardo la ultima silaba y genero una nueva
    * empiezo a acumular de nuevo
    * */
    private val vocHiato = { vocal: Char ->
        lastSyllable.vowels= acumulated
        word.addSyllable(lastSyllable)
        lastSyllable= Syllable()
        acumulated=vocal.toString()
    }

    /*
    * se reconocio hiato con h
    * formo grupo silabico con las vocales previas a la h
    * guardo la ultima silaba y genero una nueva
    * guardo la h como prefijo
    * empiezo a acumular de nuevo
    * */
    private val vocHiatoH = { vocal: Char ->
        lastSyllable.vowels=acumulated.substring(0,acumulated.length-1)
        word.addSyllable(lastSyllable)
        lastSyllable= Syllable()
        lastSyllable.syllabicPrefix="h"
        acumulated=vocal.toString()
    }

    /*
    * se llego al final de la word
    * formo grupo silabico con lo acumulated
    * guardo la silaba
    * */
    private val vocFin = { _ : Char ->
        lastSyllable.vowels=acumulated
        word.addSyllable(lastSyllable)
    }

    /*
    * lo acumulated forma grupo vocal
    * formo grupo silabico, guardo silaba
    * empiezo a acumular de nuevo
    * */
    private val vocDeterminado = { consonant: Char ->
        lastSyllable.vowels= acumulated
        acumulated=consonant.toString()
    }

    /*
    * se reconocio grupo vocal seguido de h
    * formo grupo silabico con todo lo acumulated previo a la h
    * empiezo a acumular e nuevo, pero agregando primero la h
    * */
    private val vocDeterminadoH = {consonant : Char ->
        lastSyllable.vowels= acumulated.substring(0, acumulated.length-1)
        acumulated= "h$consonant"
    }

    /*
    * se reconocio que lo acumulated pertenece a 2 syllables distintas
    * si lo acumulated es de tamaño 1, se guarda la silaba actual, y se genera una nueva
    * con el acumulated como prefijo
    * si es mayor a 1, se guarda en la silaba actual como sufijo todo el acumulated
    * menos la ultima letra que sera prefijo de la nueva silaba
    * */
    private val sepDeterminado = { vocal : Char ->
        val prefix: String
        if(acumulated.length>1){
            prefix=acumulated.substring(acumulated.length-1)
            lastSyllable.syllabicSufix=acumulated.substring(0,acumulated.length-1)
        } else {
            prefix=acumulated
        }
        word.addSyllable(lastSyllable)
        lastSyllable=Syllable()
        lastSyllable.syllabicPrefix=prefix
        acumulated=vocal.toString()
    }

    /*
    * se reconocio que lo acumulated pertenece a 2 syllables distintas y hay grupo consonantico
    * las 2 ultimas letras del acumulated son prefijo de la nueva silaba y el resto
    * son sufijo de la actual
    * */
    private val sepDeterminadoGC = { vocal: Char ->
        val prefix=acumulated.substring(acumulated.length-2)
        lastSyllable.syllabicSufix=acumulated.substring(0,acumulated.length-2)
        word.addSyllable(lastSyllable)
        lastSyllable=Syllable()
        lastSyllable.syllabicPrefix=prefix
        acumulated=vocal.toString()
    }

    /*
    * se llego al final de la word
    * todo lo acumulated se guarda como sufijo de la silaba actual
    * */
    private val sepFin = { _ : Char ->
        lastSyllable.syllabicSufix=acumulated
        word.addSyllable(lastSyllable)
    }
}