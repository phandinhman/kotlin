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

package org.jetbrains.kotlin.idea.editor.quickDoc;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.JUnit3RunnerWithInners;
import org.jetbrains.kotlin.test.KotlinTestUtils;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.generators.tests.TestsPackage}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("idea/testData/editor/quickDoc")
@TestDataPath("$PROJECT_ROOT")
@RunWith(JUnit3RunnerWithInners.class)
public class QuickDocProviderTestGenerated extends AbstractQuickDocProviderTest {
    public void testAllFilesPresentInQuickDoc() throws Exception {
        KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("idea/testData/editor/quickDoc"), Pattern.compile("^([^_]+)\\.[^\\.]*$"), true);
    }

    @TestMetadata("AtConstantWithUnderscore.kt")
    public void testAtConstantWithUnderscore() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/editor/quickDoc/AtConstantWithUnderscore.kt");
        doTest(fileName);
    }

    @TestMetadata("AtFunctionParameter.kt")
    public void testAtFunctionParameter() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/editor/quickDoc/AtFunctionParameter.kt");
        doTest(fileName);
    }

    @TestMetadata("AtImplicitLambdaParameter.kt")
    public void testAtImplicitLambdaParameter() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/editor/quickDoc/AtImplicitLambdaParameter.kt");
        doTest(fileName);
    }

    @TestMetadata("AtTypeParameter.kt")
    public void testAtTypeParameter() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/editor/quickDoc/AtTypeParameter.kt");
        doTest(fileName);
    }

    @TestMetadata("AtVariableDeclaration.kt")
    public void testAtVariableDeclaration() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/editor/quickDoc/AtVariableDeclaration.kt");
        doTest(fileName);
    }

    @TestMetadata("ConstructorVarParameter.kt")
    public void testConstructorVarParameter() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/editor/quickDoc/ConstructorVarParameter.kt");
        doTest(fileName);
    }

    @TestMetadata("DeprecationWithReplaceInfo.kt")
    public void testDeprecationWithReplaceInfo() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/editor/quickDoc/DeprecationWithReplaceInfo.kt");
        doTest(fileName);
    }

    @TestMetadata("EscapeHtmlInsideCodeBlocks.kt")
    public void testEscapeHtmlInsideCodeBlocks() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/editor/quickDoc/EscapeHtmlInsideCodeBlocks.kt");
        doTest(fileName);
    }

    @TestMetadata("IndentedCodeBlock.kt")
    public void testIndentedCodeBlock() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/editor/quickDoc/IndentedCodeBlock.kt");
        doTest(fileName);
    }

    @TestMetadata("JavaClassUsedInKotlin.kt")
    public void testJavaClassUsedInKotlin() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/editor/quickDoc/JavaClassUsedInKotlin.kt");
        doTest(fileName);
    }

    @TestMetadata("JavaMethodUsedInKotlin.kt")
    public void testJavaMethodUsedInKotlin() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/editor/quickDoc/JavaMethodUsedInKotlin.kt");
        doTest(fileName);
    }

    @TestMetadata("KotlinClassUsedFromJava.java")
    public void testKotlinClassUsedFromJava() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/editor/quickDoc/KotlinClassUsedFromJava.java");
        doTest(fileName);
    }

    @TestMetadata("KotlinPackageClassUsedFromJava.java")
    public void testKotlinPackageClassUsedFromJava() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/editor/quickDoc/KotlinPackageClassUsedFromJava.java");
        doTest(fileName);
    }

    @TestMetadata("MethodFromStdLib.kt")
    public void testMethodFromStdLib() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/editor/quickDoc/MethodFromStdLib.kt");
        doTest(fileName);
    }

    @TestMetadata("OnClassDeclarationWithNoPackage.kt")
    public void testOnClassDeclarationWithNoPackage() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/editor/quickDoc/OnClassDeclarationWithNoPackage.kt");
        doTest(fileName);
    }

    @TestMetadata("OnFunctionDeclarationWithPackage.kt")
    public void testOnFunctionDeclarationWithPackage() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/editor/quickDoc/OnFunctionDeclarationWithPackage.kt");
        doTest(fileName);
    }

    @TestMetadata("OnInheritedMethodUsage.kt")
    public void testOnInheritedMethodUsage() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/editor/quickDoc/OnInheritedMethodUsage.kt");
        doTest(fileName);
    }

    @TestMetadata("OnInheritedPropertyUsage.kt")
    public void testOnInheritedPropertyUsage() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/editor/quickDoc/OnInheritedPropertyUsage.kt");
        doTest(fileName);
    }

    @TestMetadata("OnMethodUsage.kt")
    public void testOnMethodUsage() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/editor/quickDoc/OnMethodUsage.kt");
        doTest(fileName);
    }

    @TestMetadata("OnMethodUsageMultiline.kt")
    public void testOnMethodUsageMultiline() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/editor/quickDoc/OnMethodUsageMultiline.kt");
        doTest(fileName);
    }

    @TestMetadata("OnMethodUsageWithBracketsInParam.kt")
    public void testOnMethodUsageWithBracketsInParam() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/editor/quickDoc/OnMethodUsageWithBracketsInParam.kt");
        doTest(fileName);
    }

    @TestMetadata("OnMethodUsageWithCodeBlock.kt")
    public void testOnMethodUsageWithCodeBlock() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/editor/quickDoc/OnMethodUsageWithCodeBlock.kt");
        doTest(fileName);
    }

    @TestMetadata("OnMethodUsageWithMarkdown.kt")
    public void testOnMethodUsageWithMarkdown() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/editor/quickDoc/OnMethodUsageWithMarkdown.kt");
        doTest(fileName);
    }

    @TestMetadata("OnMethodUsageWithReceiver.kt")
    public void testOnMethodUsageWithReceiver() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/editor/quickDoc/OnMethodUsageWithReceiver.kt");
        doTest(fileName);
    }

    @TestMetadata("OnMethodUsageWithReturnAndLink.kt")
    public void testOnMethodUsageWithReturnAndLink() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/editor/quickDoc/OnMethodUsageWithReturnAndLink.kt");
        doTest(fileName);
    }

    @TestMetadata("OnMethodUsageWithReturnAndThrows.kt")
    public void testOnMethodUsageWithReturnAndThrows() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/editor/quickDoc/OnMethodUsageWithReturnAndThrows.kt");
        doTest(fileName);
    }

    @TestMetadata("OnMethodUsageWithSee.kt")
    public void testOnMethodUsageWithSee() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/editor/quickDoc/OnMethodUsageWithSee.kt");
        doTest(fileName);
    }

    @TestMetadata("OnMethodUsageWithTypeParameter.kt")
    public void testOnMethodUsageWithTypeParameter() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/editor/quickDoc/OnMethodUsageWithTypeParameter.kt");
        doTest(fileName);
    }

    @TestMetadata("TopLevelMethodFromJava.java")
    public void testTopLevelMethodFromJava() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/editor/quickDoc/TopLevelMethodFromJava.java");
        doTest(fileName);
    }

    @TestMetadata("TypeNamesFromStdLibNavigation.kt")
    public void testTypeNamesFromStdLibNavigation() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/editor/quickDoc/TypeNamesFromStdLibNavigation.kt");
        doTest(fileName);
    }
}
