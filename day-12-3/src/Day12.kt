import java.io.File
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.test.assertEquals

class Day12 {
    companion object {
        fun createFerry(): Ferry {
            return Ferry()
        }

        fun parseInput(input: String): List<String> {
            return input.trim().split("\n").map { it.trim() }.filter { it.isNotEmpty() }
        }
    }
}

fun main() {
    runTests()

    val input = File("${System.getProperty("user.dir")}/day-12-3/src/input.txt")
        .readText()
    val instructions = Day12.parseInput(input)
    val part1 = Day12.createFerry()
    instructions.forEach { part1.move(it) }
    println("the distance is ${part1.manhattanDistance}")
}


class Ferry(
    var heading: Direction = Direction.East,
    var east: Int = 0,
    var south: Int = 0
) {
    val manhattanDistance: Int
        get() = east.absoluteValue + south.absoluteValue

    val moveInstruction = Regex("^([NSEWLRF])(\\d+)$")
    fun move(where: String) {
        val instruction = moveInstruction.matchEntire(where)
        val direction = instruction!!.groups[1]!!.value
        val amount = instruction.groups[2]!!.value.toInt()

        when (direction) {
            "F" -> head(heading, amount)
            "N" -> head(Direction.North, amount)
            "S" -> head(Direction.South, amount)
            "E" -> head(Direction.East, amount)
            "W" -> head(Direction.West, amount)
            "R" -> turn(Clock.Clockwise, amount)
            "L" -> turn(Clock.Anticlockwise, amount)
            else -> throw RuntimeException("Unexpected direction $direction")
        }
    }

    private fun head(heading: Direction, amount: Int) {
        when (heading) {
            Direction.North -> south -= amount
            Direction.South -> south += amount
            Direction.West -> east -= amount
            Direction.East -> east += amount
        }
    }

    val cardinal = listOf(Direction.East, Direction.South, Direction.West, Direction.North)
    private fun turn(direction: Clock, amount: Int) {
        val delta = amount / if (direction == Clock.Clockwise) 90 else -90
        val currIndex = cardinal.indexOf(heading)
        heading = cardinal[(currIndex + delta + 4) % 4]
    }
}

enum class Clock {
    Clockwise, Anticlockwise
}

enum class Direction {
    North, South, East, West
}


fun runTests() {
    val ferry = Day12.createFerry()
    assertEquals(0, ferry.east)
    assertEquals(0, ferry.south)
    assertEquals(Direction.East, ferry.heading)

    ferry.move("F10")
    assertEquals(10, ferry.east)
    assertEquals(0, ferry.south)
    assertEquals(Direction.East, ferry.heading)

    ferry.move("N3")
    assertEquals(10, ferry.east)
    assertEquals(-3, ferry.south)
    assertEquals(Direction.East, ferry.heading)

    ferry.move("R90")
    assertEquals(10, ferry.east)
    assertEquals(-3, ferry.south)
    assertEquals(Direction.South, ferry.heading)


    ferry.move("L180")
    assertEquals(10, ferry.east)
    assertEquals(-3, ferry.south)
    assertEquals(Direction.North, ferry.heading)

    ferry.move("L180")
    assertEquals(10, ferry.east)
    assertEquals(-3, ferry.south)
    assertEquals(Direction.South, ferry.heading)


    val instructions = Day12.parseInput(
        """
            F10
            N3
            F7
            R90
            F11 
        """.trimIndent()
    )

    val ferry2 = Day12.createFerry()
    for (instruction in instructions) {
        ferry2.move(instruction)
    }
    assertEquals(Direction.South, ferry2.heading)
    assertEquals(17, ferry2.east)
    assertEquals(8, ferry2.south)
    assertEquals(25, ferry2.manhattanDistance)
}