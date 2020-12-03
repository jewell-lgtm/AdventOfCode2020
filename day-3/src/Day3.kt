import Day3.Row
import java.io.File
import kotlin.test.assertEquals

class Day3(val rows: List<Row>, var turn: Int, val vel: Vector) {
    companion object {
        fun part1(strs: List<String>): Day3 {
            return Day3.create(strs, Vector(3, 1))
        }

        fun create(strs: List<String>, vel: Vector): Day3 {
            val rows = strs.mapIndexed { i, str -> Row(str, i) }
            return Day3(rows, 0, vel)
        }
    }


    open class Row(val str: String, val y: Int) {
        open fun atX(x: Int): Square = if (str[x % str.length] == '.') Square.SNOW else Square.TREE

        class SnowyRow : Row("", -1) {
            override fun atX(x: Int) = Square.SNOW
        }
    }

    enum class Square {
        SNOW, TREE
    }

    data class Point(val x: Int, val y: Int)

    data class Vector(val x: Int, val y: Int)

    fun move() {
        turn += 1
    }

    val complete: Boolean
        get() = turn >= rows.size

    val position: Point
        get() = Point(turn * vel.x, turn * vel.y)

    val square: Square
        get() = currentRow.atX(position.x)

    val currentRow: Row
        get() = rows.getOrElse(position.y) { Row.SnowyRow() }


}

fun main(args: Array<String>) {
    runTests()

    val txtReport = File("${System.getProperty("user.dir")}/day-3/src/input.txt")
        .useLines { it.toList() }

    println("Part 1: Landed on ${countTrees(Day3.part1(txtReport))} trees")

    val counts: List<Int> = listOf(
        Day3.Vector(1, 1),
        Day3.Vector(3, 1),
        Day3.Vector(5, 1),
        Day3.Vector(7, 1),
        Day3.Vector(1, 2)
    ).map { countTrees(Day3.create(txtReport, it)) }
    val product = counts.reduce { acc, i -> acc * i }
    println("We landed on these trees: $counts, giving a product of $product")

}

private fun countTrees(game: Day3): Int {
    var i = 0 // I'm afraid of infinity
    var trees = 0
    while (!game.complete && i++ < 1000) {
        if (game.square == Day3.Square.TREE) {
            trees++
        }
        game.move()
    }

    return trees
}


fun runTests() {

    val row = Row(".#.", 0)
    assertEquals(Day3.Square.SNOW, row.atX(0))
    assertEquals(Day3.Square.TREE, row.atX(1))
    assertEquals(Day3.Square.SNOW, row.atX(2))
    assertEquals(Day3.Square.SNOW, row.atX(3))
    assertEquals(Day3.Square.TREE, row.atX(4))
    assertEquals(Day3.Square.SNOW, row.atX(5))

    val test = Day3.create(listOf("....", "...#"), Day3.Vector(3, 1))
    assertEquals(Day3.Point(0, 0), test.position)
    assertEquals(Day3.Square.SNOW, test.square);
    assertEquals(false, test.complete)
    test.move()
    assertEquals(Day3.Point(3, 1), test.position)
    assertEquals(Day3.Square.TREE, test.square);
    assertEquals(false, test.complete)
    test.move()
    assertEquals(true, test.complete)


}
/**

val testInput = listOf(
"1-3 a: abcde",
"1-3 b: cdefg",
"2-9 c: ccccccccc"
)
val rules = Day2.parseInput(testInput)
println("According to test data ${rules.count { it.rule1Valid }} are valid")


val out = Day2.parseInput(txtReport)

println("According to rule 1: ${out.count { it.rule1Valid }} are valid")
println("According to rule 2: ${out.count { it.rule2Valid }} are valid")
}
 */