package com.example.betom.rimador.modelos

import com.example.betom.rimador.utilidades.SILABA_NADA_ULTIMO
import com.example.betom.rimador.utilidades.SILABA_PREF_ULTIMO
import com.example.betom.rimador.utilidades.SILABA_SUF_ULTIMO
import com.example.betom.rimador.utilidades.SILABA_VOC_ULTIMO

class Silaba {
    var prefijoSilabico=""
    var grupoVocal=""
    var sufijoSilabico=""

    fun getUltimoElemento(): Int {
        return if (sufijoSilabico!="") SILABA_SUF_ULTIMO
            else if (grupoVocal!="") SILABA_VOC_ULTIMO
            else if (prefijoSilabico != "") SILABA_PREF_ULTIMO
            else SILABA_NADA_ULTIMO
    }

    override fun toString(): String {
        var retorno= if(prefijoSilabico=="") grupoVocal else prefijoSilabico+grupoVocal
        retorno+= if(sufijoSilabico=="") "" else sufijoSilabico
        return retorno
    }
}