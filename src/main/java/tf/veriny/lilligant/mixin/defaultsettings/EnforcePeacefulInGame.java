/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package tf.veriny.lilligant.mixin.defaultsettings;

import net.minecraft.world.WorldSaveProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tf.veriny.lilligant.config.LilligantConfig;

/**
 * Enforces peaceful mode in-game.
 */
@Mixin(WorldSaveProperties.class)
public abstract class EnforcePeacefulInGame {
    @Inject(method = "isDifficultyLocked", at = @At("HEAD"), cancellable = true)
    void ll$difficultyAlwaysLocked(CallbackInfoReturnable<Boolean> cir) {
        if (LilligantConfig.INSTANCE.getContentConfig().getForcePeaceful()) {
            cir.setReturnValue(true);
        }
    }
}
