//WITH_REFLECT

import java.util.*
import kotlin.properties.Delegates

fun box(): String {
    var foo: String by Delegates.notNull();

    object {
        fun baz() {
            foo = "OK"
        }
    }.baz()
    return foo
}
