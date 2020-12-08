import java.io.File

fun main() {
    val tape = File("${System.getProperty("user.dir")}/day-08/src/input.txt")
        .useLines { it.toList() }

    val part1 = TapeComputer(tape.toMutableList())
    println("Part1 2: the final acc was ${part1.apply { processUntilLoop() }.acc}")

    val newTapes = tape.mapIndexed { index, _ ->
        val newInstruction = "mut +$index"
        mutableListOf(newInstruction).apply { addAll(tape) }
    }
    val terminates = newTapes.map { TapeComputer(it) }.find { it.terminates() }
    println("Part1 2: the final acc was ${terminates?.acc}")
}



fun TapeComputer.terminates(): Boolean {
    try {
        while(true) { tick() }
    } catch (e: UnexpectedLoopException) {
        return false
    } catch (e: OutOfTapeException) {
        return true
    }
}

fun TapeComputer.processUntilLoop() {
    try {
        while(true) { tick() }
    } catch (e: UnexpectedLoopException) {
    }
}
