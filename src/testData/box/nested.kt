fun evalOuter(a: Int, b: String): String {
    return "$a $b ${evalInner(b)}"
}

fun evalInner(b: String) = b.length

fun box(): String {
    val result = evalOuter(43, "Hello World!")
    return if (result == "43 Hello World! 12") "OK" else "Fail: $result"
}