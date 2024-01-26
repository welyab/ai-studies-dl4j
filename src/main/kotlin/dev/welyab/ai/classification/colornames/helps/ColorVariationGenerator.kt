package dev.welyab.ai.classification.colornames.helps

import java.awt.Color
import java.util.TreeSet
import kotlin.math.cbrt
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt
import org.apache.commons.imaging.color.ColorConversions
import org.apache.commons.imaging.color.ColorConversions.convertRGBtoXYZ
import org.apache.commons.imaging.color.ColorConversions.convertXYZtoCIELab

object ColorVariationGenerator {

    fun generateHsvVariations(
        color: Color,
        maxVariants: Int,
    ): List<Color> {
        val extraVariants = 10
        val baseHsv = ColorConversions.convertRGBtoHSV(color.rgb)
        val variants = TreeSet<Color> { o1, o2 -> o1.rgb.compareTo(o2.rgb) }
        val hMaxDiff = 3.00
        val sMaxDiff = 0.15
        val vMaxDiff = 0.1
        var hIncrement = 0.0
        do {
            for (hSign in listOf(-1, 1)) {
                val h = (baseHsv.H + hIncrement * hSign)
                    .let { if (it > 360.0) it - 360 else it }
                    .let { if (it < 0) 360.0 + it else it }
                var sIncrement = 0.000
                do {
                    for (sSign in listOf(-1, 1)) {
                        val s = baseHsv.S + sIncrement * sSign
                        if (s < 0.0 || s > 1.0) continue
                        if (s < baseHsv.S - vMaxDiff || s > baseHsv.S + vMaxDiff) continue
                        var vIncrement = 0.000
                        do {
                            for (vSign in listOf(-1, 1)) {
                                val v = baseHsv.V + vIncrement * vSign
                                if (v < 0.0 || v > 1.0) continue
                                if (v < baseHsv.V - vMaxDiff || v > baseHsv.V + vMaxDiff) continue
                                val variant = Color(ColorConversions.convertHSVtoRGB(h, s, v))
                                if (variants.contains(variant)) continue
                                variants += variant
                            }
                            vIncrement += 0.001
                        } while (
                            variants.size < maxVariants + extraVariants
                            && (baseHsv.V - vIncrement >= 0.0 || baseHsv.V + vIncrement <= 1.0)
                            && vIncrement < vMaxDiff
                        )
                    }
                    sIncrement += 0.001
                } while (
                    variants.size < maxVariants + extraVariants
                    && (baseHsv.S - sIncrement >= 0.0 || baseHsv.S + sIncrement <= 1.0)
                    && sIncrement < sMaxDiff
                )
            }
            hIncrement += 0.0125
        } while (variants.size < maxVariants + extraVariants && hIncrement < hMaxDiff)
        return variants.toList()
    }

    fun generateRgbVariations(color: Color, maxVariants: Int): List<Color> {
        val range = ceil(cbrt(maxVariants.toDouble())).toInt()
        val rangeHalfLeft = if (range % 2 == 1) (range / 2) + 1 else range / 2
        val rangeHalfRight = range / 2
        val startRed = (color.red - rangeHalfLeft)
            .let { max(0, it) }
            .let { it - if (color.red + rangeHalfRight > 255) color.red + rangeHalfRight - 255 else 0 }
        val endRed = (color.red + rangeHalfRight)
            .let { min(255, it) }
            .let { it - if (color.red - rangeHalfLeft < 0) color.red - rangeHalfLeft else 0 }
        val startGreen = (color.green - rangeHalfLeft)
            .let { max(0, it) }
            .let { it - if (color.green + rangeHalfRight > 255) color.green + rangeHalfRight - 255 else 0 }
        val endGreen = (color.green + rangeHalfRight)
            .let { min(255, it) }
            .let { it - if (color.green - rangeHalfLeft < 0) color.green - rangeHalfLeft else 0 }
        val startBlue = (color.blue - rangeHalfLeft)
            .let { max(0, it) }
            .let { it - if (color.blue + rangeHalfRight > 255) color.blue + rangeHalfRight - 255 else 0 }
        val endBlue = (color.blue + rangeHalfRight)
            .let { min(255, it) }
            .let { it - if (color.blue - rangeHalfLeft < 0) color.blue - rangeHalfLeft else 0 }
        val variants = mutableSetOf<Color>()
        variants += color
        for (red in startRed..endRed) {
            for (green in startGreen..endGreen) {
                for (blue in startBlue..endBlue) {
                    variants += Color(red, green, blue)
                }
            }
        }
        return variants.toList()
    }

    fun generateCieLabVariations(color: Color, maxVariants: Int): List<Color> {
        val baseLabColor = convertXYZtoCIELab(convertRGBtoXYZ(color.rgb))
        fun calculateDistance(l: Double, a: Double, b: Double): Int {
            return sqrt(
                (baseLabColor.L - l).pow(2) +
                        (baseLabColor.a - a).pow(2) +
                        (baseLabColor.b - b).pow(2)
            ).times(100000000).toInt()
        }

        data class Variant(val variantColor: Color, val distance: Int) : Comparable<Variant> {
            override fun compareTo(other: Variant): Int {
                if (this.variantColor.rgb == other.variantColor.rgb) return 0
                return distance.compareTo(other.distance)
            }
        }

        val variants = TreeSet<Variant>()
        variants += Variant(color, 0)
        for (r in 0..255) {
            for (g in 0..255) {
                for (b in 0..255) {
                    val variantColor = Color(r, g, b)
                    val variantLab = convertXYZtoCIELab(convertRGBtoXYZ(variantColor.rgb))
                    val distance = calculateDistance(variantLab.L, variantLab.a, variantLab.b)
                    val variant = Variant(variantColor, distance)
                    if (variants.size < maxVariants + 10) {
                        variants += variant
                    } else if (variants.higher(variant) != null) {
                        variants.pollLast()
                        variants += variant
                    }
                }
            }
        }
        return variants
            .asSequence()
            .take(maxVariants)
            .map { it.variantColor }
            .toList()
    }
}
