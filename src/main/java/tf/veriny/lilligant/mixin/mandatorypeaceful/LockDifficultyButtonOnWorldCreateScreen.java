/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package tf.veriny.lilligant.mixin.mandatorypeaceful;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.world.WorldCreator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import tf.veriny.lilligant.config.LilligantConfig;

/**
 * Locks the difficulty button on the Create World screen.
 */
@Mixin(CreateWorldScreen.GameTab.class)
public abstract class LockDifficultyButtonOnWorldCreateScreen {
    @Redirect(
        method = "method_48664",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/WorldCreator;isHardcore()Z")
    )
    boolean ll$pretendDifficultyIsLocked(WorldCreator instance) {
        if (LilligantConfig.INSTANCE.getContentConfig().getForcePeaceful()) {
            return true;
        }

        return instance.isHardcore();
    }
}
