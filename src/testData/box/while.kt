fun evalFactorial(n: Int): Int {
    var result = 1
    var i = 0
    while (++i <= n) {
        result *= i
    }
    return result
}

fun box(): String {
    val result = evalFactorial(11)
    return if (result == 39_916_800) "OK" else "Fail: $result"
}