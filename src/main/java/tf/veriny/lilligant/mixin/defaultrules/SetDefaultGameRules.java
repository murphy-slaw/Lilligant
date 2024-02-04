package tf.veriny.lilligant.mixin.defaultrules;

import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tf.veriny.lilligant.config.LilligantConfig;

import java.util.Map;

@Mixin(GameRules.class)
abstract class SetDefaultGameRules {
    @Shadow @Final private static Map<GameRules.Key<?>, GameRules.Type<?>> RULE_TYPES;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void ll$overwriteDefaultGameRuleValues(CallbackInfo ci) {
        if (LilligantConfig.INSTANCE.getContentConfig().getApplyDefaultGameRules()) {
            RULE_TYPES.put(GameRules.DO_MOB_SPAWNING, GameRules.BooleanRule.create(false));
            RULE_TYPES.put(GameRules.KEEP_INVENTORY, GameRules.BooleanRule.create(true));
            RULE_TYPES.put(GameRules.ANNOUNCE_ADVANCEMENTS, GameRules.BooleanRule.create(false));
        }
    }
}
