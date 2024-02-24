/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package tf.veriny.lilligant.pack

import net.fabricmc.loader.api.FabricLoader
import kotlin.io.path.div
import kotlin.io.path.exists
import kotlin.io.path.readLines

/**
 * Contains information about the environment a Kamuidrome pack was exported in.
 */
public data class KamuidromeMetadata(
    public val packName: String,
    public val packVersion: String,
    public val gitInfo: GitInfo?,
) {
    public data class GitInfo(
        public val commitHash: String,
        public val branch: String,
        public val commitMessage: String,
    )

    public companion object {
        /**
         * Loads the Kamuidrome metadata from the Minecraft directory.
         */
        private fun load(): KamuidromeMetadata? {
            val baseDir = FabricLoader.getInstance().gameDir
            val metadata = baseDir / "kamuidrome.metadata"
            if (!metadata.exists()) return null

            // Newline terminated file.
            val lines = metadata.readLines(Charsets.UTF_8)
            // Line 1: The pack name.
            val packName = lines[0]
            // Line 2: The pack version.
            val packVersion = lines[1]
            // Line 3: The git info.
            val rawGitInfo = lines[2]
            val gitInfo = if (rawGitInfo.isEmpty()) {
                null
            } else {
                val split = rawGitInfo.split(" ", limit = 3)
                val (commitHash, branch, message) = split
                GitInfo(commitHash, branch, message)
            }

            return KamuidromeMetadata(packName, packVersion, gitInfo)
        }

        /**
         * The cached Kamuidrome metadata for this pack.
         */
        @JvmField
        public val METADATA: KamuidromeMetadata? = load()
    }
}
