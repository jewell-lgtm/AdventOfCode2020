import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Day04 {
    companion object {
        fun part1(str: String): List<Credentials> {
            return str.split("\n\n").map { Credentials(parseStr(it), Ruleset.part1()) }
        }
        fun part2(str: String): List<Credentials> {
            return str.split("\n\n").map { Credentials(parseStr(it), Ruleset.part2()) }
        }

        fun parseStr(str: String): List<Pair<String, String>> {
            return str.replace("\n", " ")
                .trim()
                .split(" ")
                .map { token ->
                    token.split(":")
                        .let { it[0] to it[1] }
                }
        }
    }


    class Credentials(val creds: List<Pair<String, String>>, val ruleset: Ruleset) {
        val valid: Boolean
            get() = ruleset.validate(creds)
    }


    class Ruleset(val rules: List<Rule>) {
        companion object {
            fun part1(): Ruleset {
                val tokens: List<Rule> = listOf(
                    PresentRule("byr"),
                    PresentRule("iyr"),
                    PresentRule("eyr"),
                    PresentRule("hgt"),
                    PresentRule("hcl"),
                    PresentRule("ecl"),
                    PresentRule("pid")// ,
//                    PresentRule("cid")
                )
                return Ruleset(tokens)
            }
            fun part2(): Ruleset {
                val tokens: List<Rule> = listOf(
                    BirthYearRule(),
                    IssueYearRule(),
                    ExpirationYearRule(),
                    HeightRule(),
                    HairRule(),
                    EyesRule(),
                    PassportRule(),
                )
                return Ruleset(tokens)
            }
        }

        fun validate(creds: List<Pair<String, String>>) = rules.all { it.isValid(creds) }

    }

    interface Rule {
        fun isValid(creds: List<Pair<String, String>>): Boolean
    }

    class PresentRule(val key: String) : Rule  {
        override fun isValid(creds: List<Pair<String, String>>): Boolean {
            return creds.any { it.first == key }
        }
    }

    abstract class InRangeRule(val key: String, val from: Int, val to: Int) : Rule {
        override fun isValid(creds: List<Pair<String, String>>): Boolean {
            return creds.find { it.first == key }?.let { it.second.toIntOrNull() in from..to } ?: false
        }
    }

    class BirthYearRule : InRangeRule("byr", 1920, 2002)
    class IssueYearRule : InRangeRule("iyr", 2010, 2020)
    class ExpirationYearRule : InRangeRule("eyr", 2020, 2030)


    class HeightRule : Rule {
        override fun isValid(creds: List<Pair<String, String>>): Boolean {
            return creds.find { it.first == "hgt" }?.let { (_, value) ->
                Regex("^(\\d+)cm$").matchEntire(value)?.let { match ->
                    match.groups[1]!!.value.toIntOrNull()?.let { it in 150..193 }
                } ?: Regex("^(\\d+)in$").matchEntire(value)?.let { match ->
                    match.groups[1]!!.value.toIntOrNull()?.let { it in 59..76 }
                }
            } ?: false
        }
    }

    abstract class RegexRule(val key: String, val regex: Regex) : Rule {
        override fun isValid(creds: List<Pair<String, String>>): Boolean {
            return creds.find { it.first == key }
                ?.let {
                    regex.matches(it.second.trim())
                } ?: false
        }
    }

    class HairRule : RegexRule(key = "hcl", regex = Regex("^#[a-fA-F0-9]{6}$"))
    class EyesRule : RegexRule(key = "ecl", regex = Regex("^amb|blu|brn|gry|grn|hzl|oth$"))
    class PassportRule : RegexRule(key = "pid", regex = Regex("^\\d\\d\\d\\d\\d\\d\\d\\d\\d$"))
}

fun main() {
    runTests()

    val input = File("${System.getProperty("user.dir")}/day-4/src/input.txt").readText()

    val part1 = Day04.part1(input)
    println("There are ${part1.count { it.valid }} credentials")
    val part2 = Day04.part2(input)
    println("There are ${part2.count { it.valid }} credentials")
}


fun runTests() {

    val birthYearRule = Day04.BirthYearRule()
    assertTrue(birthYearRule.isValid(listOf("byr" to "2002", "something" to "else")))
    assertFalse(birthYearRule.isValid(listOf()))
    assertFalse(birthYearRule.isValid(listOf("asd" to "asd")))
    assertFalse(birthYearRule.isValid(listOf("byr" to "asd")))
    assertFalse(birthYearRule.isValid(listOf("byr" to "1919")))
    assertFalse(birthYearRule.isValid(listOf("byr" to "2003")))

    val height = Day04.HeightRule()
    assertTrue(height.isValid(listOf("hgt" to "190cm", "something" to "else")))
    assertTrue(height.isValid(listOf("hgt" to "60in", "something" to "else")))
    assertFalse(height.isValid(listOf()))
    assertFalse(height.isValid(listOf("asd" to "asd")))
    assertFalse(height.isValid(listOf("hgt" to "asd")))
    assertFalse(height.isValid(listOf("hgt" to "149cm")))
    assertFalse(height.isValid(listOf("hgt" to "194cm")))
    assertFalse(height.isValid(listOf("hgt" to "58in")))
    assertFalse(height.isValid(listOf("hgt" to "77in")))

    val hair = Day04.HairRule()
    assertTrue(hair.isValid(listOf("hcl" to "#000000", "something" to "else")))
    assertFalse(hair.isValid(listOf()))
    assertFalse(hair.isValid(listOf("asd" to "asd")))
    assertFalse(hair.isValid(listOf("hcl" to "asd")))
    assertFalse(hair.isValid(listOf("hcl" to "#000")))
    assertFalse(hair.isValid(listOf("hcl" to "#0000000")))
    assertFalse(hair.isValid(listOf("hcl" to "#000x00")))

    val eyes = Day04.EyesRule()
    assertTrue(eyes.isValid(listOf("ecl" to "amb", "something" to "else")))
    assertTrue(eyes.isValid(listOf("ecl" to "blu", "something" to "else")))
    assertTrue(eyes.isValid(listOf("ecl" to "brn", "something" to "else")))
    assertTrue(eyes.isValid(listOf("ecl" to "gry", "something" to "else")))
    assertTrue(eyes.isValid(listOf("ecl" to "grn", "something" to "else")))
    assertTrue(eyes.isValid(listOf("ecl" to "hzl", "something" to "else")))
    assertTrue(eyes.isValid(listOf("ecl" to "oth", "something" to "else")))
    assertFalse(eyes.isValid(listOf()))
    assertFalse(eyes.isValid(listOf("asd" to "asd")))
    assertFalse(eyes.isValid(listOf("ecl" to "asd")))
    assertFalse(eyes.isValid(listOf("ecl" to "blue")))


    val passport = Day04.PassportRule()
    assertTrue(passport.isValid(listOf("pid" to "012345678", "something" to "else")))
    assertFalse(passport.isValid(listOf()))
    assertFalse(passport.isValid(listOf("asd" to "asd")))
    assertFalse(passport.isValid(listOf("pid" to "asd")))
    assertFalse(passport.isValid(listOf("pid" to "1234567890")))



    val str = "ecl:gry pid:860033327 eyr:2020 hcl:#fffffd\n" +
            "byr:1937 iyr:2017 cid:147 hgt:183cm\n" +
            "\n" +
            "iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884\n" +
            "hcl:#cfa07d byr:1929"
    val credentials = Day04.part1(str)
    assertEquals(2, credentials.size)
    val valid = credentials[0]
    val invalid = credentials[1]
    assertTrue(valid.valid)
    assertFalse(invalid.valid)


    var str2 = "pid:087499704 hgt:74in ecl:grn iyr:2012 eyr:2030 byr:1980\n" +
            "hcl:#623a2f\n" +
            "\n" +
            "eyr:2029 ecl:blu cid:129 byr:1989\n" +
            "iyr:2014 pid:896056539 hcl:#a97842 hgt:165cm\n" +
            "\n" +
            "hcl:#888785\n" +
            "hgt:164cm byr:2001 iyr:2015 cid:88\n" +
            "pid:545766238 ecl:hzl\n" +
            "eyr:2022\n" +
            "\n" +
            "iyr:2010 hgt:158cm hcl:#b6652a ecl:blu byr:1944 eyr:2021 pid:093154719"
    val credentials2 = Day04.part2(str2)
    assertEquals(4, credentials2.size)
    assertEquals(4, credentials2.count { it.valid })


    var str3 = "eyr:1972 cid:100\n" +
            "hcl:#18171d ecl:amb hgt:170 pid:186cm iyr:2018 byr:1926\n" +
            "\n" +
            "iyr:2019\n" +
            "hcl:#602927 eyr:1967 hgt:170cm\n" +
            "ecl:grn pid:012533040 byr:1946\n" +
            "\n" +
            "hcl:dab227 iyr:2012\n" +
            "ecl:brn hgt:182cm pid:021572410 eyr:2020 byr:1992 cid:277\n" +
            "\n" +
            "hgt:59cm ecl:zzz\n" +
            "eyr:2038 hcl:74454a iyr:2023\n" +
            "pid:3556412378 byr:2007"
    val credentials3 = Day04.part2(str3)
    assertEquals(4, credentials3.size)
    assertEquals(0, credentials3.count { it.valid })


}