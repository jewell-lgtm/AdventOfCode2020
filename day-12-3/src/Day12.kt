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
    runTests2()

    val input = File("${System.getProperty("user.dir")}/day-12-3/src/input.txt")
        .readText()
    val instructions = Day12.parseInput(input)

    val part1 = Day12.createFerry()
    instructions.forEach { part1.move(it) }
    println("Part 1: the distance is ${part1.manhattanDistance}")

    val part2 = Day12.createFerry()
    instructions.forEach { part2.moveWaypoint(it) }
    println("Part 2: the distance is ${part2.manhattanDistance}")
}

open class Coord(var east: Int, var south: Int) {
    val west get() = -1 * east
    val north get() = -1 * south

    val manhattanDistance: Int
        get() = east.absoluteValue + south.absoluteValue
}

class Ferry(
    var heading: Direction = Direction.East,
    var waypoint: Coord = Coord(10, -1),
    east: Int = 0,
    south: Int = 0,
): Coord(east, south) {
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

    fun moveWaypoint(where: String) {
        val instruction = moveInstruction.matchEntire(where)
        val direction = instruction!!.groups[1]!!.value
        val amount = instruction.groups[2]!!.value.toInt()

        when (direction) {
            "F" -> headWaypoint(amount)
            "N" -> relocateWaypoint(Direction.North, amount)
            "S" -> relocateWaypoint(Direction.South, amount)
            "E" -> relocateWaypoint(Direction.East, amount)
            "W" -> relocateWaypoint(Direction.West, amount)
            "R" -> turnWaypoint(Clock.Clockwise, amount)
            "L" -> turnWaypoint(Clock.Anticlockwise, amount)
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

    private fun headWaypoint(amount: Int) {
        val east = waypoint.east * amount
        val south = waypoint.south * amount
        this.east += east
        this.south += south
    }

    private fun turnWaypoint(clockwise: Clock, amount: Int) {
        val actualAmount = if (clockwise.equals(Clock.Clockwise)) amount else 360 + (-1 * amount) % 360

        waypoint = when(actualAmount) {
            90 -> Coord(-1 * waypoint.south, waypoint.east)
            180 -> Coord(-1 * waypoint.east, -1 * waypoint.south )
            270 -> Coord(waypoint.south, -1 * waypoint.east )
            else -> TODO("Dont know how to turn ${amount}")
        }
    }


    private fun relocateWaypoint(direction: Direction, amount: Int) {
        when(direction) {
            Direction.North -> this.waypoint.south -= amount
            Direction.South -> this.waypoint.south += amount
            Direction.West -> this.waypoint.east -= amount
            Direction.East -> this.waypoint.east += amount
        }
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

fun runTests2() {
//    val instructions = listOf(
//        "F10",
//        "N3",
//        "F7",
//        "R90",
//        "F11"
//    )

    val ferry = Day12.createFerry()
    assertEquals(10, ferry.waypoint.east)
    assertEquals(1, ferry.waypoint.north)

    ferry.moveWaypoint("F10")
    assertEquals(100, ferry.east)
    assertEquals(10, ferry.north)

    ferry.moveWaypoint("N3")
    assertEquals(100, ferry.east)
    assertEquals(10, ferry.north)
    assertEquals(10, ferry.waypoint.east)
    assertEquals(4, ferry.waypoint.north)

    ferry.moveWaypoint("F7")
    assertEquals(170, ferry.east)
    assertEquals(38, ferry.north)

    ferry.moveWaypoint("R90")
    assertEquals(4, ferry.waypoint.east)
    assertEquals(10, ferry.waypoint.south)

    ferry.moveWaypoint("F11")
    assertEquals(214, ferry.east)
    assertEquals(72, ferry.south)

    assertEquals(286, ferry.manhattanDistance)
}