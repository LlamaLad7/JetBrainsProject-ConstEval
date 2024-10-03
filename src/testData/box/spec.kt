fun evalAdd(a: Int, b: Int): Int {
    return a + b
}

fun box(): String {
    // evalAdd(1, 2) must be evaluated as 3
    val result = evalAdd(1, 2)
    return if (result == 3) "OK" else "Fail: $result"
}