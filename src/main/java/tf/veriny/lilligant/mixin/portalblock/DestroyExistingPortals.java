/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package tf.veriny.lilligant.mixin.portalblock;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tf.veriny.lilligant.portal.NetherPortalFormationInterceptor;
import tf.veriny.lilligant.portal.PortalCantForm;

/**
 * Destroys existing portals on entity collision (i.e. when the player exits it).
 */
@Mixin(NetherPortalBlock.class)
abstract class DestroyExistingPortals {

    // TODO: Memoize this?
    @Inject(method = "onEntityCollision", at = @At("HEAD"), cancellable = true)
    void ll$killExistingPortals(
        BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci
    ) {
        var result = NetherPortalFormationInterceptor.Companion.check(world, pos);
        if (result instanceof PortalCantForm pcf) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            ci.cancel();
        }
    }
}
