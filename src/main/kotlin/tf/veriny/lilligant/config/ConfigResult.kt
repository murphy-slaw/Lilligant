package tf.veriny.lilligant.config

/**
 * Sealed class over the possible ways a configuration file could load.
 */
public sealed interface ConfigResult<out T : Any> {
    public val what: T
}

/**
 * The file loaded normally with no issues.
 */
public data class LoadedNormally<out T : Any>(override val what: T) : ConfigResult<T>

/**
 * The file loaded, but was missing some properties. A new file should be saved as a .new file
 * and a warning should be printed. The values for said properties have been included from a
 * different object.
 */
public data class LoadedMissingProperties<out T : Any>(override val what: T): ConfigResult<T>

/**
 * The configuration file was missing, so the default config was loaded. A new configuration
 * should be saved immediately.
 */
public data class LoadedDefault<out T : Any>(override val what: T): ConfigResult<T>