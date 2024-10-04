fun evalBinaryStringMethods(a: String, b: String, i: Int, flag: Boolean): String {
    return """
        ${a.compareTo(b)}
        ${a < b}
        ${a <= b}
        ${a > b}
        ${a >= b}
        ${a == b}
        ${a != b}
        ${a + b}
        ${a + i}
        ${a + flag}
        ${a.equals(b)}
    """.trimIndent()
}

fun box(): String {
    val result = evalBinaryStringMethods("abacus", "banana", 12, true)
    val expected = """
        -1
        true
        true
        false
        false
        false
        true
        abacusbanana
        abacus12
        abacustrue
        false
    """.trimIndent()
    return if (result == expected) "OK" else "Fail: $result"
}