package ga

import kotlinx.coroutines.experimental.runBlocking
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList
import kotlin.math.max
import kotlin.math.sqrt

val random = Random(0)

fun main(args: Array<String>) = runBlocking {
    val taskSize = 500

    val ga = GeneticAlgorithm(
            individualGenerator = {
                randomNumbers(taskSize, 0, 1)
            },
            fitnessFunc = { it.sum() },
            mutation = { gene -> !gene },
            mutationProb = 0.2,
            tournamentSize = 25,
            crossoverFunc = { p1, p2 -> (0 until p1.size).map { max(p1[it], p2[it]) } },
            crossoverProb = 0.3
    )
    val result = ga.run(50, 50)

    print("Value: %s\nFitness: %s".format(result.individual, result.fitness))
}

operator fun Int.not(): Int {
    return if (this == 1) 0
    else 1
}

typealias Chromosome = List<Gene>

typealias Individual = Chromosome

typealias Gene = Int

class GeneticAlgorithm(private val individualGenerator: () -> Individual,
                       private val fitnessFunc: (Individual) -> Int,
                       private val mutation: (Gene) -> Gene,
                       private val mutationProb: Double,
                       private val tournamentSize: Int,
                       private val crossoverFunc: (Individual, Individual) -> Individual,
                       private val crossoverProb: Double,
                       private val generationListener: (Int, GAResult) -> Unit = { _, _ -> }) {

    fun run(populationSize: Int, generationAmount: Int): GAResult {

        var currentGeneration: List<Individual> = ArrayList()

        var bestResult: GAResult? = null

        for (generationNumber in (0 until generationAmount)) {
            val requiredIndividualAmount = populationSize - currentGeneration.size

            val generation = (0 until requiredIndividualAmount).asSequence()
                    .map { individualGenerator() }
                    .toList()
            currentGeneration += generation

            val currentBestResult = currentGeneration.map { GAResult(it to fitnessFunc(it)) }.maxBy { it.fitness }!!
            if (bestResult == null || currentBestResult.fitness > bestResult.fitness) {
                bestResult = currentBestResult
            }
            generationListener(generationNumber, bestResult)

            //mutation
            currentGeneration = currentGeneration.map {
                if (probably(sqrt(mutationProb))) {
                    return@map it.map { if(probably(sqrt(mutationProb))) mutation(it) else it }
                } else it
            } as MutableList<Individual>

            val results = currentGeneration.asSequence()
                    .map { it to fitnessFunc(it) }
                    .map { GAResult(it) }
                    .toList().shuffled(random)

            //selection
            val selectedOnes = results.chunked(tournamentSize)
                    .map {
                        it.maxBy(GAResult::fitness)!!
                    }.map(GAResult::individual)
            currentGeneration = selectedOnes

            //crossover
            currentGeneration = currentGeneration.chunked(2) {
                if (it.size == 1 || probably(crossoverProb)) {
                    it
                } else {
                    val (parent1, parent2) = it
                    listOf(crossoverFunc(parent1, parent2))
                }
            }.flatten()
        }

        return bestResult!!
    }

    inner class GAResult(pair: Pair<Individual, Int>) {
        val individual: Individual = pair.first
        val fitness: Int = pair.second
    }
}

fun probably(probability: Double): Boolean {
    return random.nextDouble() < probability
}

fun randomNumbers(amount: Int, min: Int, max: Int): List<Int> =
        random.ints(amount.toLong(), min, max + 1).boxed().collect(Collectors.toList())!!

