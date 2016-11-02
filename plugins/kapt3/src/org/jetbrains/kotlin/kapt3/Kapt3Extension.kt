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

package org.jetbrains.kotlin.kapt3

import com.intellij.openapi.project.Project
import com.sun.tools.javac.tree.JCTree
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit
import org.jetbrains.kotlin.analyzer.AnalysisResult
import org.jetbrains.kotlin.codegen.*
import org.jetbrains.kotlin.codegen.state.GenerationState
import org.jetbrains.kotlin.codegen.state.KotlinTypeMapper
import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.kapt3.diagnostic.ErrorsKapt3
import org.jetbrains.kotlin.kapt3.diagnostic.KaptError
import org.jetbrains.kotlin.kapt3.stubs.ClassFileToSourceStubConverter
import org.jetbrains.kotlin.kapt3.util.KaptLogger
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.BindingTrace
import org.jetbrains.kotlin.resolve.jvm.extensions.AnalysisHandlerExtension
import java.io.File
import java.net.URLClassLoader
import java.util.*
import javax.annotation.processing.Processor
import com.sun.tools.javac.util.List as JavacList

class Kapt3Extension(
        val annotationProcessingClasspath: List<File>,
        val javaSourceRoots: List<File>,
        val sourcesOutputDir: File,
        val classFilesOutputDir: File,
        val stubsOutputDir: File?,
        val options: Map<String, String>, //TODO
        val aptOnly: Boolean,
        val pluginInitializedTime: Long,
        val logger: KaptLogger
) : AnalysisHandlerExtension {
    private var annotationProcessingComplete = false

    private fun setAnnotationProcessingComplete(): Boolean {
        if (annotationProcessingComplete) return true

        annotationProcessingComplete = true
        return false
    }

    override fun analysisCompleted(
            project: Project,
            module: ModuleDescriptor,
            bindingTrace: BindingTrace,
            files: Collection<KtFile>
    ): AnalysisResult? {
        if (setAnnotationProcessingComplete()) return null

        fun doNotGenerateCode() = AnalysisResult.Companion.success(BindingContext.EMPTY, module, shouldGenerateCode = false)

        if (files.isEmpty()) {
            logger.info("No Kotlin source files, aborting")
            return if (aptOnly) doNotGenerateCode() else null
        }

        logger.info { "Initial analysis took ${System.currentTimeMillis() - pluginInitializedTime} ms" }
        logger.info { "Kotlin files to compile: " + files.map { it.virtualFile?.name ?: "<in memory ${it.hashCode()}>" } }

        val processors = loadProcessors(annotationProcessingClasspath)
        if (processors.isEmpty()) return if (aptOnly) doNotGenerateCode() else null

        val (kaptContext, generationState) = compileStubs(project, module, bindingTrace.bindingContext, files.toList())

        try {
            val javaSourceFiles = collectJavaSourceFiles()
            val kotlinSourceStubs = generateKotlinSourceStubs(kaptContext, generationState.typeMapper)

            val (annotationProcessingTime) = measureTimeMillis {
                kaptContext.doAnnotationProcessing(
                        javaSourceFiles, processors,
                        annotationProcessingClasspath, sourcesOutputDir, classFilesOutputDir, kotlinSourceStubs)
            }

            logger.info { "Annotation processing took $annotationProcessingTime ms" }
        } catch (thr: Throwable) {
            if (thr !is KaptError || thr.kind != KaptError.Kind.ERROR_RAISED) {
                logger.exception(thr)
            }
            bindingTrace.report(ErrorsKapt3.KAPT3_PROCESSING_ERROR.on(files.first()))
            return null // Compilation will be aborted anyway because of the error above
        } finally {
            generationState.destroy()
            kaptContext.close()
        }

        return if (aptOnly) {
            doNotGenerateCode()
        } else {
            AnalysisResult.RetryWithAdditionalJavaRoots(
                    bindingTrace.bindingContext,
                    module,
                    listOf(sourcesOutputDir),
                    addToEnvironment = true)
        }
    }

    private fun compileStubs(
            project: Project,
            module: ModuleDescriptor,
            bindingContext: BindingContext,
            files: List<KtFile>
    ): Pair<KaptContext, GenerationState> {
        val builderFactory = Kapt3BuilderFactory()

        val generationState = GenerationState(
                project,
                builderFactory,
                module,
                bindingContext,
                files,
                disableCallAssertions = false,
                disableParamAssertions = false)

        val (classFilesCompilationTime) = measureTimeMillis {
            KotlinCodegenFacade.compileCorrectFiles(generationState, CompilationErrorHandler.THROW_EXCEPTION)
        }

        val compiledClasses = builderFactory.compiledClasses
        val origins = builderFactory.origins

        logger.info { "Stubs compilation took $classFilesCompilationTime ms" }
        logger.info { "Compiled classes: " + compiledClasses.joinToString { it.name } }

        return Pair(KaptContext(logger, compiledClasses, origins), generationState)
    }

    private fun generateKotlinSourceStubs(kaptContext: KaptContext, typeMapper: KotlinTypeMapper): JavacList<JCCompilationUnit> {
        val (stubGenerationTime, kotlinSourceStubs) = measureTimeMillis {
            ClassFileToSourceStubConverter(kaptContext, typeMapper).convert()
        }

        logger.info { "Java stub generation took $stubGenerationTime ms" }
        logger.info { "Stubs for Kotlin classes: " + kotlinSourceStubs.joinToString { it.sourcefile.name } }

        if (stubsOutputDir != null) {
            saveStubs(stubsOutputDir, kotlinSourceStubs)
        }

        return kotlinSourceStubs
    }

    private fun saveStubs(outputDir: File, stubs: JavacList<JCTree.JCCompilationUnit>) {
        for (stub in stubs) {
            val className = (stub.defs.first { it is JCTree.JCClassDecl } as JCTree.JCClassDecl).simpleName.toString()

            val packageName = stub.packageName?.toString() ?: ""
            val packageDir = if (packageName.isEmpty()) outputDir else File(outputDir, packageName.replace('.', '/'))
            packageDir.mkdirs()
            File(packageDir, className + ".java").writeText(stub.toString())
        }
    }

    private fun collectJavaSourceFiles(): List<File> {
        val javaFilesFromJavaSourceRoots = javaSourceRoots.flatMap {
            root -> root.walk().filter { it.isFile && it.extension == "java" }.toList()
        }
        logger.info { "Java source files: " + javaFilesFromJavaSourceRoots.joinToString { it.canonicalPath } }

        return javaFilesFromJavaSourceRoots
    }

    private fun loadProcessors(classpath: List<File>): List<Processor> {
        val classLoader = URLClassLoader(classpath.map { it.toURI().toURL() }.toTypedArray())
        val processors = ServiceLoader.load(Processor::class.java, classLoader).toList()

        if (processors.isEmpty()) {
            logger.info("No annotation processors available, aborting")
        } else {
            logger.info { "Annotation processors: " + processors.joinToString { it.javaClass.canonicalName } }
        }

        return processors
    }
}

private inline fun <T> measureTimeMillis(block: () -> T) : Pair<Long, T> {
    val start = System.currentTimeMillis()
    val result = block()
    return Pair(System.currentTimeMillis() - start, result)
}