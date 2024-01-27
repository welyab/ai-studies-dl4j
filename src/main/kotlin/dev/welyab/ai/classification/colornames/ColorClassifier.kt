package dev.welyab.ai.classification.colornames

import dev.welyab.ai.classification.colornames.helps.ColorSample
import java.awt.Color
import java.nio.file.Paths
import kotlin.random.Random
import kotlin.random.nextInt
import org.datavec.api.records.metadata.RecordMetaData
import org.datavec.api.records.reader.impl.csv.CSVRecordReader
import org.datavec.api.records.reader.impl.transform.TransformProcessRecordReader
import org.datavec.api.split.FileSplit
import org.datavec.api.transform.TransformProcess
import org.datavec.api.transform.schema.Schema
import org.datavec.api.transform.transform.string.StringMapTransform
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator
import org.deeplearning4j.nn.conf.NeuralNetConfiguration
import org.deeplearning4j.nn.conf.layers.DenseLayer
import org.deeplearning4j.nn.conf.layers.OutputLayer
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.nn.weights.WeightInit
import org.nd4j.evaluation.classification.Evaluation
import org.nd4j.evaluation.meta.Prediction
import org.nd4j.linalg.activations.Activation
import org.nd4j.linalg.cpu.nativecpu.NDArray
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize
import org.nd4j.linalg.learning.config.Sgd
import org.nd4j.linalg.lossfunctions.LossFunctions

class ColorClassification

fun main() {
    val csvReader = CSVRecordReader(1, ',')
        .apply {
            initialize(
                FileSplit(
                    Paths.get(
                        ColorClassification::class.java.getResource(
                            "/dev/welyab/ai/classification/colornames/color_dataset.csv"
                        ).toURI()
                    ).toFile()
                )
            )
        }

    val labelIndex = 3
    val numClasses = 12
    val batchSize = Int.MAX_VALUE

    val schema = Schema
        .Builder()
        .addColumnString("red")
        .addColumnString("green")
        .addColumnString("blue")
        .addColumnString("name")
        .build()

    val transform = TransformProcess
        .Builder(schema)
        .transform(StringMapTransform(
            "name",
            ColorName.values().associate { it.name.lowercase() to it.ordinal.toString() }
        ))
        .build()

    val transformReader = TransformProcessRecordReader(csvReader, transform)

    val iterator = RecordReaderDataSetIterator(transformReader, batchSize, labelIndex, numClasses)
    iterator.isCollectMetaData = true

    val allData = iterator.next()
    allData.shuffle(123)
    val testAndTrain = allData.splitTestAndTrain(0.65)

    val trainingData = testAndTrain.train
    val testData = testAndTrain.test

    val testMetaData = testData.getExampleMetaData(
        RecordMetaData::class.java
    )

    val normalizer: DataNormalization = NormalizerStandardize()
    normalizer.fit(trainingData)
    normalizer.transform(trainingData)
    normalizer.transform(testData)

    val numInputs = 3
    val outputNum = numClasses
    val seed: Long = 6

    val conf = NeuralNetConfiguration.Builder()
        .seed(seed)
        .activation(Activation.TANH)
        .weightInit(WeightInit.XAVIER)
        .updater(Sgd(0.1))
        .l2(1e-4)
        .list()
        .layer(DenseLayer.Builder().nIn(numInputs).nOut(24).activation(Activation.RELU).build())
        .layer(DenseLayer.Builder().nOut(24).activation(Activation.RELU).build())
        .layer(DenseLayer.Builder().nOut(24).activation(Activation.RELU).build())
        .layer(DenseLayer.Builder().nOut(16).activation(Activation.RELU).build())
        .layer(
            OutputLayer
                .Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                .activation(Activation.SOFTMAX)
                .nIn(numInputs)
                .nOut(outputNum)
                .build()
        )
        .build()

    val network = MultiLayerNetwork(conf)
    network.init()

    for (i in 0..3000) {
        network.fit(trainingData)
    }

    val eval = Evaluation(numClasses)
    val output = network.output(testData.features)
    eval.eval(testData.labels, output, testMetaData)

    println(eval.stats())

    val predictionErrors = eval.predictionErrors
    for (p: Prediction in predictionErrors) {
        println(
            "Predicted class: " + p.predictedClass + ", Actual class: " + p.actualClass
                    + "\t" + p.getRecordMetaData(RecordMetaData::class.java).location
        )
    }

    listOf(
        "WHITE" to Color(255, 255, 255),
        "WHITE" to Color(241, 241, 241),
        "WHITE" to Color(255, 249, 243),
        "WHITE" to Color(248, 249, 249),

        "GRAY" to Color(207, 207, 207),
        "GRAY" to Color(171, 171, 171),
        "GRAY" to Color(93, 88, 83),
        "GRAY" to Color(53, 53, 53),

        "BLACK" to Color(16, 16, 15),
        "BLACK" to Color(2, 7, 2),
        "BLACK" to Color(13, 11, 5),
        "BLACK" to Color(8, 4, 0),

        "RED" to Color(234, 28, 28),
        "RED" to Color(202, 28, 0),
        "RED" to Color(232, 63, 63),
        "RED" to Color(162, 15, 15),

        "GREEN" to Color(81, 255, 0),
        "GREEN" to Color(32, 101, 0),
        "GREEN" to Color(18, 57, 0),
        "GREEN" to Color(186, 253, 154),

        "BLUE" to Color(18, 68, 236),
        "BLUE" to Color(34, 58, 139),
        "BLUE" to Color(147, 232, 255),
        "BLUE" to Color(193, 242, 255),

        "ORANGE" to Color(255, 158, 0),
        "ORANGE" to Color(223, 149, 28),
        "ORANGE" to Color(248, 159, 13),
        "ORANGE" to Color(254, 162, 1),

        "YELLOW" to Color(255, 255, 0),
        "YELLOW" to Color(245, 176, 65),
        "YELLOW" to Color(247, 220, 111),
        "YELLOW" to Color(183, 149, 11),

        "PINK" to Color(255, 51, 246),
        "PINK" to Color(234, 120, 229),
        "PINK" to Color(187, 21, 179),
        "PINK" to Color(255, 0, 243),

        "BROWN" to Color(116, 74, 0),
        "BROWN" to Color(160, 122, 55),
        "BROWN" to Color(114, 74, 5),
        "BROWN" to Color(87, 62, 18),

        "PURPLE" to Color(171, 23, 204),
        "PURPLE" to Color(130, 19, 155),
        "PURPLE" to Color(172, 118, 184),
        "PURPLE" to Color(79, 0, 96),

        "BEIGE" to Color(251, 250, 227),
        "BEIGE" to Color(243, 241, 213),
        "BEIGE" to Color(252, 250, 230),
        "BEIGE" to Color(245, 240, 190),
    ).forEachIndexed { index, pair ->
        val rgb = NDArray(1, 3)
        rgb.putScalar(0, 0, pair.second.red.toDouble())
        rgb.putScalar(0, 1, pair.second.green.toDouble())
        rgb.putScalar(0, 2, pair.second.blue.toDouble())
        normalizer.transform(rgb)
        ColorSample.generate(
            pair.second,
            ColorName.values()[network.predict(rgb)[0]].name,
            Paths.get(System.getProperty("user.dir"))
                .resolve("src/main/resources/dev/welyab/ai/classification/colornames/predicted/${pair.first}_${index + 1}.png")
        )
    }

    repeat(10) { index ->
        val color = Color(
            Random.nextInt(256),
            Random.nextInt(256),
            Random.nextInt(256)
        )
        val rgb = NDArray(1, 3)
        rgb.putScalar(0, 0, color.red.toDouble())
        rgb.putScalar(0, 1, color.green.toDouble())
        rgb.putScalar(0, 2, color.blue.toDouble())
        normalizer.transform(rgb)
        ColorSample.generate(
            color,
            ColorName.values()[network.predict(rgb)[0]].name,
            Paths.get(System.getProperty("user.dir"))
                .resolve("src/main/resources/dev/welyab/ai/classification/colornames/predicted/random_${index + 1}.png")
        )
    }
}
