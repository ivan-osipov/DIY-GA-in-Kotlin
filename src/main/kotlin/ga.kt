import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList
import kotlin.math.max

val random = Random(0)

fun main(args: Array<String>) {
    val taskSize = 100L

    val ga = GeneticAlgorithm(
            individualGenerator = { random.ints(taskSize, 0, 2).boxed().collect(Collectors.toList()) },
            fitnessFunc = { it.sum().toDouble() },
            mutation = { individual -> individual.map { if(random.nextInt(100) < 10) !it else it } },
            tournamentSize = 10,
            crossoverFunc = { p1: Individual, p2: Individual ->
//                val border = random.nextInt(p1.size)
//                val child: List<Int> = p1.subList(0, border) + p2.subList(border, p2.size)
                val child = (0 until p1.size).map { max(p1[it], p2[it]) }
                child
            }
    )
    val result = ga.run()
    print("Fitness: %s Value: %s".format(result.fitness, result.individual))
}

operator fun Int.not(): Int {
    return if(this == 1) 0
    else 1
}

typealias Chromosome = List<Int>

typealias Individual = Chromosome

class GeneticAlgorithm(private val individualGenerator: () -> Individual,
                       private val fitnessFunc: (Individual) -> Double,
                       private val mutation: (Individual) -> Individual,
                       private val tournamentSize: Int,
                       private val crossoverFunc: (Individual, Individual) -> Individual) {

    fun run(populationSize: Int = 50, generationAmount: Int = 1000): GAResult {

        var currentGeneration: List<Individual> = ArrayList()
        var requiredIndividualAmount: Int

        for (generationNumber in (0 until generationAmount)) {
            requiredIndividualAmount = populationSize - currentGeneration.size

            val generation = (0 until requiredIndividualAmount).asSequence()
                    .map { individualGenerator() }
                    .toList()
            currentGeneration += generation

            //mutation
            currentGeneration = currentGeneration.map {
                if(random.nextInt(100) < 20) {
                    mutation(it)
                } else it
            }

            val results = currentGeneration.asSequence()
                    .map { it to fitnessFunc(it) }
                    .map { GAResult(it) }
                    .toList().shuffled()

            //selection
            val selectedOnes = results.chunked(tournamentSize)
                    .map {
                        return it.maxBy(GAResult::fitness)!!
                    }.map(GAResult::individual)
            currentGeneration = selectedOnes

            //crossover
            currentGeneration = currentGeneration.chunked(2) {
                val (parent1, parent2) = it
                if(random.nextInt(100) < 60) {
                    it
                } else {
                    listOf(crossoverFunc(parent1, parent2))
                }
            }.flatten()
        }

        return currentGeneration.map { GAResult(it to fitnessFunc(it)) }.maxBy { it.fitness }!!
    }

    inner class GAResult(pair: Pair<Individual, Double>) {
        val individual: Individual = pair.first
        val fitness: Double = pair.second
    }
}

