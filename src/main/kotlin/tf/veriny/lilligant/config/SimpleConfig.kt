/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package tf.veriny.lilligant.config

import cc.ekblad.toml.encodeToDocument
import cc.ekblad.toml.model.TomlValue
import cc.ekblad.toml.serialization.from
import cc.ekblad.toml.serialization.write
import cc.ekblad.toml.tomlMapper
import cc.ekblad.toml.util.InternalAPI
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.util.Identifier
import java.nio.file.Path
import kotlin.io.path.createParentDirectories
import kotlin.io.path.div
import kotlin.io.path.exists
import kotlin.io.path.writer
import kotlin.reflect.KClass
import kotlin.reflect.full.createType

// this is the entire api!
/**
 * Namespace for configuration helpers.
 */
public /*namespace*/ object SimpleConfig {

    private val mapper = tomlMapper {
        // this is fucking awful lol
        encoder { it: Identifier -> TomlValue.String(it.toString()) }
        decoder<TomlValue.String, Identifier> { it: TomlValue.String -> Identifier(it.value) }
    }

    private fun getConfigPath(modId: String, configName: String?): Path {
        val loader = FabricLoader.getInstance()

        return if (configName != null) {
            loader.configDir / modId / "${configName}.toml"
        } else {
            loader.configDir / "${modId}.toml"
        }
    }

    /**
     * Loads a configuration file in the TOML format for the provided [modId], deserialised into
     * the provided [configKlass] type.
     *
     * If [configName] is null, then the config will be loaded from the path
     * ``config/[modId].toml``; otherwise, it will be loaded from the path
     * ``config/[modId]/[configName].toml.
     *
     * If [defaultConfig] is not null, then the values will be used to fill in missing keys from
     * the loaded config.
     *
     * This method either returns a constructed instance of [T], or null if there was no such file
     * (in which instance, you should immediately call [save] with the default config).
     */
    @OptIn(InternalAPI::class)  // not like it's gonna change from underneath us
    public fun <T : Any> load(
        modId: String,
        configKlass: KClass<T>,
        configName: String? = null,
        defaultConfig: T? = null,
    ): T? {
        val configPath = getConfigPath(modId, configName)
        if (!configPath.exists()) return null

        return if (defaultConfig != null) {
            mapper.decode(configKlass.createType(), TomlValue.from(configPath), defaultConfig)
        } else {
            mapper.decode(configKlass.createType(), TomlValue.from(configPath))
        }
    }

    /**
     * Saves a configuration file in the TOML format for the provided [modId], serialising it
     * from the provided [config] instance.
     *
     * If [configName] is null, then the config will be saved to the path
     * ``config/[modId].toml``; otherwise, it will be saved to the path
     * ``config/[modId]/[configName].toml.
     */
    public fun <T : Any> save(
        modId: String,
        config: T,
        configName: String? = null,
    ) {
        val configPath = getConfigPath(modId, configName)
        configPath.createParentDirectories()

        val content = mapper.encodeToDocument(config)
        configPath.writer(Charsets.UTF_8).use { content.write(it) }
    }
}
