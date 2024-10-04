fun evalBinaryBooleanMethods(a: Boolean, b: Boolean): String {
    return """
        ${a.compareTo(b)}
        ${a < b}
        ${a <= b}
        ${a > b}
        ${a >= b}
        ${a == b}
        ${a != b}
        ${a and b}
        ${a or b}
        ${a xor b}
        ${a && b}
        ${a || b}
        ${a.equals(b)}
    """.trimIndent()
}

fun box(): String {
    val result = evalBinaryBooleanMethods(false, true)
    val expected = """
        -1
        true
        true
        false
        false
        false
        true
        false
        true
        true
        false
        true
        false
    """.trimIndent()
    return if (result == expected) "OK" else "Fail: $result"
}