package com.example.betom.rimador.utilidades


/*informacion sobre la estructura actual de la ultima silaba determinada de una palabra*/
const val SILABA_SUF_ULTIMO = 3
const val SILABA_VOC_ULTIMO = 2
const val SILABA_PREF_ULTIMO = 1
const val SILABA_NADA_ULTIMO = 0

/*informacion sobre la maquina de estados para separarEnSilabas en silabas*/
const val MAX_GRUPOS = 11
const val CARACTER_FIN_PALABRA = " "

/*informacion sobre constantes de la clase palabra*/
const val NO_HAY_ERROR = ""
const val NO_SE_PUEDE_SEPARAR = ""

/*mensajes de error separando en silabas*/
const val ERROR_PALABRA_SIN_VOCALES = "No existen palabras sin vocales"
const val ERROR_PALABRA_SIMBOLOS_NO_PERMITIDOS = "Palabra con simbolos no permitidos -> "
const val ERROR_PALABRA_VACIA = "Palabra vacia"

/*mensajes de error buscando silaba tonica*/
const val ERROR_VARIAS_VOCALES_ACENTUADAS = "No puede haber mas de una vocal acentuada por palabra"