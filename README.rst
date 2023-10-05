Lilligant
=========

This is my library mod.

Installation
------------

This is JIJ'd into all my mods, so don't worry about it. For devs, you can get it like so:

.. code-block:: kotlin

    plugins {
        // Sets up Kotlin, Spotless, and various Maven repositories.
        id("tf.veriny.gradle.base-plugin").version("0.7.1")
        id("fabric-loom").version("1.4.1")
    }

    dependencies {
        modApi("tf.veriny.unova:lilligant:0.7.0")
    }

Config
------

See the ``SimpleConfig`` class, but tl;dr:

- ``SimpleConfig#load`` loads a TOML config, using the provided default config for unprovided
  values.
- ``SimpleConfig#save`` saves a TOML config.

This is also done via JIJ'ing the TOML library. No extra setup required. Works on both fucking
sides!

Enchantments
------------

Lilligant adds a new ``Aerial Affinity`` enchantment which can be placed on a helmet to reduce the
normal on-ground speed penalty that you get when mining.

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