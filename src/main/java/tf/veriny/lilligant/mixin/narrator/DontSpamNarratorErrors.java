package tf.veriny.lilligant.mixin.narrator;

import com.mojang.text2speech.Narrator;
import com.mojang.text2speech.NarratorLinux;
import com.mojang.text2speech.OperatingSystem;
import net.minecraft.client.util.ChatNarratorManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Prevents the game from spamming "Failed to load library flite" errors on systems without flite.
 */
@Mixin(ChatNarratorManager.class)
public class DontSpamNarratorErrors {
    @Redirect(
        method = "<init>",
        at = @At(value = "INVOKE", target = "Lcom/mojang/text2speech/Narrator;getNarrator()Lcom/mojang/text2speech/Narrator;"),
        remap = false
    )
    @Nullable Narrator lessSpammyGetNarrator() {
        if (OperatingSystem.get() != OperatingSystem.LINUX) {
            return Narrator.getNarrator();
        }

        try {
            return new NarratorLinux();
        } catch (Narrator.InitializeException e) {
            if (e.getCause() instanceof UnsatisfiedLinkError) {
                Narrator.LOGGER.error("Couldn't load flite library, suppressing");
            } else {
                Narrator.LOGGER.error("Error while loading the narrator", e);
            }

            return Narrator.EMPTY;
        }
    }

}
