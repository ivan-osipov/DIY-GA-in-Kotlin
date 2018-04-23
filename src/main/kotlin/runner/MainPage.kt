package runner

import ga.GeneticAlgorithm
import ga.not
import javafx.concurrent.Task
import javafx.geometry.Pos
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.scene.layout.Priority
import model.*
import tornadofx.*
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.max

class MainPage : View("Genetics Experiments") {
    override val root = vbox {
        minWidth = 800.0
        minHeight = 600.0
        tabpane {
            tab<ChartTab>()
            tab<ScheduleTab>()
        }
    }
}

class ScheduleTab : View("Schedule") {

    override val root = vbox {
        gridpane {
            isGridLinesVisible = true
            val schedule = Matrix<Event?>(DAYS, LESSONS) { _, _ -> null }
            row {
                arrayOf("", "ПН", "ВТ", "СР", "ЧТ", "ПТ", "СБ").forEach {
                    label(it) {
                        gridpaneColumnConstraints {
                            hgrow = Priority.ALWAYS
                        }
                    }
                }
            }
            var time = LocalTime.of(8, 0)
            schedule.T.withIndex().forEach { indexedDays ->
                val eventsByDays = indexedDays.value
                row {
                    label(DateTimeFormatter.ofPattern("HH:mm").format(time)) {
                        gridpaneColumnConstraints {
                            hgrow = Priority.ALWAYS
                            alignment = Pos.CENTER
                        }
                    }
                    time = time.plusHours(1)
                    for (event in eventsByDays) {
                        label(event.toGridView()) {
                            gridpaneColumnConstraints {
                                hgrow = Priority.ALWAYS
                                alignment = Pos.CENTER
                            }
                        }
                    }
                }
            }
        }
    }

    private fun Event?.toGridView() = this?.let {
        "${it.teacher} - ${it.group}"
    } ?: "---"
}

class ChartTab : View("Chart") {

    val pool: ExecutorService = Executors.newSingleThreadExecutor()

    private lateinit var series: XYChart.Series<Number, Number>

    override val root = vbox {
        linechart("Genetic Algorithm", NumberAxis().apply { label = "Generation" }, NumberAxis().apply { label = "Fitness" }) {
            minHeight = 500.0
            series = series("GA")
        }
        button("Run") {
            action {
                pool.submit(SchedulingTask(TaskConfig(
                        data = createData(),
                        evolutionListener = { generation, gaResult ->
                            series.data.add(XYChart.Data(generation,gaResult.fitness))
                        }
                )))

            }
        }
    }
}

class SchedulingTask(private val taskConfig: TaskConfig) : Task<Schedule>() {
    override fun call(): Schedule {

        val ga = GeneticAlgorithm(
                individualGenerator = {
                    TODO()
                },
                fitnessFunc = { it.sum() },
                mutation = { gene -> !gene },
                mutationProb = taskConfig.mutationProb,
                tournamentSize = taskConfig.tournamentSize,
                crossoverFunc = { p1, p2 -> (0 until p1.size).map { max(p1[it], p2[it]) } },
                crossoverProb = taskConfig.crossoverProb,
                generationListener = taskConfig.evolutionListener
        )
        val gaResult = ga.run(taskConfig.populationSize, taskConfig.generationAmount)

        return gaResult.toSchedule()
    }

}

class TaskConfig(val populationSize: Int = 50,
                 val generationAmount: Int = 50,
                 val tournamentSize: Int = 500,
                 val mutationProb: Double = 0.2,
                 val crossoverProb: Double = 0.3,
                 val data: Data,
                 val evolutionListener: (Int, GeneticAlgorithm.GAResult) -> Unit = { _, _ -> })

private fun GeneticAlgorithm.GAResult.toSchedule(): Schedule {
    val rawSchedule = this.individual

    TODO()
}
