/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package tf.veriny.lilligant.portal

import net.minecraft.text.Text

/**
 * Marker interface for portal interceptor results.
 */
public sealed interface PortalResult {
    public companion object {
        public fun ok(): PortalResult {
            return PortalCanForm
        }

        public fun failed(reason: Text): PortalResult {
            return PortalCantForm(reason)
        }
    }
}

/**
 * The portal at the provided location can form, according to the interceptor that returned this.
 */
public data object PortalCanForm : PortalResult

/**
 * The portal at the provided location can't form, with the reason [why] specified.
 */
public data class PortalCantForm(public val why: Text) : PortalResult
