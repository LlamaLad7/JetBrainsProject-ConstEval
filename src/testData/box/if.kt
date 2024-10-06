fun evalIf(a: Int, b: Boolean, c: String, d: String): String {
    var result = ""
    if (a == 5 || a == 6) {
        result += "a"
    } else if (a == 7) {
        result += "b"
    } else {
        result += "c"
    }
    if (b) {
        result += "d"
    } else {
        result += "e"
    }
    if (c == "hello") {
        result += "f"
    } else if (c == "world") {
        result += "g"
    }
    return if (d == "hi") {
        result + "h"
    } else {
        result + "i"
    }
}

fun box(): String {
    val result = evalIf(6, false, "oops", "hi")
    return if (result == "aeh") "OK" else "Fail: $result"
}