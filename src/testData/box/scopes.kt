fun evalScopeTest(a: Int, flag: Boolean): Int {
    var result = 1
    if (flag) {
        val a = 13
        result *= a
    } else {
        val a = 5
        result *= a
    }
    return result * a
}

fun box(): String {
    val result = evalScopeTest(7, true)
    return if (result == 7 * 13) "OK" else "Fail: $result"
}