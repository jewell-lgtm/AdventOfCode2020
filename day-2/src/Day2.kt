import java.io.File
import java.lang.RuntimeException
import kotlin.test.assertNotNull

class Day2 {

    companion object {
        fun parseInput(text: List<String>): List<PasswordRule> {
            return text.map { PasswordRule.fromString(it) }
        }
    }
}

class PasswordRule(val atLeast: Int, val atMost: Int, val searchChar: String, val searchString: String) {
    companion object {
        val inputRule = Regex("^(\\d)+-(\\d)+ (\\w): (\\w)+$")
        fun fromString(string: String): PasswordRule {
            val matches = inputRule.find(string) ?: throw RuntimeException("matches was null $string")
            return PasswordRule(
                    atLeast = matches.groups[1]!!.value.toInt(),
                    atMost = matches.groups[2]!!.value.toInt(),
                    searchChar = matches.groups[3]!!.value,
                    searchString = matches.groups[4]!!.value
            )
        }
    }

    fun isValid(): Boolean {
        val count = searchString.count { searchChar.contains(searchChar) }
        return count in atLeast..atMost
    }
}


fun main(args: Array<String>) {
    val testInput = listOf(
            "1-3 a: abcde",
            "1-3 b: cdefg",
            "2-9 c: ccccccccc"
    )
    val rules = Day2.parseInput(testInput)
    println(rules.count { it.isValid() })

    val txtReport = File("${System.getProperty("user.dir")}/day-2/src/input.txt")
            .useLines { line -> line.toList().map { it.toString() } }

    val rules2 = Day2.parseInput(txtReport)
    println(rules2.count { it.isValid() })
}