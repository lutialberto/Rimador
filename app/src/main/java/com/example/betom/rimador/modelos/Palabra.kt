package com.example.betom.rimador.modelos

import com.example.betom.rimador.utilidades.NO_HAY_ERROR

class Palabra {
    var silabas=ArrayList<Silaba>()
    var mensajeError= NO_HAY_ERROR

    fun agregarSilaba(silaba: Silaba){
        silabas.add(silaba)
    }

    override fun toString(): String {
        var retorno=""
        for (silaba in silabas){
            retorno+=silaba.toString()+"-"
        }
        return retorno.substring(0,retorno.length-1)
    }

    fun sePuedeSeparar():Boolean{
        return mensajeError== NO_HAY_ERROR
    }
}