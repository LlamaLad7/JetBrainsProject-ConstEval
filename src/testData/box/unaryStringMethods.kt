fun evalUnaryStringMethods(s: String): String {
    return """
        ${s.length}
        ${s.trimIndent()}
        ${s.hashCode()}
        ${s.toString()}
    """.trimIndent()
}

fun box(): String {
    val result = evalUnaryStringMethods("   hello")
    val expected = """
        8
        hello
        -714002254
           hello
    """.trimIndent()
    return if (result == expected) "OK" else "Fail: $result"
}