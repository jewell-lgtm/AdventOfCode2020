import java.io.File
import kotlin.test.assertEquals


class Day1(private val target: Int, private val expenseReport: List<Int>) {

    fun answer(): Pair<Int, Int> {
        for (first in expenseReport) {
            val second = expenseReport.firstOrNull { it + first == target }
            if (second != null) {
                return Pair(first, second)
            }
        }
        return Pair(-1, -1)
    }

}


fun main(args: Array<String>) {
    val testReport = listOf(1721, 979, 366, 299, 675, 1456)
    val test = Day1(2020, testReport)
    val testAnswer = test.answer()

    assertEquals(2020, testAnswer.first + testAnswer.second)

    val txtReport = File("${System.getProperty("user.dir")}/day-1/src/report.txt")
            .useLines { line -> line.toList().map { it.toInt() } }
    val day1 = Day1(2020, txtReport)
    val day1Answer = day1.answer()

    assertEquals(2020, day1Answer.first + day1Answer.second)

    println(day1Answer)

}