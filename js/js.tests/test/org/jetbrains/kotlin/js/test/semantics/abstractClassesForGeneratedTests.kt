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

package org.jetbrains.kotlin.js.test.semantics

import org.jetbrains.kotlin.js.test.BasicBoxTest

abstract class BorrowedInlineTest(relativePath: String) : BasicBoxTest(
        "compiler/testData/codegen/boxInline/$relativePath",
        "${BasicBoxTest.TEST_DATA_DIR_PATH}/out/codegen/boxInline/$relativePath/"
) {
    init {
        additionalCommonFileDirectories += BasicBoxTest.TEST_DATA_DIR_PATH + relativePath + "/_commonFiles/"
    }
}

abstract class AbstractNonLocalReturnsTest : BorrowedInlineTest("nonLocalReturns/")

abstract class AbstractBoxJsTest() : BasicBoxTest(
        BasicBoxTest.TEST_DATA_DIR_PATH + "box/",
        BasicBoxTest.TEST_DATA_DIR_PATH + "out/box/"
)

abstract class AbstractJsCodegenBoxTest : BasicBoxTest(
        "compiler/testData/codegen/box/",
        BasicBoxTest.TEST_DATA_DIR_PATH + "out/codegen/box/"
)
