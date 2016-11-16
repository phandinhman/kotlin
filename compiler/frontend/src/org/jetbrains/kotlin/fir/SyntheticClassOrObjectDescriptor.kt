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

package org.jetbrains.kotlin.fir

import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.descriptors.annotations.Annotations
import org.jetbrains.kotlin.descriptors.impl.ClassDescriptorBase
import org.jetbrains.kotlin.incremental.components.LookupLocation
import org.jetbrains.kotlin.incremental.components.NoLookupLocation
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.DescriptorUtils
import org.jetbrains.kotlin.resolve.lazy.descriptors.ClassResolutionScopesSupport
import org.jetbrains.kotlin.resolve.scopes.DescriptorKindFilter
import org.jetbrains.kotlin.resolve.scopes.LexicalScope
import org.jetbrains.kotlin.resolve.scopes.MemberScope
import org.jetbrains.kotlin.resolve.scopes.MemberScopeImpl
import org.jetbrains.kotlin.storage.StorageManager
import org.jetbrains.kotlin.types.AbstractClassTypeConstructor
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.TypeConstructor
import org.jetbrains.kotlin.utils.Printer
import java.util.*

/*
 * This class introduces all attributes that are needed for synthetic classes/object so far.
 * This list may grow in the future, adding more constructor parameters.
 * This class is in 1-to-1 correspondence with SyntheticClassOrObject.
 */
class SyntheticClassOrObjectDescriptor(
        parentClassOrObject: FirClassOrObject,
        storageManager: StorageManager,
        containingDeclaration: DeclarationDescriptor,
        name: Name,
        source: SourceElement,
        outerScope: LexicalScope,
        private val modality: Modality,
        private val visibility: Visibility,
        private val kind: ClassKind,
        private val isCompanionObject: Boolean
) : ClassDescriptorBase(storageManager, containingDeclaration, name, source), ClassDescriptorWithResolutionScopes {
    val syntheticDeclaration = SyntheticClassOrObject(parentClassOrObject, name.asString(), this)

    private val typeConstructor = SyntheticTypeConstructor(storageManager)
    private val resolutionScopesSupport = ClassResolutionScopesSupport(this, storageManager, { outerScope })
    private val syntheticSupertypes = extensionAddSyntheticSupertypes()
    private val unsubstitutedMemberScope = UnsubstitutedMemberScope()

    override val annotations: Annotations get() = Annotations.EMPTY
    override fun getModality(): Modality = modality
    override fun getVisibility(): Visibility = visibility
    override fun getKind(): ClassKind = kind
    override fun isCompanionObject(): Boolean = isCompanionObject
    override fun isInner(): Boolean = false
    override fun isData(): Boolean = false
    override fun getCompanionObjectDescriptor(): ClassDescriptorWithResolutionScopes? = null
    override fun getTypeConstructor(): TypeConstructor = typeConstructor
    override fun getConstructors(): Collection<ClassConstructorDescriptor> = emptyList()
    override fun getUnsubstitutedPrimaryConstructor(): ClassConstructorDescriptor? = null
    override fun getDeclaredTypeParameters(): List<TypeParameterDescriptor> = emptyList()
    override fun getStaticScope(): MemberScope = MemberScope.Empty

    override fun getUnsubstitutedMemberScope(): MemberScope = unsubstitutedMemberScope

    override fun getDeclaredCallableMembers(): List<CallableMemberDescriptor> {
        val result = mutableListOf<CallableMemberDescriptor>()
        for (descriptor in DescriptorUtils.getAllDescriptors(unsubstitutedMemberScope))
            if (descriptor is CallableMemberDescriptor && descriptor.kind != CallableMemberDescriptor.Kind.FAKE_OVERRIDE)
                result.add(descriptor)
        return result
    }

    override fun getScopeForClassHeaderResolution(): LexicalScope = resolutionScopesSupport.scopeForClassHeaderResolution()
    override fun getScopeForConstructorHeaderResolution(): LexicalScope = resolutionScopesSupport.scopeForConstructorHeaderResolution()
    override fun getScopeForCompanionObjectHeaderResolution(): LexicalScope = resolutionScopesSupport.scopeForCompanionObjectHeaderResolution()
    override fun getScopeForMemberDeclarationResolution(): LexicalScope = resolutionScopesSupport.scopeForMemberDeclarationResolution()
    override fun getScopeForStaticMemberDeclarationResolution(): LexicalScope = resolutionScopesSupport.scopeForStaticMemberDeclarationResolution()

    override fun getScopeForInitializerResolution(): LexicalScope = throw UnsupportedOperationException("Not supported for synthetic class or object")

    private inner class SyntheticTypeConstructor(storageManager: StorageManager) : AbstractClassTypeConstructor(storageManager) {
        override fun getParameters(): List<TypeParameterDescriptor> = emptyList()
        override fun isFinal(): Boolean = true
        override fun isDenotable(): Boolean = true
        override fun getDeclarationDescriptor(): ClassifierDescriptor = this@SyntheticClassOrObjectDescriptor
        override fun computeSupertypes(): Collection<KotlinType> = syntheticSupertypes
        override val supertypeLoopChecker: SupertypeLoopChecker = SupertypeLoopChecker.EMPTY
    }

    private inner class UnsubstitutedMemberScope() : MemberScopeImpl() {
        override fun getContributedFunctions(name: Name, location: LookupLocation): Collection<SimpleFunctionDescriptor> {
            val fromSupertypes = typeConstructor.supertypes.flatMap { it.memberScope.getContributedFunctions(name, location) }
            return extensionGenerateSyntheticMethods(name, fromSupertypes)
        }

        override fun getContributedDescriptors(kindFilter: DescriptorKindFilter, nameFilter: (Name) -> Boolean): Collection<DeclarationDescriptor> {
            val result = ArrayList<DeclarationDescriptor>()
            for (supertype in typeConstructor.supertypes) {
                for (descriptor in supertype.memberScope.getContributedDescriptors()) {
                    if (descriptor is FunctionDescriptor) {
                        result.addAll(getContributedFunctions(descriptor.name, NoLookupLocation.FOR_ALREADY_TRACKED))
                    }
                    else if (descriptor is PropertyDescriptor) {
                        result.addAll(getContributedVariables(descriptor.name, NoLookupLocation.FOR_ALREADY_TRACKED))
                    }
                }
            }
            return result
        }

        override fun printScopeStructure(p: Printer) {
            p.println(javaClass.simpleName, " {")
            p.pushIndent()
            p.println("thisDescriptor = ", this@SyntheticClassOrObjectDescriptor)
            p.popIndent()
            p.println("}")
        }
    }
}