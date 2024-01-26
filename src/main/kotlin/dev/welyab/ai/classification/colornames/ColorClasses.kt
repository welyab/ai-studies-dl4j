package dev.welyab.ai.classification.colornames

import java.awt.Color

enum class ColorName {
    WHITE,
    GRAY,
    BLACK,
    RED,
    GREEN,
    BLUE,
    ORANGE,
    PINK,
    YELLOW,
    BROWN,
    PURPLE,
    BEIGE
}

object ColorClasses {
    val colorRgbByName = mapOf(
        ColorName.WHITE to listOf(
            Color(255, 255, 255),
        ),
        ColorName.GRAY to listOf(
            Color(25, 25, 25),
            Color(50, 50, 50),
            Color(75, 75, 75),
            Color(100, 100, 100),
            Color(125, 125, 125),
            Color(150, 150, 150),
            Color(175, 175, 175),
            Color(200, 200, 200),
            Color(225, 225, 225),
        ),
        ColorName.BLACK to listOf(
            Color(0, 0, 0),
        ),
        ColorName.RED to listOf(
            Color(255, 0, 0),
            Color(150, 0, 0),
            Color(100, 0, 0),
            Color(255, 50, 0),
        ),
        ColorName.GREEN to listOf(
            Color(0, 255, 0),
            Color(0, 200, 0),
            Color(0, 150, 0),
            Color(0, 100, 0),
            Color(0, 50, 0),
            Color(50, 255, 0),
            Color(100, 255, 0),
            Color(150, 255, 0),
            Color(100, 255, 0),
        ),
        ColorName.BLUE to listOf(
            Color(0, 0, 255),
            Color(0, 0, 200),
            Color(0, 0, 150),
            Color(0, 0, 100),
            Color(0, 0, 50),
            Color(0, 255, 255),
            Color(0, 200, 255),
            Color(0, 150, 255),
            Color(0, 100, 255),
            Color(0, 50, 255),
        ),
        ColorName.ORANGE to listOf(
            Color(255, 106, 10),
            Color(255, 117, 25),
            Color(255, 132, 50),
        ),
        ColorName.PINK to listOf(
            Color(255, 0, 255),
            Color(255, 50, 255),
            Color(255, 100, 255),
            Color(255, 150, 255),
            Color(246, 38, 129),
        ),
        ColorName.YELLOW to listOf(
            Color(255, 255, 0),
            Color(255, 255, 25),
            Color(255, 255, 50),
            Color(255, 255, 100),
        ),
        ColorName.BROWN to listOf(
            Color(150, 75, 0),
            Color(178, 89, 0),
            Color(178, 100, 0),
            Color(114, 63, 11),
            Color(76, 42, 7),
        ),
        ColorName.PURPLE to listOf(
            Color(160, 32, 240),
            Color(93, 19, 142),
        ),
        ColorName.BEIGE to listOf(
            Color(245, 245, 220),
            Color(245, 245, 190),
        ),
    )

    val colorClassLabelByName = colorRgbByName
        .entries
        .sortedBy { it.key }
        .mapIndexed { index, entry -> entry.key to index }
        .toMap()

    val colorNameByClassLabel = colorClassLabelByName
        .entries
        .associate { it.value to it.key }
}
