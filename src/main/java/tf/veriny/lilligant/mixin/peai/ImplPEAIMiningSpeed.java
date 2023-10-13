/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package tf.veriny.lilligant.mixin.peai;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import tf.veriny.lilligant.util.PlayerEntityAwareItem;

@Mixin(PlayerInventory.class)
abstract class ImplPEAIMiningSpeed {
    @Shadow
    @Final
    public PlayerEntity player;

    private ImplPEAIMiningSpeed() {}

    @Redirect(method = "getBlockBreakingSpeed", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getMiningSpeedMultiplier(Lnet/minecraft/block/BlockState;)F"))
    float ctae$implementGetMiningSpeedMultiplier(ItemStack instance, BlockState state) {
        var item = instance.getItem();
        if (item instanceof PlayerEntityAwareItem peai) {
            return peai.getMiningSpeedMultiplier(instance, player, state);
        }

        return instance.getMiningSpeedMultiplier(state);
    }
}
