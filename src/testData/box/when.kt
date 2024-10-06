fun evalWhen(a: Int, b: Boolean, c: String, d: String): String {
    var result = ""
    when (a) {
        5, 6 -> result += "a"
        7 -> result += "b"
        else -> result += "c"
    }
    when (b) {
        true -> result += "d"
        else -> result += "e"
    }
    when (c) {
        "hello" -> result += "f"
        "world" -> result += "g"
    }
    return when (d) {
        "hi" -> result + "h"
        else -> result + "i"
    }
}

fun box(): String {
    val result = evalWhen(6, false, "oops", "hi")
    return if (result == "aeh") "OK" else "Fail: $result"
}