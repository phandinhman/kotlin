// MODULE: lib
// FILE: lib.kt
package test

var value: Int = 0

inline var z: Int
    get() = ++value
    set(p: Int) { value = p }

// MODULE: main(lib)
// FILE: main.kt

import test.*

fun box(): String {
    val v = z
    if (value != 1) return "fail 1: $value"

    z = v + 2

    if (value != 3) return "fail 2: $value"
    var p = z

    if (value != 4)  return "fail 3: $value"

    return "OK"
}
