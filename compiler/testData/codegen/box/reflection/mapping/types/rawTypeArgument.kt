// TODO: muted automatically, investigate should it be ran for JS or not
// IGNORE_BACKEND: JS

// WITH_REFLECT
// FILE: J.java

import java.util.List;

public interface J {
    List foo();
}

// FILE: K.kt

import kotlin.reflect.jvm.javaType
import kotlin.test.assertEquals

fun box(): String {
    assertEquals(Any::class.java, J::foo.returnType.arguments.single().type!!.javaType)

    return "OK"
}
