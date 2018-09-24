package com.example.betom.rimador.servicios

import android.util.Log
import com.example.betom.rimador.modelos.Palabra
import com.example.betom.rimador.modelos.Silaba
import com.example.betom.rimador.utilidades.*

object Silabeador {

    private lateinit var acumulado: String
    private lateinit var ultimaSilaba: Silaba
    private lateinit var palabra: Palabra

    fun separarEnSilabas (p:Palabra){
        acumulado=""
        ultimaSilaba=Silaba()
        this.palabra= p
        var estadoActual=0
        Log.d("SEP","cadena a separarEnSilabas -> |${palabra.cadena}|")

        val matrizEstados= inicializarMatrizCambioEstados()
        val matrixAcciones= inicializarMatrizAcciones()

        val cadena="${palabra.cadena.toLowerCase().trim()}$CARACTER_FIN_PALABRA"
        Log.d("SEP","cadena a separarEnSilabas -> |$cadena|")

        for (letra in cadena){
            if(estadoActual!=-1) {
                val nroGrupoLetras= encontrarNroGrupoLetras(letra)
                Log.d("SEP","estado actual, grupo -> |$estadoActual|$nroGrupoLetras|$letra|")
                //dado la letra y estado actual llamo a la funcion correspondiente de la matriz
                matrixAcciones[MAX_GRUPOS*estadoActual+nroGrupoLetras](letra)
                //dado la letra y estado actual consigo el proximo estado
                estadoActual=matrizEstados[MAX_GRUPOS*estadoActual+nroGrupoLetras]
                Log.d("SEP","estado nuevo -> |$estadoActual|")
            }
        }
        Log.d("SEP","cadena separada -> |${palabra.separadaEnSilabas()}|")
    }

    private fun encontrarNroGrupoLetras(letra: Char): Int {
        return when(letra){
            'i','u','ü' -> 0  //vd: vocales debiles
            'a','e','o','á','é','í','ó','ú' -> 1  //vf: vocales fuertes
            'b','f','g','p','t' -> 2  //cig: consonantes que forman grupo con 'r' y 'l'
            'd' -> 3  //consonantes que forman grupo con 'r'
            'c' -> 4  //consonantes que forman grupo con 'l', 'r' y 'h'
            'l' -> 5  //consonantes que forman grupo con 'l' y cig
            'r' -> 6  //consonantes que forman grupo con 'r' y cig
            'h' -> 7  //consonantes que forman grupo con 'c'
            'j','k','m','n','q','s','v','w','x','y','z' -> 8 //cn: no forman grupo
            ' ' -> 9  //caracter de fin de palabra
            else -> 10  //error
        }
    }

    private fun inicializarMatrizCambioEstados(): Array<Int>{
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

    private fun inicializarMatrizAcciones(): Array<(Char) -> Unit> {
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
    * se determino que lo que hay en acumulado es un prefijo silabico
    * guardo el acumulado como prefijo de la silaba
    * empiezo a cumular de nuevo
    * */
    private val inicDeterminado = {vocal: Char ->
        ultimaSilaba.prefijoSilabico= acumulado
        acumulado=vocal.toString()
    }

    /*
    * se trata analizar una palabra sin vocales
    * */
    private val inicErrorFin = { _: Char ->
        palabra.mensajeError = ERROR_PALABRA_SIN_VOCALES
    }

    /*
    * se trata de analizar una palabra que tiene simbolos que no son letras en minuscula
    * */
    private val errorOtro = { otro: Char ->
        palabra.mensajeError = "$ERROR_PALABRA_SIMBOLOS_NO_PERMITIDOS$otro"
    }

    /*
    * se trata de analizar una "palabra" sin simbolos
    * */
    private val inicErrorEntrada = { _: Char ->
        palabra.mensajeError = ERROR_PALABRA_VACIA
    }

    /*
    * aun no se reconocio ningun patron
    * guardo una letra nueva en el acumulado
    * */
    private val construyendo = { letra: Char ->
        acumulado+=letra
    }

    /*
    * se reconocio un hiato sin h de por medio
    * formo un grupo silabico con lo acumulado
    * guardo la ultima silaba y genero una nueva
    * empiezo a acumular de nuevo
    * */
    private val vocHiato = { vocal: Char ->
        ultimaSilaba.grupoVocal= acumulado
        palabra.agregarSilaba(ultimaSilaba)
        ultimaSilaba= Silaba()
        acumulado=vocal.toString()
    }

    /*
    * se reconocio hiato con h
    * formo grupo silabico con las vocales previas a la h
    * guardo la ultima silaba y genero una nueva
    * guardo la h como prefijo
    * empiezo a acumular de nuevo
    * */
    private val vocHiatoH = { vocal: Char ->
        ultimaSilaba.grupoVocal=acumulado.substring(0,acumulado.length-1)
        palabra.agregarSilaba(ultimaSilaba)
        ultimaSilaba= Silaba()
        ultimaSilaba.prefijoSilabico="h"
        acumulado=vocal.toString()
    }

    /*
    * se llego al final de la palabra
    * formo grupo silabico con lo acumulado
    * guardo la silaba
    * */
    private val vocFin = { _ : Char ->
        ultimaSilaba.grupoVocal=acumulado
        palabra.agregarSilaba(ultimaSilaba)
    }

    /*
    * lo acumulado forma grupo vocal
    * formo grupo silabico, guardo silaba
    * empiezo a acumular de nuevo
    * */
    private val vocDeterminado = { consonante: Char ->
        ultimaSilaba.grupoVocal= acumulado
        acumulado=consonante.toString()
    }

    /*
    * se reconocio grupo vocal seguido de h
    * formo grupo silabico con todo lo acumulado previo a la h
    * empiezo a acumular e nuevo, pero agregando primero la h
    * */
    private val vocDeterminadoH = {consonante : Char ->
        ultimaSilaba.grupoVocal= acumulado.substring(0, acumulado.length-1)
        acumulado= "h$consonante"
    }

    /*
    * se reconocio que lo acumulado pertenece a 2 silabas distintas
    * si lo acumulado es de tamaño 1, se guarda la silaba actual, y se genera una nueva
    * con el acumulado como prefijo
    * si es mayor a 1, se guarda en la silaba actual como sufijo todo el acumulado
    * menos la ultima letra que sera prefijo de la nueva silaba
    * */
    private val sepDeterminado = { vocal : Char ->
        val prefijo: String
        if(acumulado.length>1){
            prefijo=acumulado.substring(acumulado.length-1)
            ultimaSilaba.sufijoSilabico=acumulado.substring(0,acumulado.length-1)
        } else {
            prefijo=acumulado
        }
        palabra.agregarSilaba(ultimaSilaba)
        ultimaSilaba=Silaba()
        ultimaSilaba.prefijoSilabico=prefijo
        acumulado=vocal.toString()
    }

    /*
    * se reconocio que lo acumulado pertenece a 2 silabas distintas y hay grupo consonantico
    * las 2 ultimas letras del acumulado son prefijo de la nueva silaba y el resto
    * son sufijo de la actual
    * */
    private val sepDeterminadoGC = { vocal: Char ->
        val prefijo=acumulado.substring(acumulado.length-2)
        ultimaSilaba.sufijoSilabico=acumulado.substring(0,acumulado.length-2)
        palabra.agregarSilaba(ultimaSilaba)
        ultimaSilaba=Silaba()
        ultimaSilaba.prefijoSilabico=prefijo
        acumulado=vocal.toString()
    }

    /*
    * se llego al final de la palabra
    * todo lo acumulado se guarda como sufijo de la silaba actual
    * */
    private val sepFin = { _ : Char ->
        ultimaSilaba.sufijoSilabico=acumulado
        palabra.agregarSilaba(ultimaSilaba)
    }
}