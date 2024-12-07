/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package tf.veriny.lilligant.config

public data class ContentConfiguration(
    /** If true, then the Aerial Affinity enchantment will be added to the game. */
    public val addAerialAffinity: Boolean,
    /** Adds the tag enchantment interceptor. */
    public val addTagEnchantmentInterceptor: Boolean,
    /** Configuration for the default portal formation interceptor. */
)

/**
 * Central configuration used by Lilligant.
 */
public object LilligantConfig : ConfigContainer("lilligant") {

    public val contentConfig: ContentConfiguration by delegate(
        "content",
        LilligantConfig::class.java.getResourceAsStream("/config/content.toml")!!
    )
}
