package com.example.betom.rimador.services

class StringCodifier() {

    fun getDBText(str:String):String{
        var aux=""
        str.forEach { element ->
            aux+=when(element){
                'á' -> "a\\"
                'é' -> "e\\"
                'í' -> "i\\"
                'ó' -> "o\\"
                'ú' -> "u\\"
                'ñ' -> "n~"
                else -> element
            }
        }
        return aux
    }

    fun getAppText(str:String):String{
        var aux=""
        for (i in 0 until str.length)
            if(str[i]=='\\' || str[i]=='~') {
                aux = aux.substring(0, aux.length - 1) +
                        when (str[i - 1]) {
                            'a' -> 'á'
                            'e' -> 'é'
                            'i' -> 'í'
                            'o' -> 'ó'
                            'u' -> 'ú'
                            'n' -> 'ñ'
                            '\\' -> '\u0000'
                            else -> str[i]
                        }
            } else {
                aux+=str[i]
            }
        return aux
    }
}