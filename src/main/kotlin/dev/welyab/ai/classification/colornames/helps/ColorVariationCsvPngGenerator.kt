package dev.welyab.ai.classification.colornames.helps

import com.google.common.collect.ComparisonChain
import dev.welyab.ai.classification.colornames.ColorClasses
import dev.welyab.ai.classification.colornames.ColorName
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
        ColorName.WHITE to object : VariationProducer {
            override fun createVariations() = ColorClasses.colorRgbByName[ColorName.WHITE]!!
                .flatMap {
                    generateCieLabVariations(it, 100)
                }.flatMap {
                    generateRgbVariations(it, 30)
                }.distinct().toList()
        },
        ColorName.BLACK to object : VariationProducer {
            override fun createVariations() = ColorClasses.colorRgbByName[ColorName.BLACK]!!
                .flatMap {
                    generateCieLabVariations(it, 100)
                }.flatMap {
                    generateRgbVariations(it, 30)
                }.distinct().toList()
        },
        ColorName.GRAY to object : VariationProducer {
            override fun createVariations() = ColorClasses.colorRgbByName[ColorName.GRAY]!!
                .flatMap {
                    generateCieLabVariations(it, 100)
                }.flatMap {
                    generateRgbVariations(it, 30)
                }.distinct().toList()
        },
        ColorName.RED to object : VariationProducer {
            override fun createVariations() = ColorClasses.colorRgbByName[ColorName.RED]!!
                .flatMap {
                    generateCieLabVariations(it, 100)
                }.flatMap {
                    generateRgbVariations(it, 30)
                }.distinct().toList()
        },
        ColorName.GREEN to object : VariationProducer {
            override fun createVariations() = ColorClasses.colorRgbByName[ColorName.GREEN]!!
                .flatMap {
                    generateCieLabVariations(it, 100)
                }.flatMap {
                    generateRgbVariations(it, 30)
                }.distinct().toList()
        },
        ColorName.BLUE to object : VariationProducer {
            override fun createVariations() = ColorClasses.colorRgbByName[ColorName.BLUE]!!
                .flatMap {
                    generateCieLabVariations(it, 100)
                }.flatMap {
                    generateRgbVariations(it, 30)
                }.distinct().toList()
        },
        ColorName.ORANGE to object : VariationProducer {
            override fun createVariations() = ColorClasses.colorRgbByName[ColorName.ORANGE]!!
                .flatMap {
                    generateCieLabVariations(it, 100)
                }.flatMap {
                    generateRgbVariations(it, 30)
                }.distinct().toList()
        },
        ColorName.PINK to object : VariationProducer {
            override fun createVariations() = ColorClasses.colorRgbByName[ColorName.PINK]!!
                .flatMap {
                    generateCieLabVariations(it, 100)
                }.flatMap {
                    generateRgbVariations(it, 30)
                }.distinct().toList()
        },
        ColorName.YELLOW to object : VariationProducer {
            override fun createVariations() = ColorClasses.colorRgbByName[ColorName.YELLOW]!!
                .flatMap {
                    generateCieLabVariations(it, 100)
                }.flatMap {
                    generateRgbVariations(it, 30)
                }.distinct().toList()
        },
        ColorName.BROWN to object : VariationProducer {
            override fun createVariations() = ColorClasses.colorRgbByName[ColorName.BROWN]!!
                .flatMap {
                    generateCieLabVariations(it, 100)
                }.flatMap {
                    generateRgbVariations(it, 30)
                }.distinct().toList()
        },
        ColorName.PURPLE to object : VariationProducer {
            override fun createVariations() = ColorClasses.colorRgbByName[ColorName.PURPLE]!!
                .flatMap {
                    generateCieLabVariations(it, 100)
                }.flatMap {
                    generateRgbVariations(it, 30)
                }.distinct().toList()
        },
        ColorName.BEIGE to object : VariationProducer {
            override fun createVariations() = ColorClasses.colorRgbByName[ColorName.BEIGE]!!
                .flatMap {
                    generateCieLabVariations(it, 100)
                }.flatMap {
                    generateRgbVariations(it, 30)
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
