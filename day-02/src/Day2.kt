import java.io.File

class Day2 {

    companion object {
        fun parseInput(text: List<String>): List<PasswordRule> {
            return text.map { PasswordRule.fromString(it) }
        }
    }
}

class PasswordRule(val atLeast: Int, val atMost: Int, val searchString: String, val password: String) {
    companion object {
        private val inputRule = Regex("^(\\d+)-(\\d+) (\\w): (\\w+)$")
        fun fromString(string: String): PasswordRule {
            val matches = inputRule.find(string) ?: throw RuntimeException("matches was null $string")
            return PasswordRule(
                    atLeast = matches.groups[1]!!.value.toInt(),
                    atMost = matches.groups[2]!!.value.toInt(),
                    searchString = matches.groups[3]!!.value,
                    password = matches.groups[4]!!.value
            )
        }
    }

    val rule1Valid: Boolean
        get() = password.chunked(1).count { it == searchString } in atLeast..atMost

    val searchChar = searchString[0]
    val atPos1 = password[atLeast - 1]
    val atPos2 = password[atMost - 1]

    val rule2Valid: Boolean
        get() = listOf(atPos1 == searchChar, atPos2 == searchChar).count { it } == 1


}


fun main(args: Array<String>) {
    val testInput = listOf(
            "1-3 a: abcde",
            "1-3 b: cdefg",
            "2-9 c: ccccccccc"
    )
    val rules = Day2.parseInput(testInput)
    println("According to test data ${rules.count { it.rule1Valid }} are valid")

    val txtReport = File("${System.getProperty("user.dir")}/day-2/src/input.txt")
            .useLines { it.toList() }

    val out = Day2.parseInput(txtReport)

    println("According to rule 1: ${out.count { it.rule1Valid }} are valid")
    println("According to rule 2: ${out.count { it.rule2Valid }} are valid")
}