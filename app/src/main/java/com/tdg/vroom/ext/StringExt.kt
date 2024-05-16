package com.tdg.vroom.ext


fun String.getTextTwoCharacter(): String {
    val fullName = this
    if (fullName.isNotEmpty()) {
        val getCharacterName = fullName.split(" ")
        if (getCharacterName.size > 1) {
            return "${getCharacterName[0].first()}${getCharacterName[1].first()}"
        } else {
            return fullName.first().toString()
        }
    }
    return fullName
}