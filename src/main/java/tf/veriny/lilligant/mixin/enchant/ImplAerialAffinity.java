package tf.veriny.lilligant.mixin.enchant;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import tf.veriny.lilligant.enchant.AerialAffinityEnchantment;

@Mixin(PlayerEntity.class)
abstract class ImplAerialAffinity {
    @Redirect(method = "getBlockBreakingSpeed", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isOnGround()Z"))
    boolean ll$checkForAA(PlayerEntity instance) {
        var level = EnchantmentHelper.getEquipmentLevel(AerialAffinityEnchantment.INSTANCE, instance);
        return level > 0 || instance.isOnGround();
    }
}
