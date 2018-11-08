package com.example.betom.rimador.wordHandlers

import com.example.betom.rimador.models.CreatedWord
import com.example.betom.rimador.models.Syllable
import com.example.betom.rimador.utilities.*
import java.util.*
import kotlin.collections.ArrayList

class WordCreator {

    private var maxSyllables:Int = DEFAULT_MAX_SYLLABLES+1
    var minSyllables:Int = DEFAULT_MIN_SYLLABLES
    private val consonantGroups= setupConsonantGroups()
    private val vocalGroups= setupVocalGroups()

    fun setMaxSyllables(n:Int){
        maxSyllables=n+1
    }

    /*
    * get a random integer number between 2 other numbers
    * */
    private fun IntRange.random() =
            Random().nextInt(endInclusive - start) +  start

    /*
    * 1. gets exactly a list of n words
    * 2. for each word -> determine the number of syllables at random between min and max syllables
    * 3. make each syllable individually
    * 4. finish the word and start with the next one
    * */
    fun getWords():ArrayList<CreatedWord>{
        val words=ArrayList<CreatedWord>()

        for (i in 0 until NUMBER_OF_WORDS_TO_BE_CREATED){
            val syllablesLength=(minSyllables..maxSyllables).random()

            val hiddenSyllables=ArrayList<Syllable>()
            val syllables=ArrayList<Syllable>()
            for(j in 0 until syllablesLength){
                val lastSyllable=if(syllables.size==0) Syllable() else syllables.last()
                val newHiddenSyllable=makeNewHiddenSyllable(lastSyllable)
                val newSyllable= makeNewSyllable(newHiddenSyllable)
                        hiddenSyllables.add(newHiddenSyllable)
                syllables.add(newSyllable)
            }
            val createdWord=CreatedWord(intoString(syllables))
            createdWord.addHiddenSyllables(hiddenSyllables)
            words.add(createdWord)
        }
        return words.distinctBy { it.toString() } as ArrayList<CreatedWord>
    }

    private fun makeNewSyllable(newHiddenSyllable: Syllable): Syllable {
        val syllable=Syllable()
        //pick a vocal member group
        var vocalGroupChosenMembers=""
        for (name in newHiddenSyllable.vowels)
            vocalGroupChosenMembers+=getRandomMemberFrom(vocalGroups,name.toString()){ _,_ ->
                true
            }

        //pick a member of the prefix group
        val consonantPrefixGroupChosenMember=getRandomMemberFrom(consonantGroups,newHiddenSyllable.syllabicPrefix){ element, group ->
            isValidPrefix(element,group,newHiddenSyllable.vowels.first().toString())
        }

        //pick a member of the sufix group
        val consonantSufixGroupChosenMember=getRandomMemberFrom(consonantGroups,newHiddenSyllable.syllabicSufix){ _, _ ->
            true
        }

        vocalGroupChosenMembers=fixVocalGroup(consonantPrefixGroupChosenMember,newHiddenSyllable.vowels.first().toString(),newHiddenSyllable.syllabicPrefix,vocalGroupChosenMembers)
        syllable.vowels=vocalGroupChosenMembers
        syllable.syllabicPrefix=consonantPrefixGroupChosenMember
        syllable.syllabicSufix=consonantSufixGroupChosenMember

        return syllable
    }

    /*
    * 1. determine a prefix group and sufix group for the syllable (no sound group is a legit option)
    * 2. determine a vocal group (could filter for soft groups if there is not a sufix in the previous syllable
    * */
    private fun makeNewHiddenSyllable(lastSyllable: Syllable): Syllable {
        val syllable=Syllable()

        //pick a prefix syllabic sound
        val randomizer=Random()
        val hasPrefix= finishWithSoftVocal(lastSyllable) || lastSyllable.syllabicSufix.isNotEmpty()
                || randomizer.nextFloat()<0.7
        val chosenPrefixConsonantGroupName=if(hasPrefix)
            getRandomGroupNameFrom(consonantGroups) { consonantGroupName ->
                !consonantGroupName.contains(SUFIX) && consonantGroupName!=NO_SOUND_GROUP
            }
        else
            NO_SOUND_GROUP

        //pick a sufix syllabic sound
        val hasSufix=randomizer.nextFloat()<0.3
        val chosenSufixConsonantGroupName=if(hasSufix)
            getRandomGroupNameFrom(consonantGroups) { consonantGroupName ->
                !consonantGroupName.contains(PREFIX) && consonantGroupName!=NO_SOUND_GROUP
            }
        else
            NO_SOUND_GROUP
        //pick a vocal group
        val vocalGroupNames=ArrayList<String>()
        var chosenVocalGroupName=getRandomGroupNameFrom(vocalGroups){vocalGroupName ->
            if(lastSyllable.syllabicSufix.isEmpty() && !hasPrefix)
                vocalGroups.getValue(STRONG_VOCALS).contains(vocalGroupName)
            else
                vocalGroups.getValue("allVocalGroups").contains(vocalGroupName)
        }
        vocalGroupNames.add(chosenVocalGroupName)

        //pick a second vocal group
        val hasDiphthong=randomizer.nextFloat()<0.15
        if(hasDiphthong) {
            chosenVocalGroupName = getRandomGroupNameFrom(vocalGroups) { vocalGroupName ->
                if (vocalGroups.getValue(STRONG_VOCALS).contains(chosenVocalGroupName.last().toString()))
                    vocalGroups.getValue(SOFT_VOCALS).contains(vocalGroupName)
                else
                    vocalGroups.getValue("allVocalGroups").contains(vocalGroupName)
            }
            vocalGroupNames.add(chosenVocalGroupName)
        }
        syllable.syllabicPrefix=chosenPrefixConsonantGroupName
        syllable.vowels=chosenVocalGroupName
        syllable.syllabicSufix=chosenSufixConsonantGroupName

        return syllable
    }

    /*
    * get a random group name from the given hashmap that matches the condition
    * */
    private fun getRandomGroupNameFrom(hashmap: Map<String,ArrayList<String>>, condition: (String) -> Boolean):String{
        val filteredGroupNames=hashmap.keys.filter { name -> condition(name)}
        val groupChosenPosition=(0..filteredGroupNames.size).random()
        return filteredGroupNames[groupChosenPosition]
    }

    /*
    * get a random member from a group of the given hashmap that matches the condition
    * */
    private fun getRandomMemberFrom(hashmap: Map<String, ArrayList<String>>, groupName:String, condition: (String,String) -> Boolean):String{
        val groupMembers=hashmap.getValue(groupName).filter{ member -> condition(member,groupName) }
        val chosenGroupMemberPosition=(0..groupMembers.size).random()
        return groupMembers[chosenGroupMemberPosition]
    }

    private fun finishWithSoftVocal(lastSyllable: Syllable): Boolean {
        return !lastSyllable.vowels.isEmpty() && lastSyllable.syllabicSufix.isEmpty()
                && vocalGroups.getValue(SOFT_VOCALS).contains(lastSyllable.vowels.last().toString())
    }

    /*
    * do the following transformations if necesary:
    * q: {e,i} -> {que,qui}
    * g: {e,i,ue,ui} ->{gue,gui,güe,güi}
    * */
    private fun fixVocalGroup(consonantPrefixGroupChosenMember: String, chosenVocalGroupName: String, chosenPrefixConsonantGroupName: String, vocalGroupChosenMember: String): String {
        return when (consonantPrefixGroupChosenMember) {
            "q" -> if (chosenVocalGroupName == "e" || chosenVocalGroupName == "i")
                "u$vocalGroupChosenMember"
            else
                vocalGroupChosenMember
            "g" -> when (vocalGroupChosenMember) {
                "e", "i" -> if(chosenPrefixConsonantGroupName=="$PREFIX g")
                    "u$vocalGroupChosenMember"
                else
                    vocalGroupChosenMember
                "ue","ui" -> if(chosenPrefixConsonantGroupName=="$PREFIX g")
                    "ü${vocalGroupChosenMember.substring(1)}"
                else
                    vocalGroupChosenMember
                else -> vocalGroupChosenMember
            }
            else -> vocalGroupChosenMember
        }
    }

    /*
    * filter by the following rules
    * k: {a,o,u} -> {k,c}; {e,i} -> {k,q}
    * s: {a,o,u} -> {s,x,z}; {e,i} -> {c,s,x,z}
    * j: {a,o,u} -> {h,j}; {e,i} -> {g,h,j}
    * y: {todas-y} -> {ll,y}; {y} -> {ll}
    * */
    private fun isValidPrefix(element: String, group: String, chosenVocalGroupName: String): Boolean {
        return when(group){
            "$PREFIX j" -> if(element=="g")
                (chosenVocalGroupName == "e" || chosenVocalGroupName == "i")
            else
                true
            "$PREFIX k" -> when(element){
                "q" -> (chosenVocalGroupName == "e" || chosenVocalGroupName == "i")
                "c" -> !(chosenVocalGroupName == "e" || chosenVocalGroupName == "i")
                else -> true
            }
            "$PREFIX s" -> if(element=="c")
                (chosenVocalGroupName == "e" || chosenVocalGroupName == "i")
            else
                true
            else -> true
        }

    }

    /*
    * key -> name of: group of vocals or group of group of vocals
    * value -> vocals -> posible representations
    *          group of vocals -> groups with common properties
    * */
    private fun setupVocalGroups(): Map<String,ArrayList<String>> {
        return mapOf(
                "allVocalGroups" to arrayListOf("a","e","i","o","u"),
                "a" to arrayListOf("a"),
                "e" to arrayListOf("e"),
                //"i" to arrayListOf("i","y"),
                "i" to arrayListOf("i"),
                "o" to arrayListOf("o"),
                //"u" to arrayListOf("u","w"),
                "u" to arrayListOf("u"),
                SOFT_VOCALS to arrayListOf("i","u"),
                STRONG_VOCALS to arrayListOf("a","e","o"),
                "h" to arrayListOf("h")
        )
    }

    /*
    * key -> prefix/sufix syllabic sound
    * value -> posible representations of the sound
    * */
    private fun setupConsonantGroups():Map<String,ArrayList<String>>{
        return mapOf(
                NO_SOUND_GROUP to arrayListOf(""),
                "b" to arrayListOf("b","v"),
                "$PREFIX bl" to arrayListOf("bl","vl"),
                "$PREFIX br" to arrayListOf("br","vr"),
                "ch" to arrayListOf("ch"),
                "d" to arrayListOf("d"),
                "$PREFIX dr" to arrayListOf("dr"),
                "$PREFIX f" to arrayListOf("f","ph"),
                "$SUFIX f" to arrayListOf("f"),
                "$PREFIX fl" to arrayListOf("fl"),
                "$PREFIX fr" to arrayListOf("fr"),
                "g" to arrayListOf("g"),
                "$PREFIX gl" to arrayListOf("gl"),
                "$PREFIX gr" to arrayListOf("gr"),
                "h" to arrayListOf("h"),
                "$PREFIX j" to arrayListOf("j","g","h"),
                "$SUFIX j" to arrayListOf("j"),
                "k" to arrayListOf("k","c","ck","q"),
                "$PREFIX kl" to arrayListOf("kl","cl"),
                "$PREFIX kr" to arrayListOf("kr","cr"),
                "$SUFIX ks" to arrayListOf("ks","cs","cz","cks","ckz","kz","qs","qz"),
                "l" to arrayListOf("l"),
                "m" to arrayListOf("m"),
                "n" to arrayListOf("n"),
                "$SUFIX ns" to arrayListOf("ns","nz"),
                "$PREFIX ñ" to arrayListOf("ñ"),
                "p" to arrayListOf("p"),
                "$PREFIX pl" to arrayListOf("pl"),
                "$PREFIX pr" to arrayListOf("pr"),
                "r" to arrayListOf("r"),
                "$PREFIX rr" to arrayListOf("rr","r"),
                "$PREFIX s" to arrayListOf("s","c","x","z"),
                "$SUFIX s" to arrayListOf("s","z"),
                "t" to arrayListOf("t"),
                "$PREFIX tl" to arrayListOf("tl"),
                "$PREFIX tr" to arrayListOf("tr"),
                "x" to arrayListOf("x"),
                "$PREFIX y" to arrayListOf("y","sh","ll"),
                "z" to arrayListOf("z")
        )
    }

    /*
    * the toString method for syllables array list
    * */
    private fun intoString(elements:ArrayList<Syllable>):String {
        var str = ""
        for (e in elements) {
            str += e.toString()
        }
        return str
    }
}