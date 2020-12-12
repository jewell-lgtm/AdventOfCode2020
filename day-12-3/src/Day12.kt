import java.io.File
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
    var heading: Cardinal = Cardinal.East,
    var waypoint: Coord = Coord(10, -1),
    east: Int = 0,
    south: Int = 0,
) : Coord(east, south) {
    val moveInstruction = Regex("^([NSEWLRF])(\\d+)$")
    fun move(where: String) {
        val instruction = moveInstruction.matchEntire(where)
        val direction = instruction!!.groups[1]!!.value
        val amount = instruction.groups[2]!!.value.toInt()

        when (direction) {
            "F" -> head(heading, amount)
            "N" -> head(Cardinal.North, amount)
            "S" -> head(Cardinal.South, amount)
            "E" -> head(Cardinal.East, amount)
            "W" -> head(Cardinal.West, amount)
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
            "N" -> relocateWaypoint(Cardinal.North, amount)
            "S" -> relocateWaypoint(Cardinal.South, amount)
            "E" -> relocateWaypoint(Cardinal.East, amount)
            "W" -> relocateWaypoint(Cardinal.West, amount)
            "R" -> turnWaypoint(Degrees(Clock.Clockwise, amount))
            "L" -> turnWaypoint(Degrees(Clock.Anticlockwise, amount))
            else -> throw RuntimeException("Unexpected direction $direction")
        }
    }


    private fun head(heading: Cardinal, amount: Int) {
        when (heading) {
            Cardinal.North -> south -= amount
            Cardinal.South -> south += amount
            Cardinal.West -> east -= amount
            Cardinal.East -> east += amount
        }
    }

    val cardinal = listOf(Cardinal.East, Cardinal.South, Cardinal.West, Cardinal.North)
    private fun turn(direction: Clock, amount: Int) {
        val delta = amount / if (direction == Clock.Clockwise) 90 else -90
        val currIndex = cardinal.indexOf(heading)
        heading = cardinal[(4 + currIndex + delta) % 4]
    }

    private fun headWaypoint(amount: Int) {
        this.east += waypoint.east * amount
        this.south += waypoint.south * amount
    }

    class Degrees(val direction: Clock, val amount: Int) {
        val clockwise get() = if (direction == Clock.Clockwise) amount else 360 + (-1 * amount) % 360
    }

    private fun turnWaypoint(amount: Degrees) {
        waypoint = when (amount.clockwise) {
            90 -> Coord(-1 * waypoint.south, waypoint.east)
            180 -> Coord(-1 * waypoint.east, -1 * waypoint.south)
            270 -> Coord(waypoint.south, -1 * waypoint.east)
            else -> TODO("Dont know how to turn ${amount}")
        }
    }


    private fun relocateWaypoint(direction: Cardinal, amount: Int) {
        when (direction) {
            Cardinal.North -> this.waypoint.south -= amount
            Cardinal.South -> this.waypoint.south += amount
            Cardinal.West -> this.waypoint.east -= amount
            Cardinal.East -> this.waypoint.east += amount
        }
    }

}

enum class Clock {
    Clockwise, Anticlockwise
}

enum class Cardinal {
    North, South, East, West
}


fun runTests() {
    val ferry = Day12.createFerry()
    assertEquals(0, ferry.east)
    assertEquals(0, ferry.south)
    assertEquals(Cardinal.East, ferry.heading)

    ferry.move("F10")
    assertEquals(10, ferry.east)
    assertEquals(0, ferry.south)
    assertEquals(Cardinal.East, ferry.heading)

    ferry.move("N3")
    assertEquals(10, ferry.east)
    assertEquals(-3, ferry.south)
    assertEquals(Cardinal.East, ferry.heading)

    ferry.move("R90")
    assertEquals(10, ferry.east)
    assertEquals(-3, ferry.south)
    assertEquals(Cardinal.South, ferry.heading)

    ferry.move("L180")
    assertEquals(10, ferry.east)
    assertEquals(-3, ferry.south)
    assertEquals(Cardinal.North, ferry.heading)

    ferry.move("L180")
    assertEquals(10, ferry.east)
    assertEquals(-3, ferry.south)
    assertEquals(Cardinal.South, ferry.heading)


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
    assertEquals(Cardinal.South, ferry2.heading)
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