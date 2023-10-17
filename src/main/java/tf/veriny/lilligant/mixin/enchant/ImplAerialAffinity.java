/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package tf.veriny.lilligant.mixin.enchant;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import tf.veriny.lilligant.config.LilligantConfig;
import tf.veriny.lilligant.enchant.AerialAffinityEnchantment;

@Mixin(PlayerEntity.class)
abstract class ImplAerialAffinity {
    @Redirect(method = "getBlockBreakingSpeed", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isOnGround()Z"))
    boolean ll$checkForAA(PlayerEntity instance) {
        var isOnGround = instance.isOnGround();
        if (!LilligantConfig.INSTANCE.getContentConfig().getAddAerialAffinity()) {
            return isOnGround;
        }

        var level = EnchantmentHelper.getEquipmentLevel(AerialAffinityEnchantment.INSTANCE, instance);
        return level > 0 || isOnGround;
    }
}
