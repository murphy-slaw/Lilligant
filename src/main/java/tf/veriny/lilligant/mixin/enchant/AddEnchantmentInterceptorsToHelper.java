package tf.veriny.lilligant.mixin.enchant;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tf.veriny.lilligant.enchant.EnchantmentEffectInterceptor;

@Mixin(EnchantmentHelper.class)
abstract class AddEnchantmentInterceptorsToHelper {
    @Inject(method = "getLevel", at = @At("HEAD"), cancellable = true)
    private static void hisui$getEnchantmentLevel(Enchantment enchantment, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        int level;
        if ((level = EnchantmentEffectInterceptor.Companion.getLevel(stack, enchantment)) > 0) {
            cir.setReturnValue(level);
        }
    }

    @Inject(method = "getEquipmentLevel", at = @At("HEAD"), cancellable = true)
    private static void hisui$getEnchantmentLevelPlayer(Enchantment enchantment, LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
        int level;
        if ((level = EnchantmentEffectInterceptor.Companion.getLevel(entity, enchantment)) > 0) {
            cir.setReturnValue(level);
        }
    }
}
