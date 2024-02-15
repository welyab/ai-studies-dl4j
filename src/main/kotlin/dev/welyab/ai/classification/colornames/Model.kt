package dev.welyab.ai.classification.colornames

import java.awt.Color
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.nd4j.linalg.cpu.nativecpu.NDArray
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize

class Model(
    val network: MultiLayerNetwork,
    val normalizer: NormalizerStandardize
) {
    fun predict(color: Color): ColorName {
        val rgb = NDArray(1, 3)
        rgb.putScalar(0, 0, color.red.toDouble())
        rgb.putScalar(0, 1, color.green.toDouble())
        rgb.putScalar(0, 2, color.blue.toDouble())
        normalizer.transform(rgb)
        return ColorName.values()[network.predict(rgb)[0]]
    }
}
