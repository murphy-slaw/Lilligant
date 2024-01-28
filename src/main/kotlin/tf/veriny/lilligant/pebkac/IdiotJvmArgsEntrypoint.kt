/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package tf.veriny.lilligant.pebkac

import net.fabricmc.api.ModInitializer
import org.apache.logging.log4j.LogManager
import java.lang.management.ManagementFactory

public class IdiotJvmArgsEntrypoint : ModInitializer {
    public companion object {
        private val FLAG_ARGS = mutableSetOf(
            "-XX:+UseG1GC",
            "-Daikars.new.flags=true",
            "-Dusing.aikars.flags=https://mcflags.emc.gs",
            "-XX:MaxTenuringThreshold=1",
        )
        private val LOGGER = LogManager.getLogger(IdiotJvmArgsEntrypoint::class.java)

        public var PEBKAC_ARGS: Set<String> = setOf<String>()
            private set
    }

    override fun onInitialize() {
        val args = ManagementFactory.getRuntimeMXBean().inputArguments
        val intersection = FLAG_ARGS.intersect(args.toSet())

        if (intersection.isNotEmpty()) {
            LOGGER.error("PEBKAC detected! Your warranty is now voided!")
            LOGGER.error("Detected problematic flags: ${intersection.joinToString(", ")}")
            PEBKAC_ARGS = intersection
        } else {
            LOGGER.info("Thank you for not messing with your JVM args :)")
        }
    }
}
