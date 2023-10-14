@file:Suppress("UnstableApiUsage")

package tf.veriny.lilligant.util

import dev.onyxstudios.cca.api.v3.component.Component
import dev.onyxstudios.cca.api.v3.component.ComponentKey
import dev.onyxstudios.cca.api.v3.component.ComponentProvider
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy
import dev.onyxstudios.cca.internal.base.ComponentRegistrationInitializer
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier

/**
 * An extended version of [Component].
 */
@Suppress("unused")  // again, type safety
public interface Komponent<Target : Any> : Component {
    public fun readFromNbt(tag: NbtWrapper)

    public fun writeToNbt(tag: NbtWrapper)

    override fun readFromNbt(tag: NbtCompound): Unit = readFromNbt(NbtWrapper(tag))
    override fun writeToNbt(tag: NbtCompound): Unit = writeToNbt(NbtWrapper(tag))
}

/**
 * A wrapper around a [ComponentKey] that provides type safety.
 */
public class TypesafeComponentKey<Target : Any, C : Komponent<Target>>(
    @PublishedApi
    internal val realKey: ComponentKey<C>
) {

    /**
     * Gets the instance of the component [C] on the target [provider], or null if it doesn't exist.
     */
    public fun get(provider: Target): C? {
        return realKey.getNullable(provider)
    }
}


/**
 * Synchronises the data about the component on the provided [provider] with clients.
 */
public fun <Target : Any, C> TypesafeComponentKey<Target, C>.synchronise(
    provider: Target, with: ServerPlayerEntity? = null
)
where C : Komponent<Target>, C : AutoSyncedComponent {
    val what = get(provider) ?: return

    return if (with != null) {
        realKey.syncWith(with, provider as ComponentProvider, what, what)
    } else {
        realKey.sync(provider, what, what)
    }
}

/**
 * Marker interface for use with [key].
 */
public interface KomponentInitialiser<Target : Any, C : Komponent<Target>> :
    ComponentRegistrationInitializer

// specific subtypes, to avoid inheriting twice
public interface EntityKomponentInitialiser<Target : Entity, C : Komponent<Target>> :
    EntityComponentInitializer, KomponentInitialiser<Target, C>

/**
 * Gets a [ComponentKey] for the this [KomponentInitialiser] in a type-safe manner.
 */
@Suppress("UnusedReceiverParameter")  // type-safety
public inline fun <Target : Any, reified C : Komponent<Target>> KomponentInitialiser<Target, C>.key(
    id: Identifier
): TypesafeComponentKey<Target, C> {
    return TypesafeComponentKey(ComponentRegistry.getOrCreate(id, C::class.java))
}

/**
 * Registers a component to an [EntityComponentFactoryRegistry] using reified type parameters.
 */
public inline fun <reified E : Entity, C : Komponent<E>> EntityComponentFactoryRegistry.register(
    key: TypesafeComponentKey<E, C>, noinline callback: (E) -> C,
) {
    registerFor(E::class.java, key.realKey, callback)
}

/**
 * Like [register], but also has an optional [RespawnCopyStrategy] parameter.
 */
@JvmName("register-playerEntity")
public fun <C : Komponent<PlayerEntity>> EntityComponentFactoryRegistry.register(
    key: TypesafeComponentKey<PlayerEntity, C>,
    copyStrategy: RespawnCopyStrategy<Component> = RespawnCopyStrategy.LOSSLESS_ONLY,
    callback: (PlayerEntity) -> C
) {
    registerForPlayers(key.realKey, callback, copyStrategy)
}