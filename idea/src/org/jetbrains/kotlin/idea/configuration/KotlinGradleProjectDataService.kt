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

package org.jetbrains.kotlin.idea.configuration

import com.intellij.openapi.externalSystem.model.DataNode
import com.intellij.openapi.externalSystem.model.ProjectKeys
import com.intellij.openapi.externalSystem.model.project.ProjectData
import com.intellij.openapi.externalSystem.service.project.IdeModifiableModelsProvider
import com.intellij.openapi.externalSystem.service.project.manage.AbstractProjectDataService
import com.intellij.openapi.externalSystem.util.ExternalSystemApiUtil
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import org.jetbrains.kotlin.config.LanguageVersion
import org.jetbrains.kotlin.idea.facet.KotlinFacet
import org.jetbrains.kotlin.idea.facet.KotlinFacetType
import org.jetbrains.kotlin.idea.facet.initializeIfNeeded
import org.jetbrains.kotlin.idea.inspections.gradle.findAll
import org.jetbrains.kotlin.idea.inspections.gradle.findKotlinPluginVersion
import org.jetbrains.plugins.gradle.model.data.BuildScriptClasspathData
import org.jetbrains.plugins.gradle.model.data.GradleSourceSetData

class KotlinGradleProjectDataService : AbstractProjectDataService<GradleSourceSetData, Void>() {
    override fun getTargetDataKey() = GradleSourceSetData.KEY

    private fun getOrCreateFacet(
            modelsProvider: IdeModifiableModelsProvider,
            module: Module
    ): KotlinFacet {
        val facetModel = modelsProvider.getModifiableFacetModel(module)

        facetModel.findFacet(KotlinFacetType.TYPE_ID, KotlinFacetType.INSTANCE.defaultFacetName)?.let { return it }

        val facet = with(KotlinFacetType.INSTANCE) { createFacet(module, defaultFacetName, createDefaultConfiguration(), null) }
        facetModel.addFacet(facet)
        return facet
    }

    override fun postProcess(
            toImport: MutableCollection<DataNode<GradleSourceSetData>>,
            projectData: ProjectData?,
            project: Project,
            modelsProvider: IdeModifiableModelsProvider
    ) {
        for (sourceSetNode in toImport) {
            val sourceSetData = sourceSetNode.data
            val ideModule = modelsProvider.findIdeModule(sourceSetData) ?: continue

            val moduleNode = ExternalSystemApiUtil.findParent(sourceSetNode, ProjectKeys.MODULE)
            val compilerVersion = moduleNode?.findAll(BuildScriptClasspathData.KEY)?.firstOrNull()?.data?.let(::findKotlinPluginVersion)
                                  ?: continue

            val facet = getOrCreateFacet(modelsProvider, ideModule)
            with(facet.configuration.settings) {
                versionInfo.targetPlatformKind = null
                versionInfo.apiLevel = null
                initializeIfNeeded(ideModule, modelsProvider.getModifiableRootModel(ideModule))
                with(versionInfo) {
                    languageLevel = LanguageVersion.fromFullVersionString(compilerVersion) ?: LanguageVersion.LATEST
                    if (apiLevel!! > languageLevel!!) {
                        apiLevel = languageLevel
                    }
                }
            }
        }
    }
}