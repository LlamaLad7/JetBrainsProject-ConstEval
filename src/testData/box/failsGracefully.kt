fun evalBad(): Int {
    println("Can't eval this!")
    return -1
}

fun box(): String {
    val result = evalBad()
    return if (result == -1) "OK" else "Fail: $result"
}