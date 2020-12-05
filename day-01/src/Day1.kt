import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


class Day1(private val target: Int, expenseReport: List<Int>) {

    private val expenseReport: List<Int>

    init {
        this.expenseReport = expenseReport.sorted()
    }

    fun answer2() = product(expenseReport).firstOrNull { it.first + it.second == target }
    fun answer3() = triple(expenseReport).firstOrNull { it.first + it.second + it.third == target }

    private fun product(list: List<Int>): Sequence<Pair<Int, Int>> = sequence {
        list.forEachIndexed { i, first ->
            // TODO: implement binary search here
            list.subList(i, list.size - 1).forEach { second ->
                yield(first to second)
            }
        }
    }

    private fun triple(list: List<Int>): Sequence<Triple<Int, Int, Int>> = sequence {
        list.forEachIndexed { i, first ->
            list.subList(i, list.size - 1).forEach { second ->
                list.subList(i + 1, list.size - 1).forEach { third ->
                    yield(Triple(first, second, third))
                }
            }
        }
    }


}


fun main(args: Array<String>) {
    val testReport = listOf(1721, 979, 366, 299, 675, 1456)
    val test = Day1(2020, testReport)
    val testAnswer = test.answer2()

    assertNotNull(testAnswer)
    assertEquals(2020, testAnswer.first + testAnswer.second)
//
    val txtReport = File("${System.getProperty("user.dir")}/day-1/src/report.txt")
            .useLines { line -> line.toList().map { it.toInt() } }
    val day1 = Day1(2020, txtReport)
    val part1Answer = day1.answer2()
    val part2Answer = day1.answer3()

    assertNotNull(part1Answer)
    assertEquals(2020, part1Answer.first + part1Answer.second)

    assertNotNull(part2Answer)
    assertEquals(2020, part2Answer.first + part2Answer.second + part2Answer.third)


    println("Part one: ${part1Answer.first * part1Answer.second}")
    println("Part two: ${part2Answer.first * part2Answer.second * part2Answer.third}")

}
