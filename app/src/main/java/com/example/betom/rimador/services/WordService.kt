package com.example.betom.rimador.services

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.betom.rimador.models.Word
import com.example.betom.rimador.utilities.URL_FIND_WORD_CONSONANT_RHYME
import com.example.betom.rimador.utilities.URL_WORDS
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

object WordService {

    val words=ArrayList<Word>()

    fun addWord(context:Context,word:Word,complete: (Boolean) -> Unit){
        val jsonBody=JSONObject()

        jsonBody.put("chain",word.str)
        jsonBody.put("assonantRhyme",word.getRhyme(false))
        jsonBody.put("consonantRhyme",word.getRhyme(true))
        jsonBody.put("firstSyllable",word.syllables.first())
        jsonBody.put("lastSyllable",word.syllables.last())
        jsonBody.put("vocalSkeleton",word.getAssonatingStructure())

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

    fun getConsonantRhymeWords(context: Context,sufix:String,complete: (Boolean) -> Unit){

        val findWordsRequest= object : JsonArrayRequest(Method.GET, "${URL_FIND_WORD_CONSONANT_RHYME}$sufix",
                null,Response.Listener {response ->
            try {
                words.clear()
                for (i in 0 until response.length()){
                    val word=response.getJSONObject(i)
                    val newWord=Word(word.getString("chain"))
                    words.add(newWord)
                }
                complete(true)
            } catch (e:JSONException) {
                Log.d("JSON","EXC: ${e.localizedMessage}")
            }
        }, Response.ErrorListener {error ->
            Log.d("ERROR","Could not find words with consonant rhyme: $error")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }
        }
        Volley.newRequestQueue(context).add(findWordsRequest)
    }
}