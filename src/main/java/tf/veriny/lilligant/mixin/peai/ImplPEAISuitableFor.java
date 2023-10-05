package tf.veriny.lilligant.mixin.peai;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tf.veriny.lilligant.util.PlayerEntityAwareItem;

@Mixin(PlayerEntity.class)
abstract class ImplPEAISuitableFor {
    private ImplPEAISuitableFor() {}

    @Shadow
    @Final
    private PlayerInventory inventory;

    @Inject(method = "canHarvest", at = @At("HEAD"), cancellable = true)
    void ctae$implementUserAwareHarvestable(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        var stack = this.inventory.getMainHandStack();
        if (stack.getItem() instanceof PlayerEntityAwareItem uah) {
            cir.setReturnValue(uah.isSuitableFor(stack, (PlayerEntity)(Object)this, state));
        }
    }
}
