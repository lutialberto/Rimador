package com.example.betom.rimador.modelos

import android.util.Log
import com.example.betom.rimador.servicios.Silabeador
import com.example.betom.rimador.utilidades.NO_HAY_ERROR
import com.example.betom.rimador.utilidades.NO_SE_PUEDE_SEPARAR

class Palabra (val cadena:String) {

    var silabas: ArrayList<Silaba>
    var mensajeError: String

    init {
        silabas=ArrayList<Silaba>()
        mensajeError= NO_HAY_ERROR
        Silabeador.separarEnSilabas(this)

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
}