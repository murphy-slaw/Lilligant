package tf.veriny.lilligant.config

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.resource.AutoCloseableResourceManager
import net.minecraft.server.MinecraftServer
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.InputStream
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * A container of configuration objects that get automatically reloaded on server datapack reload.
 */
public abstract class ConfigContainer(
    private val modId: String
) : ServerLifecycleEvents.EndDataPackReload {
    protected inner class Delegate<T : Any>(
        public val configName: String,
        public val type: KClass<T>,
        public val default: T,
        public val defaultContent: String,
    ) : ReadOnlyProperty<Any?, T> {
        public var value: T = default
            private set

        override fun getValue(thisRef: Any?, property: KProperty<*>): T {
            return value
        }

        public fun reload() {
            value = SimpleConfig.loadSafely(
                modId,
                configName,
                type,
                default,
                defaultContent
            )
        }
    }

    protected val logger: Logger = LogManager.getLogger(this.javaClass)
    protected val delegates: MutableList<Delegate<*>> = mutableListOf()

    /** Creates a new configuration delegate that is automatically reloaded. */
    protected fun <T : Any> delegate(
        configName: String, type: KClass<T>, default: T, defaultContent: String,
    ): Delegate<T> {
        val delegate = Delegate(configName, type, default, defaultContent)
        delegates.add(delegate)
        return delegate
    }

    /**
     * Creates a new configuration delegate that uses the provided [InputStream] to load the
     * default content. Used for classpath default files.
     *
     * This takes ownership of the passed-in [InputStream] and closes it automatically.
     */
    protected fun <T : Any> delegate(
        configName: String, type: KClass<T>, includedDefaultData: InputStream
    ): Delegate<T> {
        val content = includedDefaultData.use {
            it.reader(Charsets.UTF_8).readText()
        }

        val deserialised = SimpleConfig.loadWithoutDefault(type, content)
        return delegate(configName, type, deserialised, content)
    }

    /**
     * Like the previous function, but uses a reified parameter.
     */
    protected inline fun <reified T : Any> delegate(
        configName: String, includedDefaultData: InputStream
    ): Delegate<T> = delegate(configName, T::class, includedDefaultData)


    /**
     * Callled immediately after being reloaded.
     */
    protected open fun afterReload(server: MinecraftServer?) {}

    /**
     * Registers the config container now and loads the initial config. Should be called from
     * your mod initialiser.
     */
    public fun register() {
        delegates.forEach(Delegate<*>::reload)
        afterReload(null)
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register(this)
    }

    final override fun endDataPackReload(
        server: MinecraftServer,
        resourceManager: AutoCloseableResourceManager,
        success: Boolean
    ) {
        delegates.forEach(Delegate<*>::reload)
        logger.debug("Reloaded config container...")
        afterReload(server)
    }
}