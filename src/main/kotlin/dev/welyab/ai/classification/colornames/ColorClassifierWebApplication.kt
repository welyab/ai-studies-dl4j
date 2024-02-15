package dev.welyab.ai.classification.colornames

import io.javalin.Javalin
import io.javalin.http.ContentType.TEXT_PLAIN
import java.awt.Color

fun main() {
    getModel(true).run {
        Javalin.create()
            .start("0.0.0.0", 8080)
            .apply {
                get("color-name") { ctx ->
                    ctx.contentType(TEXT_PLAIN)
                    ctx.result(
                        predict(
                            Color(
                                ctx.queryParam("red")!!.toInt(),
                                ctx.queryParam("green")!!.toInt(),
                                ctx.queryParam("blue")!!.toInt()
                            )
                        ).name
                    )
                }
            }
    }
}
