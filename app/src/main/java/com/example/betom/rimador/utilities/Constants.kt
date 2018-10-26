package com.example.betom.rimador.utilities

/*informacion sobre la maquina de estados para separar en silabas */
const val LETTER_SETS_COUNT = 11
const val END_OF_WORD_DELIMITER = " "

/*informacion sobre constantes de la clase palabra*/
const val NO_ERRORS = ""
const val MAX_WORD_SIZE = 20
const val ERROR_WORD_SIZE_ABOVE_LIMIT = "La palabra no puede superar las $MAX_WORD_SIZE letras"

/*mensajes separando en silabas*/
const val ERROR_WORD_WITHOUT_VOCALS = "No existen palabras sin vocales"
const val ERROR_UNKNOWN_SYMBOL = "Palabra con simbolos no permitidos -> "
const val ERROR_EMPTY_WORD = "Palabra vacia"

/*mensajes con silaba tonica o con tilde*/
const val ERROR_TO_MANY_ACCENT_MARKS = "No puede haber mas de una vocal con tilde por palabra"
const val WORD_WITHOUT_ACCENT_MARK = -1
const val TONIC_SYLLABLE_DEFAULT_VALUE = -2

/*mensajes para la interfaz*/
const val WAITING_FOR_INPUT = "Esperando por una palabra"

/*url para las request de la bd*/
const val BASE_URL = "https://rimelappprueva.herokuapp.com/"
const val URL_WORDS= "${BASE_URL}words/"
const val URL_FIND_WORD_ASSONANT_RHYME = "${URL_WORDS}assonantRhyme/"
const val URL_FIND_WORD_CONSONANT_RHYME = "${URL_WORDS}consonantRhyme/"
const val URL_FIND_WORD_VOCAL_SKELETON = "${URL_WORDS}vocalSkeleton/"
const val URL_ADD_WORDS= "${BASE_URL}allWords"


