fun evalUnaryBooleanMethods(flag: Boolean): String {
    return """
        ${!flag}
        ${flag.hashCode()}
        ${flag.toString()}
    """.trimIndent()
}

fun box(): String {
    val result = evalUnaryBooleanMethods(true)
    val expected = """
        false
        1231
        true
    """.trimIndent()
    return if (result == expected) "OK" else "Fail: $result"
}