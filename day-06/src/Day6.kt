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
    val distinct  = groups
        .map { group ->
            group.split("\n").fold(setOf<Char>()) {
                acc, s ->  acc union s.toSet()
            }
        }
    val all = groups.map { group ->
        group.trim().split("\n").map { it.toSet() }.reduce {
            acc, s ->  acc intersect  s
        }
    }



    val distinct2 = groups.map {
        it.split("").filter { Regex("^[a-z]$").matches(it) }.distinct().size
    }
    val all2 = groups.map { group ->
        val people = group.split("\n").filter { it.isNotEmpty() }

        val answers = group.split("").distinct().filter { Regex("^[a-z]$").matches(it) }

        answers.filter { answer ->
            people.all { it.contains(answer) }
        }.size
    }
    println("The sum 1 is: ${distinct.sumBy { it.size }}")
    println("The sum 2 is: ${distinct2.sum()}")
    println("The sum for everyone answers is: ${all.sumBy { it.size }}")
    println("The sum for everyone answers is: ${all2.sum()}")
}


fun runTests() {

}
