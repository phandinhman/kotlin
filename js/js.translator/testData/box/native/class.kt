package foo

@native
class A(b: Int) {
    fun g(): Int = noImpl
    fun m(): Int = noImpl
}


fun box(): String {
    if (A(2).g() != 4) {
        return "fail1"
    }
    if (A(3).m() != 2) {
        return "fail2"
    }
    val a = A(100)
    if (a.g() != 200) {
        return "fail3"
    }
    if (a.m() != 99) {
        return "fail4"
    }
    return "OK"
}
