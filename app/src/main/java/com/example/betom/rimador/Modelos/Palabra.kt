package com.example.betom.rimador.Modelos

class Palabra {
    var silabas=ArrayList<Silaba>()

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
}