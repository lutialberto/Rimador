package com.example.betom.rimador.utilities

/*informacion sobre la maquina de estados para separarEnSilabas en syllables*/
const val LETTER_SETS_COUNT = 11
const val END_OF_WORD_DELIMITER = " "

/*informacion sobre constantes de la clase palabra*/
const val NO_ERRORS = ""
const val CANT_BE_SEPARATED = ""

/*mensajes separando en syllables*/
const val ERROR_WORD_WITHOUT_VOCALS = "No existen palabras sin vocales"
const val ERROR_UNKNOWN_SYMBOL = "Word con simbolos no permitidos -> "
const val ERROR_EMPTY_WORD = "Word vacia"
const val DIVIDE_SYLLABLES_SYMBOL = "-"

/*mensajes syllables tonica o con tilde*/
const val ERROR_TO_MANY_ACCENT_MARKS = "No puede haber mas de una vocal con tilde por palabra"
const val WORD_WITHOUT_ACCENT_MARK = -1
const val TONIC_SYLLABLE_DEFAULT_VALUE = -2
