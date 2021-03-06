import java.io.File
import kotlin.math.pow
import kotlin.test.assertEquals

class Day5 {

    companion object {
        fun getSeat(s: String): Seat {
            val id = s.foldIndexed(0) { index, acc, char -> acc + if (char == 'B' || char == 'R') 2.0.pow(index).toInt() else 0 }
            val row = s.filter { it == 'F' || it == 'B' }.replace('B', '1').replace('F', '0').toInt(2)
            val col = s.filter { it == 'L' || it == 'R' }.replace('R', '1').replace('L', '0').toInt(2)
            return Seat(row, col)

        }
    }
}

data class Seat(val row: Int, val col: Int) {
    val id: Int
        get() = (row * 8) + col
}


// Initial solution
//class Day5 {
//    companion object {
//
//        fun getSeat(s: String): Seat {
//            var row = 0..127
//            var col = 0..8
//            s.forEach { char ->
//                when (char) {
//                    'F' -> row = frontHalf(row)
//                    'B' -> row = backHalf(row)
//                    'R' -> col = backHalf(col)
//                    'L' -> col = frontHalf(col)
//                    else -> {
//                        TODO("Char " + char + " was unexpected")
//                    }
//                }
//            }
//            return Seat(row.first, col.first)
//        }
//
//        fun backHalf(range: IntRange): IntRange {
//            val rangeSize = range.last - range.first
//            val newRangeSize = Math.ceil(rangeSize / 2.0).toInt()
//            val intRange = (range.first + newRangeSize)..range.last
//            return intRange
//        }
//
//        fun frontHalf(range: IntRange): IntRange {
//            val rangeSize = range.last - range.first
//            val newRangeSize = rangeSize / 2
//            val intRange = range.first..(range.first + newRangeSize)
//            return intRange
//        }
//
//    }
//}


fun main() {
    runTests()

    val boardingCardNumbers = File("${System.getProperty("user.dir")}/day-05/src/input.txt")
            .useLines { it.toList() }

    val seats = boardingCardNumbers.map { Day5.getSeat(it) }.sortedBy { it.id }
    val ids = seats.map { it.id }
    val highest = seats.first()
    val lowest = seats.last()
    val missing = IntRange(lowest.id + 1, highest.id - 1).first { ids.contains(it) == false }


    println("The highest seat id is ${highest.id}")
    println("My seat id is ${missing}")

}


fun runTests() {
    val test = Day5()

//    assertEquals(0..63, Day5.frontHalf(0..127))


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
