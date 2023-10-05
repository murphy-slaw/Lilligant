/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package tf.veriny.lilligant.enchant

import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.registry.Holder
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import tf.veriny.lilligant.MakeUp
import kotlin.jvm.optionals.getOrNull

/**
 * An [EnchantmentEffectInterceptor] that works based on status effects in certain tags.
 */
public object TaggedStatusEnchantmentInterceptor : EnchantmentEffectInterceptor {
    /**
     * Status effects in this tag will give the Aqua Affinity effect.
     */
    public val GIVES_AQUA_AFFINITY: TagKey<StatusEffect> = key("gives_aqua_affinity")

    /**
     * Status effects in this tag will give the Frost Walker effect.
     */
    public val GIVES_FROST_WALKER: TagKey<StatusEffect> = key("gives_frost_walker")

    /**
     * Status effects in this tag will give the Aerial Affinity effect.
     */
    public val GIVES_AERIAL_AFFINITY: TagKey<StatusEffect> = key("gives_aerial_affinity")

    private fun key(name: String): TagKey<StatusEffect> {
        return TagKey.of(RegistryKeys.STATUS_EFFECT, MakeUp.id(name))
    }

    private fun getHolder(instance: StatusEffect): Holder<StatusEffect>? {
        val id =
            RegistryKey.of(RegistryKeys.STATUS_EFFECT, Registries.STATUS_EFFECT.getId(instance))
        return Registries.STATUS_EFFECT.getHolder(id).getOrNull()
    }

    override fun getEnchantmentLevel(entity: LivingEntity, enchantment: Enchantment): Int {
        val tag = when (enchantment) {
            Enchantments.FROST_WALKER -> GIVES_FROST_WALKER
            Enchantments.AQUA_AFFINITY -> GIVES_AQUA_AFFINITY
            AerialAffinityEnchantment -> GIVES_AERIAL_AFFINITY
            else -> return 0
        }

        for (effect in entity.statusEffects) {
            val holder = getHolder(effect.effectType) ?: continue
            if (holder.isIn(tag)) return 1
        }

        return 0
    }
}
