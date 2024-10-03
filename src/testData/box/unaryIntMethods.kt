fun evalUnaryIntMethods(xIn: Int): String {
    var x = xIn
    return """
        ${x++}
        ${x--}
        ${++x}
        ${--x}
        ${+x}
        ${-x}
        ${x.inv()}
        ${x.toInt()}
        ${x.toString()}
        ${x.hashCode()}
    """.trimIndent()
}

fun box(): String {
    val result = evalUnaryIntMethods(294854)
    val expected = """
        294854
        294855
        294855
        294854
        294854
        -294854
        -294855
        294854
        294854
        294854
    """.trimIndent()
    return if (result == expected) "OK" else "Fail: $result"
}