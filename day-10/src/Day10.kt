import java.io.File
import java.math.BigInteger
import kotlin.test.assertEquals

class Day10 {
    companion object {
        fun parseStr(str: String): List<Int> {
            val adapters = str.trim().split("\n").map { it.trim().toInt() }
            return adapters.sorted()
        }

//        fun getDiffs(
//            ints: List<Int>,
//            start: Int = 0,
//            diffs: Diffs = Diffs(0, 0, 0)
//        ): Diffs {
//            val tail = ints.toMutableList()
//            if (tail.size == 0) return diffs
//
//            val next = tail.removeAt(0)
//            val diff = next - start
//            diffs.incr(diff)
//
//            return getDiffs(tail.toList(), next, diffs)
//        }


        fun countAllWays(
            ints: List<Int>,
            end: Int,
        ): BigInteger {

            println("...")
            println("...")
            println("...")
            println("...")
            println("...")
            val toTry = ints.sorted().toMutableList()


            val result = countAllRecursive(toTry, mutableSetOf(), HashMap())

            return result
        }


        private fun countAllRecursive(
            ints: MutableList<Int>,
            triedNodes: MutableSet<Int>,
            memo: HashMap<Int, BigInteger>
        ): BigInteger {
            val thisNode = ints.removeLastOrNull()
            if (thisNode != null) {
                if (triedNodes.isEmpty()) {
                    memo[thisNode] = 1.toBigInteger()
                    triedNodes.add(thisNode)
                    return countAllRecursive(ints, triedNodes, memo)
                } else {
                    val possibleNodes = triedNodes.filter { it > thisNode && it <= thisNode + 3 }
                    memo[thisNode] = 0.toBigInteger()
                    for (node in possibleNodes) {
                        memo[thisNode] = memo[thisNode]!! + memo[node]!!
                    }
                    triedNodes.add(thisNode)
                    return countAllRecursive(ints, triedNodes, memo)
                }
            }
            return memo[triedNodes.minOrNull()!!]!!
        }

    }
}


data class Diffs(var one: Int, var two: Int, var three: Int) {
    fun incr(int: Int) {
        when (int) {
            1 -> this.one = this.one + 1
            2 -> this.two = this.two + 1
            3 -> this.three = this.three + 1
            else -> throw RuntimeException("$int")
        }
    }

}


fun main() {
    runTests()

    val input = Day10.parseStr("""
        ${File("${System.getProperty("user.dir")}/day-10/src/input.txt")
            .readText()}
            """.trimIndent()
    )
    val countInput = input.union(listOf(0)).sorted()
    println("" + Day10.countAllWays(countInput, countInput.max()!! + 3))

//    val diffs = Day10.getDiffs(input.union(listOf(input.last() + 3)).sorted())
//    println("The 1x3 product of diffs $diffs is ${diffs.one * diffs.three}")
//
//    val counter = Counter(input)
//    println("There are ${counter.countWays()} ways to arrange this")

}


fun runTests() {


//    assertEquals(4, Day10.countAllWays(listOf(1, 2, 3)))


    val input1 = Day10.parseStr(
        """
        16
        10
        15
        5
        1
        11
        7
        19
        6
        12
        4
    """.trimIndent()
    )


//    println(input1)

    assertEquals(8.toBigInteger(), Day10.countAllWays(input1, (input1.maxOrNull() ?: 0) + 3))

//    assertEquals(Diffs(0, 0, 0), Day10.getDiffs(emptyList()))
//    assertEquals(Diffs(7, 0, 5), Day10.getDiffs(input1))


    val input2 = Day10.parseStr(
        """
        0
        28
        33
        18
        42
        31
        14
        46
        20
        48
        47
        24
        23
        49
        45
        19
        38
        39
        11
        1
        32
        25
        35
        8
        17
        7
        9
        4
        2
        34
        10
        3
    """.trimIndent()
    )

//    assertEquals(Diffs(22, 0, 10), Day10.getDiffs(input2))
//    assertEquals(Diffs(22, 0, 10), Day10.getDiffs(input2))
    assertEquals(19208.toBigInteger(), Day10.countAllWays(input2, input2.max()!! + 3))

}


