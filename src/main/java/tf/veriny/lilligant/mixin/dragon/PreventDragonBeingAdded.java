package tf.veriny.lilligant.mixin.dragon;

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * Prevents the Ender Dragon from even being added to the world.
 */
@Mixin(EnderDragonFight.class)
public abstract class PreventDragonBeingAdded {
    @Shadow @Final private ServerWorld world;

    @Shadow public abstract void dragonKilled(EnderDragonEntity dragon);

    @Shadow protected abstract void generateEndPortal(boolean previouslyKilled);

    @Shadow protected abstract void generateNewEndGateway();

    @Shadow private boolean dragonKilled;

    @Shadow private boolean previouslyKilled;

    /**
     * Prevents the dragon from even being added to the entity list.
     */
    @Redirect(method = "createDragon", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
    boolean ll$hijackDragonCreation(ServerWorld instance, Entity entity) {
        if (instance.getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        }

        return instance.spawnEntity(entity);
    }

    /**
     * Auto-kills the dragon to reset the fight.
     */
    @Inject(
        method = "createDragon",
        at = @At("TAIL"),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    void ll$autoKillDragon(
        CallbackInfoReturnable<EnderDragonEntity> cir,
        EnderDragonEntity enderDragonEntity
    ) {
        // normal dragon logic won't run, so forcibly spawn the portals.
        if (this.world.getDifficulty() == Difficulty.PEACEFUL) {
            // otherwise, it'll try and respawn thee dragon every tick.
            this.previouslyKilled = true;
            this.dragonKilled = true;
            this.generateEndPortal(true);
            this.generateNewEndGateway();
        }
    }
}
