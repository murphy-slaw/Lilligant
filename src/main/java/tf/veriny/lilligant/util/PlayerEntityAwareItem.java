/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package tf.veriny.lilligant.util;

import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Helper interface for implementing various PlayerEntity-aware methods. Please note that this is
 * <i>not</i> injected into Item; you must implement it yourself.
 */
public interface PlayerEntityAwareItem extends FabricItem {

    /**
     * See {@link FabricItem#isSuitableFor(ItemStack, BlockState)} for more details.
     */
    default boolean isSuitableFor(
        @NotNull ItemStack stack,
        @NotNull PlayerEntity entity,
        @NotNull BlockState state
    ) {
        return isSuitableFor(stack, state);
    }

    /**
     * See {@link Item#getMiningSpeedMultiplier(ItemStack, BlockState)} for more details.
     */
    default float getMiningSpeedMultiplier(
        @NotNull ItemStack stack,
        @NotNull PlayerEntity entity,
        @NotNull BlockState state
    ) {
        return ((Item) this).getMiningSpeedMultiplier(stack, state);
    }
}
