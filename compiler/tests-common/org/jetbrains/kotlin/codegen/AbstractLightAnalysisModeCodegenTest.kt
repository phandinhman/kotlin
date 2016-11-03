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

package org.jetbrains.kotlin.codegen

import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.resolve.jvm.extensions.AnalysisHandlerExtension
import org.jetbrains.kotlin.resolve.jvm.extensions.PartialAnalysisHandlerExtension
import java.io.File

abstract class AbstractLightAnalysisModeCodegenTest : AbstractBytecodeListingTest() {
    override val classBuilderFactory: ClassBuilderFactory
        get() = ClassBuilderFactories.TEST_KAPT3

    override fun setupEnvironment(environment: KotlinCoreEnvironment) {
        AnalysisHandlerExtension.registerExtension(environment.project, PartialAnalysisHandlerExtension())
    }


    override fun getTextFile(ktFile: File): File {
        val boxTestsDir = File("compiler/testData/codegen/box")
        val outDir = File("compiler/testData/codegen/light-analysis", ktFile.toRelativeString(boxTestsDir)).parent
        return File(outDir, ktFile.nameWithoutExtension + ".txt")
    }
}