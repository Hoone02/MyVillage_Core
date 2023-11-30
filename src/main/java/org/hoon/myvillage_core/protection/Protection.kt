package org.hoon.myvillage_core.protection

import org.bukkit.Location
import java.util.UUID

data class Protection (
    var uuid: UUID,
    var owner: String,
    var firstPoint : Location,
    var secondPoint : Location,
    var cantorPoint : Location
)