fun evalVariables(flag: Boolean): String {
    var s = "a"
    if (flag) {
        s += "b"
    } else {
        s += "c"
    }
    return s
}

fun box(): String {
    val result = evalVariables(false)
    return if (result == "ac") "OK" else "Fail: $result"
}