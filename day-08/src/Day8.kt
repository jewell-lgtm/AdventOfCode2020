import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


class Day8(val generation: Int = 0, val tape: List<String>) {
    var acc = 0

    var i = 0
    val breadcrumbs: MutableList<Int> = mutableListOf()


    val grammar = Regex("(nop|acc|jmp) ([+-]\\d+)")
    fun parseInstruction(atIndex: Int = i): Pair<String, Int> {
        val input = tape.getOrNull(atIndex) ?: throw NoTapeException()
        val parsed = grammar.find(input)
        val groups = parsed!!.groups
        return groups[1]!!.value to groups[2]!!.value.toInt()
    }

    class NoTapeException : RuntimeException("Mister Lightyear needs more tape!")
    class UnexpectedLoopException : RuntimeException("Unexpected Loop")

    fun execute() {
        if (breadcrumbs.contains(i)) {
            throw UnexpectedLoopException()
        }
        breadcrumbs.add(i)
        val (cmd, value) = parseInstruction(i)
        performInstruction(cmd, value)
    }

    fun mutate(): Day8 {
        return Day8(generation + 1, mutateTape(generation, generation + 1, tape))
    }

    private fun mutateTape(oldI: Int, newI: Int, tape: List<String>): List<String> {
        return tape.mapIndexed { index, line ->
            if (index == oldI || index == newI) mutateLine(line) else line
        }
    }

    private fun mutateLine(line: String) =
        line.replace("nop", "WAS_NO_OP").replace("jmp", "nop").replace("WAS_NO_OP", "jmp")


    private fun nextGeneration() = generation + 1

    private fun performInstruction(cmd: String, value: Int) {
        when (cmd) {
            "nop" -> noOp()
            "acc" -> accumulate(value)
            "jmp" -> jump(value)
            else -> throw RuntimeException("Dont know what to do with $cmd")
        }
    }

    private fun accumulate(value: Int) {
        acc += value
        i += 1
    }

    private fun noOp() {
        i += 1
    }

    private fun jump(value: Int) {
        i += value
    }

    fun terminatesAt(): Int? {
        try {
            while (true) {
                execute()
            }
        } catch (e: UnexpectedLoopException) {
            return null
        } catch (e: NoTapeException) {
            return acc
        }
    }
}


fun main() {
    runTests()
    val tape = File("${System.getProperty("user.dir")}/day-08/src/input.txt")
        .useLines { it.toList() }
    println("Part1: the final acc was ${part1(tape)}")
    println("Part2: the final acc was ${part2(tape)}")
}

private fun part1(tape: List<String>): Int {
    val bot = Day8(0, tape)
    try {
        var i = 0
        while (++i < 1000000) {
            bot.execute()
        }
        throw RuntimeException("A million is a big number")
    } catch (e: Day8.UnexpectedLoopException) {
    }
    return bot.acc
}

private fun part2(tape: List<String>): Int {
    var bot = Day8(0, tape)
    var int = bot.terminatesAt()
    while (int == null) {
        if (bot.generation > 1000000) {
            throw RuntimeException("A million is a big number")
        }
        bot = bot.mutate()
        int = bot.terminatesAt()
    }
    return int
}


fun runTests() {
    val input = "nop +0\n" +
            "acc +1\n" +
            "jmp +4\n" +
            "acc +3\n" +
            "jmp -3\n" +
            "acc -99\n" +
            "acc +1\n" +
            "jmp -4\n" +
            "acc +6"
    val tape = input.split("\n")

    val bot = Day8(0, tape)
    assertEquals("nop" to 0, bot.parseInstruction(0))
    assertEquals("acc" to 1, bot.parseInstruction(1))
    assertEquals("jmp" to 4, bot.parseInstruction(2))
    assertEquals("acc" to 3, bot.parseInstruction(3))
    assertEquals("jmp" to -3, bot.parseInstruction(4))


    assertEquals(0, bot.i)
    assertEquals(mutableListOf(), bot.breadcrumbs)

    bot.execute() // nop +0
    assertEquals(1, bot.i)
    assertEquals(0, bot.acc)
    assertEquals(mutableListOf(0), bot.breadcrumbs)

    bot.execute() // acc +1
    assertEquals(2, bot.i)
    assertEquals(1, bot.acc)
    assertEquals(mutableListOf(0, 1), bot.breadcrumbs)

    bot.execute() // jmp +4
    assertEquals(6, bot.i)
    assertEquals(1, bot.acc)
    assertEquals(mutableListOf(0, 1, 2), bot.breadcrumbs)

    bot.execute() // acc +1
    assertEquals(7, bot.i)
    assertEquals(2, bot.acc)
    assertEquals(mutableListOf(0, 1, 2, 6), bot.breadcrumbs)

    bot.execute() // jmp -4
    assertEquals(3, bot.i)
    assertEquals(2, bot.acc)
    assertEquals(mutableListOf(0, 1, 2, 6, 7), bot.breadcrumbs)


    bot.execute() // acc +3
    assertEquals(4, bot.i)
    assertEquals(5, bot.acc)
    assertEquals(mutableListOf(0, 1, 2, 6, 7, 3), bot.breadcrumbs)


    bot.execute() // jmp -3
    assertEquals(1, bot.i)
    assertEquals(5, bot.acc)
    assertEquals(mutableListOf(0, 1, 2, 6, 7, 3, 4), bot.breadcrumbs)


    assertFailsWith<Day8.UnexpectedLoopException> { bot.execute() }
    assertEquals(1, bot.i)
    assertEquals(mutableListOf(0, 1, 2, 6, 7, 3, 4), bot.breadcrumbs)

    val finalAcc = bot.acc
    assertEquals(5, finalAcc)

    assertEquals(0, bot.generation)

    var mutated = bot.mutate()
    var newTape = listOf(
        "jmp +0",
        "acc +1",
        "jmp +4",
        "acc +3",
        "jmp -3",
        "acc -99",
        "acc +1",
        "jmp -4",
        "acc +6"
    )
    assertEquals(1, mutated.generation)
    assertEquals(newTape, mutated.tape)
    assertFailsWith<Day8.UnexpectedLoopException> {
        mutated.execute()
        mutated.execute()
    }

    val nextTape = listOf(
        "nop +0",
        "acc +1",
        "jmp +4",
        "acc +3",
        "jmp -3",
        "acc -99",
        "acc +1",
        "nop -4",
        "acc +6"
    )
    val succeedsBot = Day8(0, nextTape)
    assertEquals(8, succeedsBot.terminatesAt())

}