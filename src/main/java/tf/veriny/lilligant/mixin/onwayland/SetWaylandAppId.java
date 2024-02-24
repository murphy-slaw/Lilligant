package tf.veriny.lilligant.mixin.onwayland;

import com.mojang.blaze3d.platform.GLX;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.LongSupplier;

/**
 * Sets the Wayland app ID after _initGlfw.
 */
@Mixin(GLX.class)
public class SetWaylandAppId {
    private static final int GLFW_WAYLAND_APP_ID = 0x00026001;

    @Inject(method = "_initGlfw", at = @At("TAIL"), remap = false)
    private static void ll$setWaylandAppClass(CallbackInfoReturnable<LongSupplier> cir) {
        if (GLFW.glfwGetPlatform() == GLFW.GLFW_PLATFORM_WAYLAND) {
            GLFW.glfwWindowHintString(GLFW_WAYLAND_APP_ID, "minecraft");
        }
    }
}
