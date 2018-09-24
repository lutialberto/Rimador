package com.example.betom.rimador.modelos

import com.example.betom.rimador.servicios.Silabeador
import com.example.betom.rimador.utilidades.*

class Palabra (val cadena:String) {

    private var silabas: ArrayList<Silaba>
    var mensajeError: String
    private var silabaTonica: Int

    init {
        silabas=ArrayList<Silaba>()
        mensajeError= NO_HAY_ERROR
        Silabeador.separarEnSilabas(this)
        silabaTonica=VALOR_POR_DEFECTO_SILABA_TONICA
        silabaTonica=getSilabaTonica()

    }

    fun agregarSilaba(silaba: Silaba){
        silabas.add(silaba)
    }

    override fun toString(): String {
        var retorno=""
        for (silaba in silabas){
            retorno+=silaba.toString()
        }
        return retorno
    }

    fun separadaEnSilabas(): String{
        if(!tieneErrores()) {
            var retorno = ""
            for (silaba in silabas) {
                retorno += silaba.toString() + "-"
            }
            return retorno.substring(0, retorno.length - 1)
        }
        return NO_SE_PUEDE_SEPARAR
    }

    fun tieneErrores():Boolean{
        return mensajeError!= NO_HAY_ERROR
    }

    //---------------------------------------------------------------------------

    fun getEstructuraVocal(){

    }

    /*
    * busco en todas las silabas si en su grupo vocal hay alguna con tilde
    * y chequeo que solo puede haber una tilde por palabra
    * */
    private fun getPosicionSilabaConTilde():Int {
        var posicionTilde= PALABRA_SIN_TILDE
        for ((i, silaba) in silabas.withIndex()) {
            for (vocal in silaba.grupoVocal) {
                if (vocal == 'á' || vocal == 'é' || vocal == 'í' || vocal == 'ó' || vocal == 'ú')
                    if (posicionTilde == PALABRA_SIN_TILDE)
                        posicionTilde = i
                    else {
                        mensajeError = ERROR_VARIAS_VOCALES_ACENTUADAS
                        return -1
                    }
            }
        }
        return posicionTilde
    }

    /*
    * busco su silaba con tilde, si no tiene entonces determino si es aguda o grave
    * */
    fun getSilabaTonica():Int {
        if(!tieneErrores()) {
            if(silabaTonica== VALOR_POR_DEFECTO_SILABA_TONICA) {
                val posSilabaTonica = getPosicionSilabaConTilde()

                return if (posSilabaTonica == PALABRA_SIN_TILDE) {
                    val maxSilabas = getCantidadSilabas()
                    when (esPalabraAgudaSinTilde()) {
                        true -> maxSilabas - 1 //es aguda
                        false -> maxSilabas - 2 //es grave
                    //no puede ser esdrujula porque no lleva tilde
                    }
                } else {
                    posSilabaTonica
                }
            } else {
                return silabaTonica
            }
        } else {
            return -1
        }
    }

    /*
    * las palabras agudas llevan tilde si terminan en 'n', 's' o vocal
    * */
    private fun esPalabraAgudaSinTilde(): Boolean {
        val ultimaLetra=toString().last()
        return when(ultimaLetra){
            'a','e','i','o','u','s','n' -> false
            else -> true
        }
    }

    fun getParteRimante(){
    }

    fun getCantidadSilabas(): Int {
        return silabas.size
    }
}