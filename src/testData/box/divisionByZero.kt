fun evalDiv(a: Int, b: Int): Int {
    return a / b
}

fun box(): String {
    try {
        evalDiv(5, 0)
        return "Fail: Didn't throw"
    } catch (_: ArithmeticException) {
        return "OK"
    }
}