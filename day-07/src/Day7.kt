import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Day7(val accepts: Map<String, Map<String, Int>>) {

    fun canContain(searchColor: String, searchQuantity: Int, startColor: String, breadCrumbs: Set<String> = emptySet()): Boolean {
        // if the bag doesnt exist, it cant hold our search bag
        val bag: Map<String, Int>? = accepts[startColor]
        if (bag.isNullOrEmpty()) return false
        // check if this bag directly holds the searched bag
        val quantity: Int = bag[searchColor] ?: 0
        if (quantity >= searchQuantity) {
            return true
        }

        val heldBags = bag.keys.filter { !breadCrumbs.contains(it) }

        return heldBags.any { heldBag ->
            canContain(searchColor, searchQuantity, heldBag, breadCrumbs.union(setOf(heldBag)))
        }
    }

    fun all(searchColor: String): Set<String>  = accepts.keys.filter { canContain(searchColor, 1, it) }.toSet()

    fun containsBags(startColor: String, accumulator: HashMap<String, Int> = HashMap()): HashMap<String, Int> {
        val bag = accepts[startColor]?.toList()
        if (bag.isNullOrEmpty()) return accumulator


        for ((color, quantity) in bag) {
            for (i in 0 until quantity) {
                accumulator[color] = (accumulator[color] ?: 0) + 1
                containsBags(color, accumulator)
            }
        }

        return accumulator

    }

    companion object {
        fun parseInput(input: String): Day7 {
            val lines = input.split("\n")
            val accepts = getAccepts(lines)
            return Day7(accepts)
        }

        private fun getAccepts(lines: List<String>): Map<String, Map<String, Int>> {
            val pairs: List<Pair<String, Map<String, Int>>> = lines.map { line ->
                val parts = line.split(" contain ")
                val color = parts.first().replace("bags", "").replace("bag", "").trim()

                val acceptedColorStrs = parts[1].split(",")
                    .map { it.replace(".", "").replace("bags", "").replace("bag", "").replace("no", "0") }
                    .filter { !it.contains("0 other") }

                val acceptedColorMap = acceptedColorStrs.map {
                    val trimmed = it.trim()
                    val col = trimmed.substringAfter(" ").trim()
                    val substringBefore = trimmed.substringBefore(" ")
                    val quantity = substringBefore.toInt()
                    col to quantity
                }.toMap()

                color to acceptedColorMap
            }


            return pairs.toMap()

        }
    }



}


fun main() {
    runTests()

    val input  = File("${System.getProperty("user.dir")}/day-07/src/input.txt").readText().trim()

    val day7 = Day7.parseInput(input)
    println(day7.all("shiny gold").size)
    println(day7.containsBags("shiny gold").values.sum())
}


fun runTests() {


    val test1 = Day7.parseInput("bright white bags contain 1 shiny gold bag.")

    assertTrue(test1.canContain("shiny gold", 1, "bright white"))
    assertFalse(test1.canContain("piss yellow", 1, "bright white"))


    val test2 = Day7.parseInput("dark orange bags contain 3 bright white bags, 4 muted yellow bags.\n" +
                                        "bright white bags contain 1 shiny gold bag.")
    assertTrue(test2.canContain("shiny gold", 1, "dark orange"))


    val test3 = Day7.parseInput("light red bags contain 1 bright white bag, 2 muted yellow bags.\n" +
                                        "dark orange bags contain 3 bright white bags, 4 muted yellow bags.\n" +
                                        "bright white bags contain 1 shiny gold bag.\n" +
                                        "muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.\n" +
                                        "shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.\n" +
                                        "dark olive bags contain 3 faded blue bags, 4 dotted black bags.\n" +
                                        "vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.\n" +
                                        "faded blue bags contain no other bags.\n" +
                                        "dotted black bags contain no other bags.")
    assertTrue(test3.canContain("shiny gold", 1, "bright white"))
    assertTrue(test3.canContain("shiny gold", 1, "muted yellow"))
    assertTrue(test3.canContain("shiny gold", 1, "dark orange"))
    assertTrue(test3.canContain("shiny gold", 1, "light red"))


    assertFalse(test3.canContain("shiny gold", 1, "shiny gold"))
    assertFalse(test3.canContain("shiny gold", 1, "dark olive"))
    assertFalse(test3.canContain("shiny gold", 1, "vibrant plum"))
    assertFalse(test3.canContain("shiny gold", 1, "faded blue"))
    assertFalse(test3.canContain("shiny gold", 1, "dotted black"))

    assertEquals(setOf("bright white", "muted yellow", "dark orange", "light red"), test3.all("shiny gold"))


    val test4 = Day7.parseInput("shiny gold bags contain 2 dark red bags.\n" +
                                        "dark red bags contain 2 dark orange bags.\n" +
                                        "dark orange bags contain 2 dark yellow bags.\n" +
                                        "dark yellow bags contain 2 dark green bags.\n" +
                                        "dark green bags contain 2 dark blue bags.\n" +
                                        "dark blue bags contain 2 dark violet bags.\n" +
                                        "dark violet bags contain no other bags.")
    assertEquals(HashMap(), test4.containsBags("dark violet"))
    assertEquals(mapOf("dark violet" to 2), test4.containsBags("dark blue"))
    assertEquals(hashMapOf("dark blue" to 2, "dark violet" to 4), test4.containsBags("dark green"))
    assertEquals(126, test4.containsBags("shiny gold").values.sum())


}