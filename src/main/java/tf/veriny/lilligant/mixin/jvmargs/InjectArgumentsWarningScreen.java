/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package tf.veriny.lilligant.mixin.jvmargs;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.screen.AccessibilityOnboardingScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.realms.RealmsClient;
import net.minecraft.resource.ResourceReload;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tf.veriny.lilligant.pebkac.IdiotJvmArgsEntrypoint;
import tf.veriny.lilligant.pebkac.IdiotJvmArgumentsScreen;

@Mixin(MinecraftClient.class)
public abstract class InjectArgumentsWarningScreen {
    @Shadow @Final public GameOptions options;

    @Shadow public abstract void setScreen(@Nullable Screen screen);

    @Inject(method = "setInitialScreen", at = @At("HEAD"), cancellable = true)
    void ll$switchToArgumentsWarningScreen(RealmsClient realmsClient, ResourceReload resourceReload, RunArgs.QuickPlay quickPlay, CallbackInfo ci) {
        var args = IdiotJvmArgsEntrypoint.Companion.getPEBKAC_ARGS();
        if (!args.isEmpty()) {
            if (this.options.onboardAccessibility) {
                setScreen(new IdiotJvmArgumentsScreen(new AccessibilityOnboardingScreen(options)));
            } else {
                setScreen(new IdiotJvmArgumentsScreen(new TitleScreen()));
            }

            ci.cancel();
        }
    }
}
