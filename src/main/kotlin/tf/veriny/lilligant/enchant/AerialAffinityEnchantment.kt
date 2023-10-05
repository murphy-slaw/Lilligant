/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package tf.veriny.lilligant.enchant

import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentTarget
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.EquipmentSlot

/**
 * An enchantment that can be placed on a helmet to remove thee speed penalty in midair.
 */
public object AerialAffinityEnchantment : Enchantment(
    Rarity.RARE,
    EnchantmentTarget.ARMOR_HEAD,
    arrayOf(EquipmentSlot.HEAD)
) {

    // identical algos so justt call it directly!
    override fun getMinPower(level: Int): Int = 1
    override fun getMaxPower(level: Int): Int = Enchantments.AQUA_AFFINITY.getMaxPower(level)

    override fun canAccept(other: Enchantment): Boolean {
        return super.canAccept(other) && other != Enchantments.AQUA_AFFINITY
    }
}