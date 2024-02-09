Lilligant
=========

This is my library and utility mod.

Installation
~~~~~~~~~~~~

For devs, you can get it like so:

.. code-block:: kotlin

    plugins {
        // Sets up Kotlin, Spotless, and various Maven repositories.
        // Add ``maven.veriny.tf`` to your plugin repository.
        id("tf.veriny.gradle.base-plugin").version("0.7.2")
        id("fabric-loom").version("1.4.1")
    }

    dependencies {
        modApi("tf.veriny.unova:lilligant:0.7.4")
    }

Features
~~~~~~~~

Sweet Berry Bushes no longer slow
---------------------------------

Sweet berry bushes will never slow the player anymore.

``PlayerEntity``-aware items
----------------------------

The ``PlayerEntityAwareItem`` interface can be implemented on items to give access to two extra
methods that are PlayerEntity-aware:

- ``isSuitableFor(ItemStack, PlayerEntity, BlockState)``
- ``getMiningSpeedMultiplier(ItemStack, PlayerEntity, BlockState)``

See the interface documentation for more details.

Config
------

See the ``SimpleConfig`` class and extensions. Also see ``ConfigContainer``, which uses Kotlin
delegates to simplify having your config objects automatically reload.

This is also done via JIJ'ing the TOML library. No extra setup required. Works on both fucking
sides!

Enchantments
------------

Lilligant adds a new ``Aerial Affinity`` enchantment which can be placed on a helmet to reduce the
normal on-ground speed penalty that you get when mining. This can be disabled in the configuration.

Lilligant hooks into how enchantment levels are calculated, allowing you to pretend like a player
or an item has an enchantment (when they really don't). Implement ``EnchantmentEffectInterceptor``
and register it with said interface in your mod initialiser.

Lilligant includes a default enchantment interceptor that allows certain enchantments to be
simulated if you have a status effect that is in a specific ``mob_effect`` tag.

- ``lilligant:gives_aqua_affinity`` - Acts as if you have the Aqua Affinity enchantment effect.
  By default, this contains the Water Breathing status effect.
- ``lilligant:gives_frost_walker`` - Acts as if you have the Frost Walker enchantment effect.
  This has no default tag entries.
- ``lilligant:gives_aerial_affinity`` - Acts as if you have the Aerial Affinity enchantment effect.
  By default, this contains the Slow Falling status effect and the Levitation status effect.

Entity Blocking
---------------

Lilligant blocks entities from spawning at the lowest level - the entity manager - meaning that
entities are well and truly unable to spawn. This is configurable in the ``entity_blocker.toml``
configuration file.

Sniffer Cooldown Adjustment
---------------------------

The ``snifferCooldown`` parameter in the content configuration can be used to customise how many
ticks a Sniffer will be on cooldown after sniffing something up, making it easier to make Sniffer
farms for e.g. torchflowers.

Portal Blocking
---------------

Lilligant adds a small API that can prevent portals from forming based on overworld conditions.
It also includes a built-in handler that stops portals from working based on height (disabled
by default). See the ``NetherPortalFormationInterceptor`` class for more information.

Narrator Error Suppression
--------------------------

Lilligant automatically suppresses the log on startup caused by a missing ``libflite.so``.

Peaceful Mode Enforcement
-------------------------

Lilligant adds a new configuration option to allow forcing peaceful mode. This locks the difficulty
to Peaceful both in-game and during world creation, and makes mob spawners automatically delete
themselves on tick as a small micro-optimisation.

JVM Argument Checking
---------------------

Lilligant adds a warning screen that warns the player if they've added useless (-XX:+EnableG1GC)
or harmful (-XX:MaxTenuringThreshold=1, which copies all the BlockPos/Vec3 spam to G1 oldgen, BAD).

Auto-Kill Ender Dragon
----------------------

On peaceful mode, the ender dragon will be automatically killed. An end portal and end gateway will
be generated like normal.