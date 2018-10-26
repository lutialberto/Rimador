package com.example.betom.rimador.word_handlers

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.betom.rimador.models.Word

class InputWordCorrector {
    public fun validateWord(context: Context, word:Word):Boolean {
        return if(!word.hasErrors())
                true
            else{
                Toast.makeText(context,word.errorMessage, Toast.LENGTH_SHORT).show()
                Log.d("ERROR","Error: ${word.errorMessage}.")
                false
            }
    }
}
