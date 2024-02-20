/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package tf.veriny.lilligant.mixin.recipebook;

import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.recipe.book.RecipeBookProvider;
import net.minecraft.client.gui.screen.recipe.book.RecipeBookWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import tf.veriny.lilligant.config.LilligantConfig;

@Mixin(InventoryScreen.class)
public abstract class RemoveClientSideBookWidget extends AbstractInventoryScreen<PlayerScreenHandler> implements RecipeBookProvider {
    @Shadow @Final private RecipeBookWidget recipeBook;

    private RemoveClientSideBookWidget(PlayerScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;"))
    Element ll$disableBookWidget(InventoryScreen instance, Element element) {
        if (!LilligantConfig.INSTANCE.getContentConfig().getDisableRecipeBook()) {
            return this.addDrawableChild((TexturedButtonWidget)element);
        }

        if (recipeBook.isOpen()) recipeBook.toggleOpen();
        // prevents the inventory screen from being shifted entirely to the right
        this.x = recipeBook.findLeftEdge(width, backgroundWidth);
        // return value isn't used.
        return null;
    }
}
