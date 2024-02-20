/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package tf.veriny.lilligant

import net.fabricmc.api.ModInitializer
import net.minecraft.block.Block
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import tf.veriny.lilligant.config.LilligantConfig
import tf.veriny.lilligant.enchant.AerialAffinityEnchantment
import tf.veriny.lilligant.enchant.EnchantmentEffectInterceptor
import tf.veriny.lilligant.enchant.TaggedStatusEnchantmentInterceptor
import tf.veriny.lilligant.portal.HeightBasedPortalInterceptor
import tf.veriny.lilligant.portal.NetherPortalFormationInterceptor
import tf.veriny.lilligant.registry.register

public object MakeUp : ModInitializer {
    public fun id(name: String): Identifier {
        return Identifier("lilligant", name)
    }

    /** Tag for flower blocks that should have plant logic overriden. */
    public val OVERRIDE_PLANT_CHECK: TagKey<Block> = TagKey.of(RegistryKeys.BLOCK, id("override_plant_check"))
    /** Tag for valid blocks that plants in the previous tag can be placed on. */
    public val FLOWER_PLANT_BLOCKS: TagKey<Block> = TagKey.of(RegistryKeys.BLOCK, id("flower_plant_blocks"))

    override fun onInitialize() {
        LilligantConfig.register()
        NetherPortalFormationInterceptor.addInterceptor(HeightBasedPortalInterceptor)

        if (LilligantConfig.contentConfig.addTagEnchantmentInterceptor) {
            EnchantmentEffectInterceptor.addInterceptor(TaggedStatusEnchantmentInterceptor)
        }

        if (LilligantConfig.contentConfig.addAerialAffinity) {
            Registries.ENCHANTMENT.register(id("aerial_affinity"), AerialAffinityEnchantment)
        }
    }
}
