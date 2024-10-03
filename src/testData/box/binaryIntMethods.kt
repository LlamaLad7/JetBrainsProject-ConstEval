fun evalBinaryIntMethods(a: Int, b: Int): String {
    return """
        ${a < b}
        ${a <= b}
        ${a > b}
        ${a >= b}
        ${a == b}
        ${a != b}
        ${a + b}
        ${a - b}
        ${a * b}
        ${a / b}
        ${a % b}
        ${a shl b}
        ${a shr b}
        ${-a ushr b}
        ${a and b}
        ${a or b}
        ${a xor b}
    """.trimIndent()
}

fun box(): String {
    val result = evalBinaryIntMethods(10239, 7)
    val expected = """
        false
        false
        true
        true
        false
        true
        10246
        10232
        71673
        1462
        5
        1310592
        79
        33554352
        7
        10239
        10232
    """.trimIndent()
    return if (result == expected) "OK" else "Fail: $result"
}