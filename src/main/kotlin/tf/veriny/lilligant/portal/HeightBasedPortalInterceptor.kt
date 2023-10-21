/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package tf.veriny.lilligant.portal

import net.minecraft.block.AbstractFireBlock
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import org.apache.logging.log4j.LogManager
import tf.veriny.lilligant.config.LilligantConfig
import tf.veriny.lilligant.util.isOverworld

/**
 * A [NetherPortalFormationInterceptor] that blocks portal formation based on height.
 */
public object HeightBasedPortalInterceptor : NetherPortalFormationInterceptor {
    private val LOGGER = LogManager.getLogger(HeightBasedPortalInterceptor::class.java)

    private val PORTAL_TOO_HIGH = Text.translatable(
        "text.lilligant.portal.too_high"
    ).styled { it.withColor(Formatting.RED) }
    private val PORTAL_TOO_LOW = Text.translatable(
        "text.lilligant.portal.too_low"
    ).styled { it.withColor(Formatting.RED) }

    override fun isPortalAllowed(world: WorldAccess, pos: BlockPos): PortalResult {
        if (world is World) {
            if (!world.isOverworld()) return PortalResult.ok()
        }

        val config = LilligantConfig.contentConfig.portalFormation
        if (!config.enableHeightBasedInterceptor) {
            return PortalResult.ok()
        }

        LOGGER.info("${pos} ${config}")

        return when {
            pos.y > config.maxPortalWorldHeight -> {
                PortalResult.failed(PORTAL_TOO_HIGH)
            }

            pos.y < config.minPortalWorldHeight -> {
                PortalResult.failed(PORTAL_TOO_LOW)
            }

            else -> PortalResult.ok()
        }
    }
}
