package tf.veriny.lilligant.mixin.recipebook;

import net.minecraft.recipe.Recipe;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerRecipeBook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tf.veriny.lilligant.config.LilligantConfig;

import java.util.Collection;

/**
 * Nuclear option that disables the server-sided recipe book entirely.
 */
@Mixin(ServerRecipeBook.class)
public class VaporiseServerSideRecipeBook {
    @Inject(method = "unlockRecipes", at = @At("HEAD"), cancellable = true)
    void ll$ignoreUnlockRecipes(Collection<Recipe<?>> recipes, ServerPlayerEntity player, CallbackInfoReturnable<Integer> cir) {
        if (LilligantConfig.INSTANCE.getContentConfig().getDisableRecipeBook()) {
            cir.setReturnValue(0);
        }
    }
}
