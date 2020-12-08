class OutOfTapeException : RuntimeException("Out Of Tape")
class UnexpectedLoopException(address: Int) : RuntimeException("Unexpected Loop at address $address")
class TooManyTicksException : RuntimeException("Too Many Ticks")
class UnknownOpCodeError(code: String) : RuntimeException("Unknown operation $code")

class TapeComputer(
    var tape: MutableList<String>,
    var address: Int = 0,
    var acc: Int = 0
) {
    var ticks = 0
    var breadCrumbs: MutableSet<Int> = mutableSetOf()

    fun tick() {
        if (ticks > 10000) throw TooManyTicksException()
        if (breadCrumbs.contains(address)) throw UnexpectedLoopException(address)
        breadCrumbs.add(address)
        val instruction = tape.getOrNull(address)?.split(" ") ?: throw OutOfTapeException()
        val code = instruction[0]
        val value = instruction[1].toInt()
        operate(code, value)
    }

    private fun operate(code: String, value: Int) = when(code) {
        "acc" -> {
            acc += value
            address += 1
        }
        "jmp" -> {
            address += value
        }
        "nop" -> {
            address += 1
        }
        "mut" -> {
            tape[value] = mutateInstruction(tape[value])
            address += 1
        }
        else -> throw UnknownOpCodeError(code)
    }

    private fun mutateInstruction(str: String) =
        str.replace("nop", "WAS_NO_OP").replace("jmp", "nop").replace("WAS_NO_OP", "jmp")
}


