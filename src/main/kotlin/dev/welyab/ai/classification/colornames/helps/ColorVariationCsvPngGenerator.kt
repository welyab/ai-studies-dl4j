package dev.welyab.ai.classification.colornames.helps

import com.google.common.collect.ComparisonChain
import dev.welyab.ai.classification.colornames.ColorClasses
import dev.welyab.ai.classification.colornames.ColorName
import dev.welyab.ai.classification.colornames.ColorName.BEIGE
import dev.welyab.ai.classification.colornames.ColorName.BLACK
import dev.welyab.ai.classification.colornames.ColorName.BLUE
import dev.welyab.ai.classification.colornames.ColorName.BROWN
import dev.welyab.ai.classification.colornames.ColorName.GRAY
import dev.welyab.ai.classification.colornames.ColorName.GREEN
import dev.welyab.ai.classification.colornames.ColorName.ORANGE
import dev.welyab.ai.classification.colornames.ColorName.PINK
import dev.welyab.ai.classification.colornames.ColorName.PURPLE
import dev.welyab.ai.classification.colornames.ColorName.RED
import dev.welyab.ai.classification.colornames.ColorName.WHITE
import dev.welyab.ai.classification.colornames.ColorName.YELLOW
import dev.welyab.ai.classification.colornames.helps.ColorVariationGenerator.generateCieLabVariations
import dev.welyab.ai.classification.colornames.helps.ColorVariationGenerator.generateRgbVariations
import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.RandomAccessFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.math.sqrt
import org.apache.commons.imaging.ImageFormats
import org.apache.commons.imaging.Imaging

interface VariationProducer {
    fun createVariations(): List<Color>
}

fun main() {
    val colorVariationGenerators = mapOf(
        WHITE to object : VariationProducer {
            override fun createVariations() = ColorClasses.colorRgbByName[WHITE]!!
                .flatMap {
                    generateCieLabVariations(it, 20)
                }.flatMap {
                    generateRgbVariations(it, 10)
                }.distinct().toList()
        },
        BLACK to object : VariationProducer {
            override fun createVariations() = ColorClasses.colorRgbByName[BLACK]!!
                .flatMap {
                    generateCieLabVariations(it, 20)
                }.flatMap {
                    generateRgbVariations(it, 10)
                }.distinct().toList()
        },
        GRAY to object : VariationProducer {
            override fun createVariations() = ColorClasses.colorRgbByName[GRAY]!!
                .flatMap {
                    generateCieLabVariations(it, 3)
                }.flatMap {
                    generateRgbVariations(it, 1)
                }.distinct().toList()
        },
        RED to object : VariationProducer {
            override fun createVariations() = ColorClasses.colorRgbByName[RED]!!
                .flatMap {
                    generateCieLabVariations(it, 15)
                }.flatMap {
                    generateRgbVariations(it, 10)
                }.distinct().toList()
        },
        GREEN to object : VariationProducer {
            override fun createVariations() = ColorClasses.colorRgbByName[GREEN]!!
                .flatMap {
                    generateCieLabVariations(it, 20)
                }.flatMap {
                    generateRgbVariations(it, 10)
                }.distinct().toList()
        },
        BLUE to object : VariationProducer {
            override fun createVariations() = ColorClasses.colorRgbByName[BLUE]!!
                .flatMap {
                    generateCieLabVariations(it, 20)
                }.flatMap {
                    generateRgbVariations(it, 10)
                }.distinct().toList()
        },
        ORANGE to object : VariationProducer {
            override fun createVariations() = ColorClasses.colorRgbByName[ORANGE]!!
                .flatMap {
                    generateCieLabVariations(it, 6)
                }.flatMap {
                    generateRgbVariations(it, 1)
                }.distinct().toList()
        },
        PINK to object : VariationProducer {
            override fun createVariations() = ColorClasses.colorRgbByName[PINK]!!
                .flatMap {
                    generateCieLabVariations(it, 6)
                }.flatMap {
                    generateRgbVariations(it, 4)
                }.distinct().toList()
        },
        YELLOW to object : VariationProducer {
            override fun createVariations() = ColorClasses.colorRgbByName[YELLOW]!!
                .flatMap {
                    generateCieLabVariations(it, 5)
                }.flatMap {
                    generateRgbVariations(it, 5)
                }.distinct().toList()
        },
        BROWN to object : VariationProducer {
            override fun createVariations() = ColorClasses.colorRgbByName[BROWN]!!
                .flatMap {
                    generateCieLabVariations(it, 4)
                }.flatMap {
                    generateRgbVariations(it, 5)
                }.distinct().toList()
        },
        PURPLE to object : VariationProducer {
            override fun createVariations() = ColorClasses.colorRgbByName[PURPLE]!!
                .flatMap {
                    generateCieLabVariations(it, 20)
                }.flatMap {
                    generateRgbVariations(it, 10)
                }.distinct().toList()
        },
        BEIGE to object : VariationProducer {
            override fun createVariations() = ColorClasses.colorRgbByName[BEIGE]!!
                .flatMap {
                    generateCieLabVariations(it, 20)
                }.flatMap {
                    generateRgbVariations(it, 1)
                }.distinct().toList()
        }
    )
    var writeHeader = true
    val csvOutputPath = Paths
        .get(System.getProperty("user.dir"))
        .resolve("src/main/resources/dev/welyab/ai/classification/colornames/color_dataset.csv")
    ColorClasses.colorRgbByName.asSequence().forEachIndexed { index, entry ->
        if (!colorVariationGenerators.containsKey(entry.key)) return@forEachIndexed
        println("${(index + 1)}/${ColorClasses.colorRgbByName.size} - Generating variations for ${entry.key}")
        val variations = colorVariationGenerators[entry.key]!!.createVariations()
        println("${variations.size} colors created for ${entry.key}")
        val imageOutputPath = Paths
            .get(System.getProperty("user.dir"))
            .resolve("src/main/resources/dev/welyab/ai/classification/colornames/sample_images")
            .resolve("${entry.key.toString().lowercase()}.png")
        if (Files.notExists(imageOutputPath.parent)) {
            Files.createDirectories(imageOutputPath.parent)
        }
        println("Creating output files with colors: $imageOutputPath")
        generateImageSample(
            output = imageOutputPath,
            variations = variations,
        )
        println("Adding colors to csv file: $csvOutputPath")
        if (writeHeader) {
            Files.deleteIfExists(csvOutputPath)
            RandomAccessFile(
                csvOutputPath.toFile(),
                "rw"
            ).use { out ->
                out.writeBytes("red,green,blue,name")
                out.writeBytes(System.lineSeparator())
            }
            writeHeader = false
        }
        generateCsv(
            output = csvOutputPath,
            variations = variations.map { it to entry.key }
        )
        println()
    }
}

fun generateCsv(
    output: Path,
    variations: List<Pair<Color, ColorName>>,
) {
    RandomAccessFile(
        output.toFile(),
        "rw"
    ).use { out ->
        out.seek(out.length())
        variations
            .sortedWith { v1, v2 ->
                ComparisonChain
                    .start()
                    .compare(v1.first.red, v2.first.red)
                    .compare(v1.first.green, v2.first.green)
                    .compare(v1.first.blue, v2.first.blue)
                    .result()
            }
            .forEach { color ->
                out.writeBytes("${color.first.red},${color.first.green},${color.first.blue},${color.second.name.lowercase()}")
                out.writeBytes(System.lineSeparator())
            }
    }
}

fun generateImageSample(
    output: Path,
    variations: List<Color>
) {
    val tileSize = 30
    val squareSideColorCount = sqrt(variations.size.toDouble()).toInt()
    val image = BufferedImage(
        tileSize * squareSideColorCount,
        tileSize * squareSideColorCount,
        BufferedImage.TYPE_INT_RGB
    )
    val g = image.graphics as Graphics2D
    val colors = variations.shuffled()
    for (col in 0 until squareSideColorCount) {
        for (row in 0 until squareSideColorCount) {
            val colorIndex = col * squareSideColorCount + row
            val color = colors[colorIndex]
            g.color = color
            g.fillRect(
                col * tileSize,
                row * tileSize,
                tileSize,
                tileSize
            )
        }
    }

    Imaging.writeImage(
        image,
        output.toFile(),
        ImageFormats.PNG
    )
}
