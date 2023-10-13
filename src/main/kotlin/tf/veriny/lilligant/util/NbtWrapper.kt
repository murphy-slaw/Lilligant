/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package tf.veriny.lilligant.util

import it.unimi.dsi.fastutil.ints.IntArrayList
import it.unimi.dsi.fastutil.ints.IntList
import it.unimi.dsi.fastutil.ints.IntLists
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.NbtString
import net.minecraft.util.Identifier
import java.util.*

/**
 * Wraps a [NbtCompound] and provides more convenient methods via extensions. This can be used
 * to avoid method name shadowing on [NbtCompound].
 */
@JvmInline
public value class NbtWrapper(public val compound: NbtCompound)

public operator fun NbtWrapper.set(key: String, value: Int): Unit = compound.putInt(key, value)
public operator fun NbtWrapper.set(key: String, value: String): Unit = compound.putString(key, value)
public operator fun NbtWrapper.set(key: String, value: UUID): Unit = compound.putUuid(key, value)


// list wrappers
/** Sets [key] simply to an NBT list. */
public operator fun NbtWrapper.set(key: String, value: NbtList) {
    compound.put(key, value)
}

// yay for jvmname!
/** Sets [key] to a list of generic [NbtElement]s. */
@JvmName("NbtWrapper-set#listNbtElement")
public operator fun NbtWrapper.set(key: String, value: List<NbtElement>) {
    compound.put(key, NbtList().also { it.addAll(value) })
}

/** Sets [key] to a list of [Identifier]s. */
@JvmName("NbtWrapper-set#listIdentifier")
public operator fun NbtWrapper.set(key: String, value: List<Identifier>) {
    compound.put(key, value.mapTo(NbtList()) { NbtString.of(it.toString()) })
}

/** Sets [key] to a list of JVM strings. */
@JvmName("NbtWrapper-set#listString")
public operator fun NbtWrapper.set(key: String, value: List<String>) {
    compound.put(key, value.mapTo(NbtList()) { NbtString.of(it) })
}

/**
 * Sets [key] in this NBT compound to the provided Enum [value]. This is stored as the *name* of
 * the entry, for compatibility with extending the provided Enum.
 */
public operator fun <EnumValue : Enum<EnumValue>> NbtWrapper.set(
    key: String, value: EnumValue
): Unit = set(key, value.name)

/**
 * Gets the enum value stored at [key] in this NBT compound. This is an inline function that
 * requires passing a specific Enum; if the type of Enum is unknown, use [NbtCompound.getString]
 * instead.
 */
public inline fun <reified EnumValue : Enum<EnumValue>> NbtWrapper.getEnumValue(
    key: String
): EnumValue {
    return enumValueOf<EnumValue>(compound.getString(key))
}

/**
 * Gets a list of the provided [Value] within this NBT compound.
 */
public inline fun <reified Value> NbtWrapper.getList(key: String): List<Value> {
    if (!compound.contains(key)) return emptyList()

    return when (Value::class) {
        String::class -> {
            compound.getList(key, NbtList.STRING_TYPE.toInt())
                .map { it as NbtString }
                .mapTo(mutableListOf()) { it.asString() as Value }
        }
        NbtCompound::class -> {
            compound.getList(key, NbtList.COMPOUND_TYPE.toInt()) as List<Value>
        }
        NbtWrapper::class -> {
            // fundamentally a no-op
            compound.getList(key, NbtList.COMPOUND_TYPE.toInt())
                .map { NbtWrapper(it as NbtCompound) } as List<Value>
        }
        else -> {
            throw IllegalArgumentException("Invalid klass: ${Value::class}")
        }
    }
}

/**
 * Gets an [IntList] from the provided key, or empty if there is no such key. Easier to use than
 * the array form.
 */
public fun NbtWrapper.getIntList(key: String): IntList {
    if (!compound.contains(key)) return IntLists.EMPTY_LIST

    return IntArrayList(compound.getIntArray(key))
}
