// PROPERTY_NOT_USED: p1
// PROPERTY_NOT_READ_FROM: p2
// PROPERTY_NOT_WRITTEN_TO: p3
// CHECK_NOT_CALLED: get_p4_s8ev3o$
// CHECK_NOT_CALLED: set_p4_rksjo2$
// CHECK_NOT_CALLED: get_p5_s8ev3o$
// CHECK_NOT_CALLED: set_p6_rksjo2$

var a = 0

inline var p1: Int
    get() = a
    set(v: Int) {
        a = v
    }

var p2: Int
    inline get() = a * 10
    set(v: Int) {
        a = v / 10
    }

var p3: Int
    get() = a * 20
    inline set(v: Int) {
        a = v / 20
    }

inline var Int.p4: Int
    get() = this * a
    set(v: Int) {
        a = this * v / 30
    }

var Int.p5: Int
    inline get() = this * a
    set(v: Int) {
        a = this * v / 40
    }

var Int.p6: Int
    get() = this * a
    inline set(v: Int) {
        a = this * v / 50
    }

fun box(): String {
    p1 = 1
    if (p1 != 1) return "test1: ${p1}"
    p2 = 15
    if (p2 != 10) return "test2: ${p2}"
    p3 = 25
    if (p3 != 20) return "test3: ${p3}"

    35.p4 = 10
    if (4.p4 != 44) return "test4: ${4.p4}"
    45.p5 = 10
    if (5.p5 != 55) return "test5: ${5.p5}"
    55.p6 = 10
    if (6.p6 != 66) return "test6: ${6.p6}"

    return "OK"
}