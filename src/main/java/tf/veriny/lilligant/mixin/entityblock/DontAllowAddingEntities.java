package tf.veriny.lilligant.mixin.entityblock;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerEntityManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.entity.EntityHandler;
import net.minecraft.world.entity.EntityLike;
import net.minecraft.world.storage.ChunkDataAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tf.veriny.lilligant.config.LilligantConfig;

/**
 * Prevents adding entities at the lowest level (the entity manager).
 */
@Mixin(ServerEntityManager.class)
abstract class DontAllowAddingEntities {
    @Inject(
        method = "addEntity(Lnet/minecraft/world/entity/EntityLike;Z)Z",
        at = @At("HEAD"),
        cancellable = true,
        require = 0
    )
    void ll$hijackLowLevelAddEntity(
        EntityLike entity, boolean existing, CallbackInfoReturnable<Boolean> cir
    ) {
        var type = ((Entity) entity).getType();
        if (LilligantConfig.INSTANCE.getEntityBlockerConfig().shouldBlock(type)) {
            cir.setReturnValue(false);
        }
    }
}
