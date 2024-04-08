package com.gtech.rapidly.utils.convert

fun asciiToHex(input: String): String {
    val hexChars = StringBuilder()
    for (char in input) {
        val asciiValue = char.code
        val hexValue = Integer.toHexString(asciiValue)
        hexChars.append(hexValue)
    }
    return hexChars.toString().uppercase()
}

fun Double.round(): Double {
    return "%.2f".format(this).toDouble()
}