package com.example.betom.rimador.services

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.betom.rimador.models.Word
import com.example.betom.rimador.utilities.URL_WORDS
import com.example.betom.rimador.wordHandlers.StringCodifier
import org.json.JSONException
import org.json.JSONObject

object WordService {

    val words=ArrayList<Word>()

    /*
    * 1. build a json body using the information of the given word
    * 2. replace the word's special characters to new ones that the DB can interpret
    * 3. make the POST request to add a new word to the DB
    * 4. send the request
    * */
    fun addWord(context:Context,word:Word,complete: (Boolean) -> Unit){
        val jsonBody=JSONObject()

        jsonBody.put("chain",word.toString())
        jsonBody.put("assonantRhyme", word.intoString(true,word.getRhyme(false)))
        jsonBody.put("consonantRhyme", word.intoString(false,word.getRhyme(true)))
        jsonBody.put("firstLetter",word.getFirstLetter())
        jsonBody.put("vocalSkeleton", word.intoString(true,word.getAssonatingStructure()))

        val codifier= StringCodifier()
        val requestBody=codifier.getDBText(jsonBody.toString())

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

    /*
    * 1. make the GET request to find words given a especifict url, currently find words with
    * a specific assonant or consonant rhyme
    * 2. success -> clear the respective data containers, extract the chain field (Word) and save any elements found
    * 3. else -> log errors
    * 4. send the request
    * */
    fun findWords(context: Context, url:String, complete: (Boolean) -> Unit){

        val findWordsRequest= object : JsonArrayRequest(Method.GET, url,
                null,Response.Listener {response ->
            try {
                words.clear()
                val codifier= StringCodifier()
                for (i in 0 until response.length()){
                    val word=response.getJSONObject(i)
                    val newWord=Word(codifier.getAppText(word.getString("chain")))
                    words.add(newWord)
                }
                complete(true)
            } catch (e:JSONException) {
                Log.d("JSON","EXC: ${e.localizedMessage}")
            }
        }, Response.ErrorListener {error ->
            Log.d("ERROR","Could not find words : $error")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }
        }
        Volley.newRequestQueue(context).add(findWordsRequest)
    }
}