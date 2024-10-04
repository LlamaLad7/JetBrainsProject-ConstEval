fun evalFactorial(n: Int): Int = when (n) {
    0, 1 -> 1
    else -> evalFactorial(n - 1) * n
}

fun box(): String {
    val result = evalFactorial(10)
    return if (result == 3_628_800) "OK" else "Fail: $result"
}