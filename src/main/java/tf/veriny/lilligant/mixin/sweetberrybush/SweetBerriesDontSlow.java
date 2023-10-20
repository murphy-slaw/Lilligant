/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package tf.veriny.lilligant.mixin.sweetberrybush;

import net.minecraft.block.BlockState;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Prevents sweet berry blocks from slowing the player down.
 */
@Mixin(SweetBerryBushBlock.class)
abstract class SweetBerriesDontSlow {
    @Redirect(
        method = "onEntityCollision",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setMovementMultiplier(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Vec3d;)V")
    )
    void ll$nullifyMovementMult(Entity instance, BlockState state, Vec3d multiplier) {
        // intentionally left blank.
    }
}
