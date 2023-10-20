/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package tf.veriny.lilligant.mixin.sniffer;

import net.minecraft.entity.passive.SnifferBrain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import tf.veriny.lilligant.config.LilligantConfig;

@Mixin(SnifferBrain.DigTask.class)
abstract class MakeSnifferCooldownConfigurable {
    @ModifyConstant(
        method = "finishRunning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/SnifferEntity;J)V",
        constant = @Constant(longValue = 9600)
    )
    long ll$makeSnifferCooldownConfigurable(long constant) {
        return LilligantConfig.INSTANCE.getContentConfig().getSnifferCooldown();
    }
}
