import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


class Day1(private val target: Int, private val expenseReport: List<Int>) {

    fun answer() = product(expenseReport).firstOrNull { it.first + it.second == target }

    private fun product(list: List<Int>): Sequence<Pair<Int,Int>> = sequence {
        list.forEachIndexed { i, first ->
            list.reversed().subList(i, list.size -1).forEach { second ->
                yield(first to second)
            }
        }
    }

}




fun main(args: Array<String>) {
    val testReport = listOf(1721, 979, 366, 299, 675, 1456)
    val test = Day1(2020, testReport)
    val testAnswer = test.answer()

    assertNotNull(testAnswer)
    assertEquals(2020, testAnswer.first + testAnswer.second)

    val txtReport = File("${System.getProperty("user.dir")}/day-1/src/report.txt")
            .useLines { line -> line.toList().map { it.toInt() } }
    val day1 = Day1(2020, txtReport)
    val day1Answer = day1.answer()

    assertNotNull(day1Answer)
    assertEquals(2020, day1Answer.first + day1Answer.second)


    println(day1Answer)

}
