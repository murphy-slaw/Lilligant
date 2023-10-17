package tf.veriny.lilligant.config

import cc.ekblad.toml.util.TomlName
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.EndDataPackReload
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.registry.Registries
import net.minecraft.resource.AutoCloseableResourceManager
import net.minecraft.server.MinecraftServer
import net.minecraft.util.Identifier

public data class EntityBlockerConfig(
    /** The list of entity IDs to block from creation. */
    public val blockedEntities: List<Identifier>
) {
    /**
     * Checks if the provided [EntityType] should be blocked from spawning.
     */
    public fun shouldBlock(type: EntityType<*>): Boolean {

        // the loot table ID is always cached on the entitytype, it's just the entity id with
        // ``entities/`` prepended.
        val id = type.lootTableId

        for (blocked in this.blockedEntities) {
            if (id.namespace != blocked.namespace) continue
            val realPath = id.path.substring(9 /* "entities/".length */)
            if (blocked.path == realPath) return true
        }

        return false
    }
}

public data class ContentConfiguration(
    /** If true, then the Aerial Affinity enchantment will be added to the game. */
    public val addAerialAffinity: Boolean,
    /** Adds the tag enchantment interceptor. */
    public val addTagEnchantmentInterceptor: Boolean,
)

/**
 * Central configuration used by Lilligant.
 */
public object LilligantConfig : ConfigContainer("lilligant") {
    public val entityBlockerConfig: EntityBlockerConfig by delegate(
        "entity_blocker",
        LilligantConfig::class.java.getResourceAsStream("/config/entity_blocker.toml")!!
    )

    public val contentConfig: ContentConfiguration by delegate(
        "content",
        LilligantConfig::class.java.getResourceAsStream("/config/content.toml")!!
    )

    override fun afterReload(server: MinecraftServer?) {
        if (server == null) return

        // brutally murder all entities that match
        for (world in server.worlds) {
            val allEntities = world.iterateEntities()
                .filterIsInstance<LivingEntity>()
                .groupBy { it.type }
            for ((type, entities) in allEntities) {
                val id = Registries.ENTITY_TYPE.getId(type)
                if (entityBlockerConfig.blockedEntities.any { it == id }) {
                    entities.forEach(LivingEntity::kill)
                }
            }
        }
    }
}