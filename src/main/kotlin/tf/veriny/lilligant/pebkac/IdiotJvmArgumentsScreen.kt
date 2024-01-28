/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package tf.veriny.lilligant.pebkac;

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.font.MultilineText
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.screen.WarningScreen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.CheckboxWidget
import net.minecraft.text.CommonTexts
import net.minecraft.text.Text
import net.minecraft.util.Formatting

@Environment(EnvType.CLIENT)
public class IdiotJvmArgumentsScreen(private val nextScreen: Screen) : WarningScreen(
    TITLE, BODY, NARRATION,
) {
    public companion object {
        public val TITLE: Text = Text.translatable("lilligant.pebkac.title").formatted(Formatting.BOLD, Formatting.RED)
        public val BODY: Text = Text.translatable("lilligant.pebkac.message")
        public val CHECKBOX: Text = Text.translatable("lilligant.pebkac.confirm")
        public val NARRATION: Text = TITLE.copy().append("\n").append(BODY);
    }

    override fun initButtons(textHeight: Int) {
        val confirm = ButtonWidget.builder(CommonTexts.PROCEED) {
            client!!.setScreen(nextScreen)
        }
            .positionAndSize(this.width / 2 - 155, 100 + textHeight, 300, 20)
            .build()

        confirm.active = false
        addDrawableChild(confirm)

        // le sigh. whatever.
        // brutally copied from WarningScreen.
        val i = (MultilineText.create(this.textRenderer, BODY, this.width - 100).count() + 1) * this.lineHeight
        val j = textRenderer.getWidth(CHECKBOX)
        this.checkbox = object : CheckboxWidget(
            this.width / 2 - (j / 2) - 8, 76 + i, j + 24, 20,
            CHECKBOX, false
        ) {
            override fun onPress() {
                super.onPress()

                confirm.active = this.isChecked
            }
        }
        this.addDrawableChild(this.checkbox)
    }
}
