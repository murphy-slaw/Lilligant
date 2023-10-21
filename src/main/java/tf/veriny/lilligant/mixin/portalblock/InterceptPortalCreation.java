/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package tf.veriny.lilligant.mixin.portalblock;

import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.dimension.AreaHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import tf.veriny.lilligant.portal.NetherPortalFormationInterceptor;
import tf.veriny.lilligant.portal.PortalCantForm;
import tf.veriny.lilligant.util.WorldUtilKt;

import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@Mixin(AbstractFireBlock.class)
abstract class InterceptPortalCreation {
    @Inject(
        method = "onBlockAdded",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/dimension/AreaHelper;createPortal()V"
        ),
        locals = LocalCapture.CAPTURE_FAILHARD,
        cancellable = true
    )
    void ll$interceptPortalCreation(
        BlockState state,
        World world,
        BlockPos pos,
        BlockState oldState,
        boolean notify,
        CallbackInfo ci,
        Optional<AreaHelper> optional
    ) {
        if (optional.isEmpty()) return;

        var result = NetherPortalFormationInterceptor.Companion.check(world, pos);
        if (result instanceof PortalCantForm pcf) {
            WorldUtilKt.notifyInRange(world, pos, 32.0f, pcf.getWhy());
            ci.cancel();
        }
    }
}
