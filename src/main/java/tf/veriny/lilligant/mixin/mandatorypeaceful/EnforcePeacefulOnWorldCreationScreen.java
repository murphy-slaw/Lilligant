/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package tf.veriny.lilligant.mixin.mandatorypeaceful;

import net.minecraft.client.world.WorldCreationContext;
import net.minecraft.client.world.WorldCreator;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tf.veriny.lilligant.config.LilligantConfig;

import java.nio.file.Path;
import java.util.Optional;
import java.util.OptionalLong;

/**
 * Enforces Peaceful mode on the world creation screen.
 */
@Mixin(WorldCreator.class)
public abstract class EnforcePeacefulOnWorldCreationScreen {
    @Shadow private Difficulty difficulty;

    @Shadow public abstract void generate();

    @Shadow private WorldCreator.GameMode gameMode;

    @Inject(method = "<init>", at = @At("TAIL"))
    void ll$setDifficultyToPeaceful(Path worldSavesPath, WorldCreationContext context, Optional defaultWorldType, OptionalLong seed, CallbackInfo ci) {
        if (LilligantConfig.INSTANCE.getContentConfig().getForcePeaceful()) {
            this.difficulty = Difficulty.PEACEFUL;
        }
    }

    @Inject(method = "setDifficulty", at = @At("HEAD"), cancellable = true)
    void ll$preventSetDifficultyCalls(Difficulty difficulty, CallbackInfo ci) {
        if (LilligantConfig.INSTANCE.getContentConfig().getForcePeaceful()) {
            this.difficulty = Difficulty.PEACEFUL;
            this.generate();
            ci.cancel();
        }
    }

    @Inject(method = "setGameMode", at = @At("HEAD"), cancellable = true)
    void ll$preventSettingHardcore(WorldCreator.GameMode mode, CallbackInfo ci) {
        // cycle along to creative instead
        if (LilligantConfig.INSTANCE.getContentConfig().getForcePeaceful() && mode == WorldCreator.GameMode.HARDCORE) {
            this.gameMode = WorldCreator.GameMode.CREATIVE;
            this.generate();
            ci.cancel();
        }
    }
}
