package runner

import ga.GeneticAlgorithm
import ga.not
import ga.probably
import ga.randomNumbers
import javafx.application.Application
import javafx.application.Platform
import javafx.scene.Scene
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.scene.control.Button
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.max


class Main : Application() {

    companion object {
        val TITLE = "EduScheduler"

        @JvmStatic
        fun main(args: Array<String>) {
            Main().run(args)
        }
    }

    fun run(args: Array<String>) {
        launch(*args)
    }

    private lateinit var series: XYChart.Series<Number, Number>

    private val pool: ExecutorService = Executors.newSingleThreadExecutor()

    override fun start(primaryStage: Stage) {
        with(primaryStage) {
            primaryStage.title = TITLE
            val root = VBox()
            initLayout(root)
            scene = Scene(root, 800.0, 550.0)
            show()
        }
    }

    private fun initLayout(root: Pane) {
        root.children.add(createChart())

        val runButton = Button("Schedule")
        root.children.add(runButton)
        runButton.setOnAction {
            pool.submit {
                exec()
            }
        }
    }

    private fun createChart(): LineChart<Number, Number> {
        val xAxis = NumberAxis()
        val yAxis = NumberAxis()
        xAxis.label = "Generation"
        yAxis.label = "Fitness"

        val lineChart = LineChart(xAxis, yAxis)
        lineChart.minHeight = 500.0

        lineChart.title = "Genetic Algorithm"

        series = XYChart.Series()
        lineChart.data.add(series)
        return lineChart
    }

    private fun exec() {

        val taskSize = 25_000

        val ga = GeneticAlgorithm(
                individualGenerator = { randomNumbers(taskSize, 0, 1) },
                fitnessFunc = { it.sum() },
                mutation = { individual -> individual.map { if (probably(0.2)) !it else it } },
                tournamentSize = 500,
                crossoverFunc = { p1, p2 -> (0 until p1.size).map { max(p1[it], p2[it]) } },
                generationListener = { generation, bestOne ->
                    Platform.runLater {
                        series.data.add(XYChart.Data(generation, bestOne.fitness))
                    }
                }
        )
        ga.run(populationSize = 5000,generationAmount = 50)
    }

}
