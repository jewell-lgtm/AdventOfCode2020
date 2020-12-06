import java.io.File

class Day6 {

    companion object {

    }
}

fun main() {
    runTests()

    val input = File("${System.getProperty("user.dir")}/day-06/src/input.txt").readText()
//    val input = "abc\n" +
//            "\n" +
//            "a\n" +
//            "b\n" +
//            "c\n" +
//            "\n" +
//            "ab\n" +
//            "ac\n" +
//            "\n" +
//            "a\n" +
//            "a\n" +
//            "a\n" +
//            "a\n" +
//            "\n" +
//            "b\n"


    val groups = input.split("\n\n")

    val distinct = groups.map {
        it.split("").filter { Regex("^[a-z]$").matches(it) }.distinct().size
    }
    val all = groups.map { group ->
        val people = group.split("\n").filter { it.isNotEmpty() }

        val answers = group.split("").distinct().filter { Regex("^[a-z]$").matches(it) }
        val all = answers.filter { answer ->
            people.all { it.contains(answer) }
        }

        all.size
    }
    println("The sum is: ${distinct.sum()}")
    println("The sum for everyone answers is: ${all.sum()}")
}


fun runTests() {

}
