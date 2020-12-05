import java.io.File
import java.lang.RuntimeException
import kotlin.test.assertEquals

class Day5 {
    companion object {

        fun getSeat(s: String): Seat {
            var row = 0..127
            var col = 0..8
            s.forEach { char ->
                when (char) {
                    'F' -> row = frontHalf(row)
                    'B' -> row = backHalf(row)
                    'R' -> col = backHalf(col)
                    'L' -> col = frontHalf(col)
                    else -> {
                        TODO("Char " + char + " was unexpected")
                    }
                }
            }
            return Seat(row.first, col.first)
        }

        fun backHalf(range: IntRange): IntRange {
            val rangeSize = range.last - range.first
            val newRangeSize = Math.ceil(rangeSize / 2.0).toInt()
            val intRange = (range.first + newRangeSize)..range.last
            return intRange
        }

        fun frontHalf(range: IntRange): IntRange {
            val rangeSize = range.last - range.first
            val newRangeSize = rangeSize / 2
            val intRange = range.first..(range.first + newRangeSize)
            return intRange
        }

    }
}

data class Seat(val row: Int, val col: Int) {
    val id: Int
        get() = (row * 8) + col
}


fun main() {
    runTests()

    val boardingCardNumbers = File("${System.getProperty("user.dir")}/day-05/src/input.txt")
            .useLines { it.toList() }

    val seats = boardingCardNumbers.map { Day5.getSeat(it) }
    val ids = seats.map { it.id }.sorted()
    val highest = seats.maxBy { it.id } ?: throw RuntimeException("ruh roh!")
    val lowest = seats.minBy { it.id } ?: throw RuntimeException("ruh roh!")
    val missing = IntRange(lowest.id + 1, highest.id - 1).first { ids.contains( it) == false }


    println("The highest seat id is ${highest.id}")
    println("My seat id is ${missing}")

}


fun runTests() {
    val test = Day5()

    assertEquals(0..63, Day5.frontHalf(0..127))


    assertEquals(Seat(44, 5), Day5.getSeat("FBFBBFFRLR"))
    listOf(
            Triple("BFFFBBFRRR", Seat(70, 7), 567),
            Triple("FFFBBBFRRR", Seat(14, 7), 119),
            Triple("BBFFBBFRLL", Seat(102, 4), 820)
    ).forEach { (str, seat, id) ->
        val foundSeat = Day5.getSeat(str)
        assertEquals(seat, foundSeat)
        assertEquals(id, seat.id)
    }

}
