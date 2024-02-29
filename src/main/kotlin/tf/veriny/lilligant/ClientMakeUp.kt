package tf.veriny.lilligant

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.minecraft.client.render.RenderLayer
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier

@Environment(EnvType.CLIENT)
public object ClientMakeUp : ClientModInitializer {
    override fun onInitializeClient() {
        val grassyEndstone = Registries.BLOCK.get(Identifier("lilligant:grassy_endstone"))
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutoutMipped(), grassyEndstone)

        ColorProviderRegistry.BLOCK.register(
            { state, world, pos, tintIndex ->
                if (tintIndex == 0) {
                    0xff00ffffU.toInt()
                } else -1
            },
            grassyEndstone
        )

        ColorProviderRegistry.ITEM.register(
            { _, _ -> 0xff00ffffU.toInt() },
            grassyEndstone.asItem()
        )
    }
}