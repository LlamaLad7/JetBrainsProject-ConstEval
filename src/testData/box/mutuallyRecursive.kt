fun evalFactorial1(n: Int): Int = when (n) {
    0, 1 -> 1
    else -> evalFactorial2(n - 1) * n
}

fun evalFactorial2(n: Int): Int = when (n) {
    0, 1 -> 1
    else -> evalFactorial1(n - 1) * n
}

fun box(): String {
    val result = evalFactorial1(12)
    return if (result == 479_001_600) "OK" else "Fail: $result"
}