package me.jordanfails.core.profile

import java.util.UUID

data class Profile(
    var uuid: UUID,
    var name: String,
    var kills: Int,
    var deaths: Int,
    var killStreak: Int,
    var redeemed: Boolean,
    var reclaimed: Boolean,
    var presents: Set<Int>
) {

    constructor(
        uuid: UUID,
        name: String
    ) : this(uuid, name, 0, 0, 0, false, false, emptySet())




}