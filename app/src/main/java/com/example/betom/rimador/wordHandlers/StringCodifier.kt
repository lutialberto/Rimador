package com.example.betom.rimador.wordHandlers

class StringCodifier {

    /*
    * transform special charaters into new ones that can be imterpreted for the DB
    * */
    fun getDBText(str:String):String{
        var aux=""
        str.forEach { element ->
            aux+=when(element){
                'á' -> "a\\\\"
                'é' -> "e\\\\"
                'í' -> "i\\\\"
                'ó' -> "o\\\\"
                'ú' -> "u\\\\"
                'ñ' -> "n~"
                else -> element
            }
        }
        return aux
    }

    /*
    * transform exclusive DB expressions into special character of the user's own language
    * */
    fun getAppText(str:String):String{
        //  a\\baco
        var aux=""
        if(str.length>=2) {
            for (i in 0 until str.length)
                if (str[i] == '\\' || str[i] == '~') {
                    aux = aux.substring(0, aux.length - 1) +
                            when (str[i - 1]) {
                                'a' -> 'á'
                                'e' -> 'é'
                                'i' -> 'í'
                                'o' -> 'ó'
                                'u' -> 'ú'
                                'n' -> 'ñ'
                                '\\' -> aux.last()
                                else -> str[i - 1]
                            }
                } else {
                    aux += str[i]
                }
        }
        else
            aux=str
        return aux
    }
}