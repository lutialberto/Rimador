package com.example.betom.rimador.models


class Syllable {
    var syllabicPrefix=""
    var vowels=""
    var syllabicSufix=""

    override fun toString(): String {
        return syllabicPrefix+vowels+syllabicSufix
    }
}