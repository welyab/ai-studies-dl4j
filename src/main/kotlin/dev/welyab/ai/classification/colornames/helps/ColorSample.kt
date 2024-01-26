package dev.welyab.ai.classification.colornames.helps

import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import javax.imageio.ImageIO

object ColorSample {
    fun generate(color: Color, name: String, outFile: Path) {
        val width = 80
        val height = 20
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val g = image.graphics as Graphics2D
        g.color = color
        g.fillRect(0, 0, width, height)
        g.color = Color(
            255 - color.red,
            255 - color.green,
            255 - color.blue
        ).darker().darker()
        g.drawString(name, 5, 15)
        if (Files.notExists(outFile.parent)) {
            Files.createDirectories(outFile.parent)
        }
        ImageIO.write(image, "png", outFile.toFile())
    }
}
