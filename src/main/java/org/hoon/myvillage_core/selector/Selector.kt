package org.hoon.myvillage_core.selector

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.BlockDisplay
import org.bukkit.entity.Player

data class Selector(
    val player: Player,

    val rangeX: Float, // 선택기 범위
    val rangeY: Float, // 선택기 범위
    val rangeZ: Float, // 선택기 범위
    var placeable: Material, // 선택기가 설치할 수 있는 상태일때 보일 블록
    var nonPlaceable: Material, // 선택기가 설치할 수 없는 상태일때 보일 블록
    var direction: Double = 0.0, // 선택기가 바라보는 방향

    var pointEntity: BlockDisplay? = null, // 선택기 메인 엔티티
    var selectorEntity : BlockDisplay? = null, // 선택기 범위 엔티티
    var isCheck: Boolean = false, // 선택기가 설치될 수 있는 상태인지 체크
    var location: Location? = null, // 선택기가 설치될 위치
    var rangeLoc: Pair<Location, Location>? = null, // 선택기 범위 Location
    var placeStage: Int? = 0 // 선택기 설치 레벨
)