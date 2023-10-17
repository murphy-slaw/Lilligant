/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package tf.veriny.lilligant.enchant

import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack

/**
 * Intercepts checks for certain enchantment effects and allows pretending that a player has an
 * enchantment on their equipment that they might not actually have.
 */
public interface EnchantmentEffectInterceptor {
    public companion object {
        private val INTERCEPTORS: MutableList<EnchantmentEffectInterceptor> = mutableListOf()

        public fun addInterceptor(giver: EnchantmentEffectInterceptor) {
            INTERCEPTORS.add(giver)
        }

        /**
         * Gets the level of the [enchantment] that the provided [entity] should have. This will
         * return the highest value from the provided interceptors.
         *
         * This generally takes priority over the [ItemStack] variant.
         */
        public fun getLevel(entity: LivingEntity, enchantment: Enchantment): Int {
            return INTERCEPTORS.maxOf { it.getEnchantmentLevel(entity, enchantment) }
        }

        /**
         * Gets the level of the [enchantment] that the provided [stack] should have. This will
         * return the highest value from the provided interceptors.
         */
        public fun getLevel(stack: ItemStack, enchantment: Enchantment): Int {
            return INTERCEPTORS.maxOf { it.getEnchantmentLevel(stack, enchantment) }
        }
    }

    /**
     * Gets the level of the provided [enchantment] on this [entity]. This should return a
     * positive integer to apply the enchantment effect, or zero if this interceptor doesn't care
     * to provide a level for said enchantment.
     */
    public fun getEnchantmentLevel(entity: LivingEntity, enchantment: Enchantment): Int {
        return 0
    }

    /**
     * Gets the level of the provided [enchantment] on the provided [stack]. This should return a
     * positive integer to apply the enchantment effect, or zero if this interceptor doesn't care
     * to provide a level for said enchantment.
     */
    public fun getEnchantmentLevel(stack: ItemStack, enchantment: Enchantment): Int {
        return 0
    }
}
