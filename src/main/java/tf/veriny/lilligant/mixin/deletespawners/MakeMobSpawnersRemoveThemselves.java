package tf.veriny.lilligant.mixin.deletespawners;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tf.veriny.lilligant.config.LilligantConfig;

/**
 * Instantly removes mob spawners on tick if forced peaceful is enabled.
 */
@Mixin(MobSpawnerBlockEntity.class)
abstract class MakeMobSpawnersRemoveThemselves {
    @Inject(method = "serverTick", at = @At("HEAD"), cancellable = true)
    private static void ll$killMobSpawners(World world, BlockPos pos, BlockState state, MobSpawnerBlockEntity blockEntity, CallbackInfo ci) {
        if (LilligantConfig.INSTANCE.getContentConfig().getForcePeaceful()) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL | Block.SKIP_DROPS);
            ci.cancel();
        }
    }
}
