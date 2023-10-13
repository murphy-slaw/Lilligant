/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package tf.veriny.lilligant.util

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.client.world.ClientWorld
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.WorldAccess

/**
 * An easy way to run things on both the server and client worlds in one chain with nicer syntax
 * than passing two lambdas as parameters
 */
@JvmInline
public value class ChainableWorld(public val world: World) {
    public inline fun onClient(block: ClientWorld.() -> Unit): ChainableWorld {
        world.onClient(block)
        return this
    }

    public inline fun onServer(block: ServerWorld.() -> Unit): ChainableWorld {
        world.onServer(block)
        return this
    }
}

/**
 * Runs the specified [block] on this world if, and only if, it is a client world.
 */
public inline fun World.onClient(block: ClientWorld.() -> Unit) {
    if (isClient && this is ClientWorld) block(this)
}

/**
 * Runs the specified [block] on this world if, and only if, it is a server world.
 */
public inline fun World.onServer(block: ServerWorld.() -> Unit) {
    // e.g. create ponder worlds aren't ServerWorlds.
    if (!isClient && this is ServerWorld) block(this)
}

/**
 * Sets the blockstate at [pos] to the provided [state], with the provided combination of boolean
 * flags.
 *
 * If [fireBlockUpdate] is true, then a block update will be caused.
 * If [notifyListeners] is true, then listeners and clients will be updated on the new state.
 * If [preventRedraw] is true, then the client will not re-render the block.
 * If [forceRedrawOnMainThread] is true, then the redraw will be done synchronously.
 * If [forceState] is true, then neighbours will not be notified about the change.
 * If [skipDrops] is true, then items will not be dropped for the block.
 * If [isBeingMoved] is true, then this blockstate is considered to have been moved e.g. by a
 * piston.
 *
 * The default combination of flags is ``fireBlockUpdate`` and ``notifyListeners``.
 */
public fun WorldAccess.setBlockState(
    pos: BlockPos, state: BlockState,
    fireBlockUpdate: Boolean = true,
    notifyListeners: Boolean = true,
    preventRedraw: Boolean = false,
    forceRedrawOnMainThread: Boolean = false,
    forceState: Boolean = false,
    skipDrops: Boolean = false,
    isBeingMoved: Boolean = false,
): Boolean {
    var flags = 0

    if (fireBlockUpdate) flags = flags.or(Block.NOTIFY_NEIGHBORS)
    if (notifyListeners) flags = flags.or(Block.NOTIFY_LISTENERS)
    if (preventRedraw) flags = flags.or(Block.NO_REDRAW)
    if (forceRedrawOnMainThread) flags = flags.or(Block.REDRAW_ON_MAIN_THREAD)
    if (forceState) flags = flags.or(Block.FORCE_STATE)
    if (skipDrops) flags = flags.or(Block.SKIP_DROPS)
    if (isBeingMoved) flags = flags.or(Block.MOVED)

    return setBlockState(pos, state, flags)
}
