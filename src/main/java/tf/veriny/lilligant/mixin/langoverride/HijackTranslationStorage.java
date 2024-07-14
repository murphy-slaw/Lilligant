package tf.veriny.lilligant.mixin.langoverride;


import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.resource.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import tf.veriny.lilligant.LangHijacker;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Mixin(TranslationStorage.class)
abstract class HijackTranslationStorage {
    private HijackTranslationStorage() {}

    @Inject(
        method = "load(Lnet/minecraft/resource/ResourceManager;Ljava/util/List;Z)Lnet/minecraft/client/resource/language/TranslationStorage;",
        at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMap;copyOf(Ljava/util/Map;)Lcom/google/common/collect/ImmutableMap;"),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void ll$injectLangValues(
        ResourceManager resourceManager,
        List<String> definitions,
        boolean rightToLeft,
        CallbackInfoReturnable<TranslationStorage> cir,
        Map<String, String> map
    ) throws IOException {  // lol!
        LangHijacker.INSTANCE.hijackLangEntries(definitions, map);
    }
}
