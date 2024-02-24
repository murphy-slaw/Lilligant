/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package tf.veriny.lilligant.pack

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint
import org.apache.logging.log4j.LogManager

public class MetadataEntrypoint : PreLaunchEntrypoint {
    private companion object {
        private val LOGGER = LogManager.getLogger(MetadataEntrypoint::class.java)
    }

    override fun onPreLaunch() {
        val metadata = KamuidromeMetadata.METADATA ?: return
        LOGGER.info("Detected Kamuidrome metadata!")
        LOGGER.info("KAMUIDROME #1: [${metadata.packName}] [${metadata.packVersion}]")
        if (metadata.gitInfo == null) {
            LOGGER.info("KAMUIDROME #2: [N/A] [N/A]")
        } else {
            LOGGER.info("KAMUIDROME #2: [${metadata.gitInfo.commitHash}] [${metadata.gitInfo.branch}]")
        }
    }
}
