/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package tf.veriny.lilligant

import net.fabricmc.api.ModInitializer
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import tf.veriny.lilligant.config.LilligantConfig
import tf.veriny.lilligant.enchant.AerialAffinityEnchantment
import tf.veriny.lilligant.enchant.EnchantmentEffectInterceptor
import tf.veriny.lilligant.enchant.TaggedStatusEnchantmentInterceptor
import tf.veriny.lilligant.registry.register

public object MakeUp : ModInitializer {
    public fun id(name: String): Identifier {
        return Identifier("lilligant", name)
    }

    override fun onInitialize() {
        LilligantConfig.register()

        if (LilligantConfig.contentConfig.addTagEnchantmentInterceptor) {
            EnchantmentEffectInterceptor.addInterceptor(TaggedStatusEnchantmentInterceptor)
        }

        if (LilligantConfig.contentConfig.addAerialAffinity) {
            Registries.ENCHANTMENT.register(id("aerial_affinity"), AerialAffinityEnchantment)
        }
    }
}
