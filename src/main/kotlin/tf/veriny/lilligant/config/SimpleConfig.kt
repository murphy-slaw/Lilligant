/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package tf.veriny.lilligant.config

import cc.ekblad.toml.decode
import cc.ekblad.toml.encodeToDocument
import cc.ekblad.toml.model.TomlException
import cc.ekblad.toml.model.TomlValue
import cc.ekblad.toml.serialization.from
import cc.ekblad.toml.serialization.write
import cc.ekblad.toml.tomlMapper
import cc.ekblad.toml.util.InternalAPI
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager
import java.nio.file.LinkOption
import java.nio.file.Path
import kotlin.io.path.*
import kotlin.reflect.KClass
import kotlin.reflect.full.createType

/**
 * Namespace for configuration helpers.
 */
public /*namespace*/ object SimpleConfig {
    internal val LOGGER = LogManager.getLogger(SimpleConfig::class.java)

    private val mapper = tomlMapper {
        // this is fucking awful lol
        encoder { it: Identifier -> TomlValue.String(it.toString()) }
        decoder<TomlValue.String, Identifier> { it: TomlValue.String -> Identifier(it.value) }
    }

    /**
     * Gets the config path for the provided mod ID and configuration file name.
     */
    public fun getConfigPath(modId: String, configName: String? = null): Path {
        val loader = FabricLoader.getInstance()

        return if (configName != null) {
            loader.configDir / modId / "${configName}.toml"
        } else {
            loader.configDir / "${modId}.toml"
        }
    }

    /**
     * Checks if the provided configuration file exists.
     */
    public fun configExists(modId: String, configName: String? = null): Boolean {
        return getConfigPath(modId, configName).exists(LinkOption.NOFOLLOW_LINKS)
    }

    @OptIn(InternalAPI::class)
    internal fun <T : Any> loadWithoutDefault(
        configKlass: KClass<T>, content: String
    ): T {
        return mapper.decode(configKlass.createType(), TomlValue.from(content))
    }

    /**
     * Loads a configuration file in the TOML format for the provided [modId], deserialised into
     * the provided [configKlass] type.
     *
     * If [configName] is null, then the config will be loaded from the path
     * ``config/[modId].toml``; otherwise, it will be loaded from the path
     * ``config/[modId]/[configName].toml.
     *
     * The [defaultConfig] will be used to fill in missing values to allow for seamless upgrades.
     *
     * This method either returns a constructed instance of [T], or null if there was no such file
     * (in which instance, you should immediately call [save] with the default config).
     */
    @OptIn(InternalAPI::class)  // not like it's gonna change from underneath us
    public fun <T : Any> load(
        modId: String,
        configName: String?,
        configKlass: KClass<T>,
        defaultConfig: T
    ): ConfigResult<T> {
        val configPath = getConfigPath(modId, configName)
        if (!configPath.exists()) return LoadedDefault(defaultConfig)

        val type = configKlass.createType()
        val value = TomlValue.from(configPath)
        return try {
            val final = mapper.decode<T>(type, value)
            LoadedNormally(final)
        } catch (e: TomlException.DecodingError.MissingNonNullableValue) {
            LoadedMissingProperties(mapper.decode(type, value, defaultConfig))
        }
    }

    /**
     * Saves a configuration file using the raw config text provided. This should *only* be used
     * if [load] returns ``null``, to write out the default configuration.
     *
     * If [configName] is null, then the config will be saved to the path
     * ``config/[modId].toml``; otherwise, it will be saved to the path
     * ``config/[modId]/[configName].toml.
     */
    public fun save(
        modId: String,
        rawConfig: String,
        configName: String? = null,
    ) {
        val configPath = getConfigPath(modId, configName)
        configPath.createParentDirectories()

        configPath.writer(Charsets.UTF_8).use { it.write(rawConfig) }
    }
}

/**
 * Loads a configuration file safely. This will do the following:
 *
 * 1. If the config file has no missing properties, it'll simply load it.
 * 2. If the config file has missing properties, it'll write a fresh file under a ``new.<hash>``
 *    file.
 * 3. If the config file is missing, it'll write the default config out directly.
 */
public fun <T : Any> SimpleConfig.loadSafely(
    modId: String,
    configName: String?,
    configType: KClass<T>,
    defaultValues: T,
    defaultContent: String,
): T {
    val path = getConfigPath(modId, configName)
    val loaded = load(modId, configName, configType, defaultValues)

    when (loaded) {
        is LoadedNormally -> {}
        is LoadedDefault -> {
            path.createParentDirectories()
            path.writer(Charsets.UTF_8).use { it.write(defaultContent) }
        }
        is LoadedMissingProperties -> {
            LOGGER.warn(
                "Configuration for ${modId}/${configName} has new properties " +
                "that need to be configured!"
            )
            val newName = path.name + ".new.${defaultContent.hashCode()}"
            val newPath = path.parent.resolve(newName)
            if (!newPath.exists()) {
                newPath.writer(Charsets.UTF_8).use { it.write(defaultContent) }
            }
        }
    }

    return loaded.what
}

/**
 * A reified version of [loadSafely].
 */
public inline fun <reified T : Any> SimpleConfig.loadSafely(
    modId: String,
    configName: String?,
    defaultValues: T,
    defaultContent: String
): T {
    return loadSafely(modId, configName, T::class, defaultValues, defaultContent)
}
