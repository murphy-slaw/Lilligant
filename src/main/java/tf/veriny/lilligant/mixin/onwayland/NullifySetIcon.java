/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package tf.veriny.lilligant.mixin.onwayland;

import com.mojang.blaze3d.glfw.Window;
import net.minecraft.client.util.WindowIcons;
import net.minecraft.resource.pack.ResourcePack;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tf.veriny.lilligant.MakeUp;

@Mixin(Window.class)
public class NullifySetIcon {
    @Inject(method = "setIcon", at = @At(value = "INVOKE", target = "Lorg/lwjgl/glfw/GLFW;glfwSetWindowIcon(JLorg/lwjgl/glfw/GLFWImage$Buffer;)V", remap = false), cancellable = true)
    void ll$rejectSetIcon(ResourcePack pack, WindowIcons icon, CallbackInfo ci) {
        LogManager.getLogger(MakeUp.class).info("Running under Wayland:" + (GLFW.glfwGetPlatform() == GLFW.GLFW_PLATFORM_WAYLAND));
        if (GLFW.glfwGetPlatform() == GLFW.GLFW_PLATFORM_WAYLAND) {
            ci.cancel();
        }
    }
}
