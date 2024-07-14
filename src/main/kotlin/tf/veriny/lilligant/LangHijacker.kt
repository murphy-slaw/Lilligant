package tf.veriny.lilligant

import net.fabricmc.loader.api.FabricLoader
import net.minecraft.util.Language
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import kotlin.io.path.div
import kotlin.io.path.exists
import kotlin.io.path.inputStream

public object LangHijacker {
    public val LOGGER: Logger = LogManager.getLogger(LangHijacker::class.java)

    public fun hijackLangEntries(definitions: List<String>, langStorage: MutableMap<String, String>) {
        val configPath = FabricLoader.getInstance().getConfigDir().resolve("lilligant")

        for (definition in definitions) {
            val fp = "lang_override_$definition.json"
            val file = configPath / fp

            if (file.exists()) {
                LOGGER.info("Overriding language entries from file $file")
                file.inputStream().use { s -> Language.load(s, langStorage::put) }
            }
        }
    }
}