/*
 * Copyright 2010-2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.js.test.utils

import com.google.dart.compiler.backend.js.ast.*
import com.google.dart.compiler.util.AstUtil
import org.jetbrains.kotlin.js.translate.utils.JsAstUtils
import java.util.*

class NameCollector : RecursiveJsVisitor() {

    private val nameReadSet = hashSetOf<String>()
    private val nameWriteSet = hashSetOf<String>()

    fun hasUnqualifiedReads(expectedName: String) = expectedName in nameReadSet
    fun hasUnqualifiedWrites(expectedName: String) = expectedName in nameWriteSet

    override fun visitNameRef(nameRef: JsNameRef) {
        super.visitNameRef(nameRef)
        nameReadSet.add(nameRef.ident)
    }

    override fun visitBinaryExpression(x: JsBinaryOperation) {
        var assignmentToProperty = false
        JsAstUtils.decomposeAssignment(x)?.let { (name, e) ->
            (name as? JsNameRef)?.let {
                assignmentToProperty = true
                nameWriteSet.add(it.ident)
                it.qualifier?.accept(this)
                e.accept(this)
            }
        }
        if (!assignmentToProperty) {
            super.visitBinaryExpression(x)
        }
    }

    override fun visitPropertyInitializer(x: JsPropertyInitializer) {
        // Skip property initializer names
        this.accept(x.valueExpr)
    }



    companion object {
        fun collectNames(node: JsNode): NameCollector {
            val visitor = NameCollector()
            node.accept(visitor)
            return visitor
        }
    }
}
