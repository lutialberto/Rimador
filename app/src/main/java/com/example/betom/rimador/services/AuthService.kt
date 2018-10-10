package com.example.betom.rimador.services

import android.content.Context
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.betom.rimador.models.Word
import com.example.betom.rimador.utilities.URL_WORDS
import org.json.JSONObject

object AuthService {
    fun addWord(context:Context,word:Word,complete: (Boolean) -> Unit){
        val jsonBody=JSONObject()

        jsonBody.put("chain",word.str)
        jsonBody.put("assonantRhyme",word.getRhyme(false))
        jsonBody.put("consonantRhyme",word.getRhyme(true))
        jsonBody.put("firstSyllable",word.syllables.first())
        jsonBody.put("lastSyllable",word.syllables.last())
        jsonBody.put("vocalSkeleton",word.getAssonatingStructure().toString())

        val requestBody=jsonBody.toString()

        val registerRequest= object : StringRequest(Method.POST, URL_WORDS,Response.Listener {_ ->
            complete(true)
        },Response.ErrorListener {_ ->
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }
        Volley.newRequestQueue(context).add(registerRequest)
    }
}