package com.example.pokemonsapp.model

import java.io.Serializable

data class MoveDetails(
    val damage_class: DamageClass?,
    val power: Int,
    val pp: Int,
    val name: String,
    val generation: Generation
) : Serializable

data class Generation(
    val name: String,
    val url: String
) : Serializable

data class DamageClass(
    val name: String,
    val url: String
) : Serializable

class ComparatorGeneration : Comparator<MoveDetails> {
    override fun compare(o1: MoveDetails?, o2: MoveDetails?): Int {
        if (o1 == null || o2 == null) {
            return 0
        }
        return o1.generation.name.compareTo(o2.generation.name)
    }
}

class ComparatorPP : Comparator<MoveDetails> {
    override fun compare(o1: MoveDetails?, o2: MoveDetails?): Int {
        if (o1 == null || o2 == null) {
            return 0
        }
        return o1.pp.compareTo(o2.pp)
    }
}

class ComparatorPower : Comparator<MoveDetails> {
    override fun compare(o1: MoveDetails?, o2: MoveDetails?): Int {
        if (o1 == null || o2 == null) {
            return 0
        }
        return o1.power.compareTo(o2.power)
    }
}

class ComparatorName : Comparator<MoveDetails> {
    override fun compare(o1: MoveDetails?, o2: MoveDetails?): Int {
        if (o1 == null || o2 == null) {
            return 0
        }
        return o1.name.compareTo(o2.name)
    }
}

class ComparatorCategory : Comparator<MoveDetails> {
    override fun compare(o1: MoveDetails?, o2: MoveDetails?): Int {
        if (o1?.damage_class == null || o2?.damage_class == null) {
            return 0
        }
        return o1.damage_class.name.compareTo(o2.damage_class.name)
    }
}