/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package tf.veriny.lilligant.registry

import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier


/** Extension function that helps with Registry#register. */
public fun <T> Registry<T>.register(id: Identifier, what: T): T =
    Registry.register(this, id, what)

/**
 * Registers a block with an item form.
 */
public fun <T : Block> registerBlock(id: Identifier, block: T) {
    Registries.BLOCK.register(id, block)
    Registries.ITEM.register(id, BlockItem(block, Item.Settings()))
}
