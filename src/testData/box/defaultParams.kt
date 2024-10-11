fun evalDefaultParams(a: Int, b: Int = a * a, c: String = "$b $a"): String {
    return c
}

fun box(): String {
    val result1 = evalDefaultParams(7)
    if (result1 != "49 7") {
        return "Fail: $result1"
    }
    val result2 = evalDefaultParams(a = 213, b = 5)
    if (result2 != "5 213") {
        return "Fail: $result2"
    }
    val result3 = evalDefaultParams(a = 42, c = "hello")
    if (result3 != "hello") {
        return "Fail: $result3"
    }
    return "OK"
}