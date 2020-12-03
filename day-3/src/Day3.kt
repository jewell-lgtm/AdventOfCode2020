import Day3.Row
import java.io.File
import kotlin.test.assertEquals

class Day3(val rows: List<Row>, var turn: Int) {
    var complete = false

    val position: Point
        get() = Point(turn * 3, turn * 1)

    companion object {
        fun createFromStrs(strs: List<String>): Day3 {
            val rows = strs.mapIndexed { i, str -> Row(str, i) }
            return Day3(rows, 0)
        }
    }


    class Row(val str: String, val y: Int) {
        fun atX(x: Int): Square = if (str[x % str.length] == '.') Square.SNOW else Square.TREE
    }

    enum class Square {
        SNOW, TREE
    }

    data class Point(val x: Int, val y: Int) {
        override fun toString(): String {
            return "(x=$x, y=$y)"
        }
    }

    fun move() {
        if (turn + 1 == rows.size) {
            complete = true
        }

        turn += 1
    }
    val square: Square
        get() = currentRow.atX(position.x )

    val currentRow: Row
        get() = rows[position.y ]
}

fun main(args: Array<String>) {

    runTests()

    val txtReport = File("${System.getProperty("user.dir")}/day-3/src/input.txt")
        .useLines { it.toList() }

    val part1 = Day3.createFromStrs(txtReport)

    var i = 0
    var trees = 0
    while(!part1.complete && i++ < 10000) {
        println("$part1.turn: I am at position ${part1.position} and I am on a ${part1.square} square")
        if (part1.square == Day3.Square.TREE) {
            trees++
        }
        part1.move()
    }
    println("Landed on $trees trees")
}


fun runTests() {

    val row = Row(".#.", 0)
    assertEquals(Day3.Square.SNOW, row.atX(0))
    assertEquals(Day3.Square.TREE, row.atX(1))
    assertEquals(Day3.Square.SNOW, row.atX(2))
    assertEquals(Day3.Square.SNOW, row.atX(3))
    assertEquals(Day3.Square.TREE, row.atX(4))
    assertEquals(Day3.Square.SNOW, row.atX(5))

    val test = Day3.createFromStrs(listOf("....", "...#"))
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