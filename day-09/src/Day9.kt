import java.io.File
import java.math.BigInteger
import kotlin.test.assertEquals

class Day9 {
}

fun main() {
    runTests()

    val input = File("${System.getProperty("user.dir")}/day-09/src/input.txt")
        .readText().trim().split("\n").map {
            try {
                it.toBigInteger()
            } catch (e: Exception) {
                println(it)
                throw e
            }
        }

    val first = findFirst(25, input)
    val nums = contigiousSum(first!!, input)!!.sorted()

    println("Found the non matching number: $first")
    println("Found the pair marking contigious block ${nums.first() + nums.last()}")
}

fun contigiousSum(first: BigInteger, input: List<BigInteger>): List<BigInteger>? {
    input.forEachIndexed { index, _ ->
        var i = index
        val contigious: MutableList<BigInteger> = mutableListOf()
        while (contigious.sum() < first && i < input.size) {
            contigious.add(input[i])
            i += 1
        }
        if (contigious.sum() == first) {
            return contigious
        }
    }

    return null
}

fun Iterable<BigInteger>.sum(): BigInteger {
    var sum: BigInteger = 0.toBigInteger()
    for (element in this) {
        sum += element
    }
    return sum
}


fun runTests() {
    var input = ("35\n" +
            "20\n" +
            "15\n" +
            "25\n" +
            "47\n" +
            "40\n" +
            "62\n" +
            "55\n" +
            "65\n" +
            "95\n" +
            "102\n" +
            "117\n" +
            "150\n" +
            "182\n" +
            "127\n" +
            "219\n" +
            "299\n" +
            "277\n" +
            "309\n" +
            "576").split("\n").map { it.toBigInteger() }

    assertEquals(127.toBigInteger(), findFirst(5, input))

}

fun findFirst(preamble: Int, input: List<BigInteger>): BigInteger? {
    for (i in (preamble) until input.size) {
        val int = input[i]
        val preambleNumbers = input.subList(i - preamble, i)

        val pairs = preambleNumbers.product().filter { it.first != it.second }

        val sums = pairs.map { it.first + it.second }

        sums.firstOrNull { it == int } ?: return int
    }

    return null
}


private fun <E> List<E>.product() = sequence {
    forEachIndexed { i, first ->
        subList(i, size).forEach { second ->
            yield(first to second)
        }
    }
}

