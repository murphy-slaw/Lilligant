/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package tf.veriny.lilligant.portal

import net.minecraft.util.math.BlockPos
import net.minecraft.world.WorldAccess
import tf.veriny.lilligant.util.onServer

/**
 * Allows preventing nether portals from spawning.
 */
public fun interface NetherPortalFormationInterceptor {
    public companion object {
        private val interceptors = mutableListOf<NetherPortalFormationInterceptor>()

        /**
         * Checks if a portal can form at the specified location.
         */
        public fun check(world: WorldAccess, pos: BlockPos): PortalResult {
            for (interceptor in interceptors) {
                val result = interceptor.isPortalAllowed(world, pos)
                if (result is PortalCantForm) {
                    return result
                }
            }

            return PortalResult.ok()
        }

        /**
         * Adds a new portal formation interceptor to the list of interceptors.
         */
        public fun addInterceptor(interceptor: NetherPortalFormationInterceptor) {
            interceptors.add(interceptor)
        }
    }

    /**
     * Checks if a nether portal can form at the specified location in the specified [world].
     */
    public fun isPortalAllowed(
        world: WorldAccess,
        pos: BlockPos,
    ): PortalResult
}
