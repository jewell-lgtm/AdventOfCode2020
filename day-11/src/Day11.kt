import java.io.File
import kotlin.test.assertEquals


class Day11 {
    companion object {
        fun parse(str: String): GameOfChairs {
            return GameOfChairs(str.trim().split("\n").map { it.trim() }.joinToString("\n"))
        }

    }
}

fun main() {
    runTests()

    val input = File("${System.getProperty("user.dir")}/day-11/src/input.txt")
        .readText()
    val game = Day11.parse(input)
    var gens = untilStable(game)
    println("$gens generations")

    println("${game.occupiedSeats} occupied seats")



    val game2 = Day11.parse(input)
    var gens2 = untilStable2(game2)
    println("$gens2 generations")
    println("${game2.occupiedSeats} occupied seats")
}


data class Seat(val x: Int, val y: Int)

class GameOfChairs(var str: String) {
    var board: String
    val width: Int
    val height: Int
    val seats: List<Seat>

    init {
        val split = str.split("\n")
        // wait wat...
        height = split.size
        width = split[0].length
        board = split.joinToString("")


        seats = mutableListOf<Seat>().apply {
            for (y in 0 until height) {
                for (x in 0 until width) {
                    if (board[coord(x, y)] == 'L') add(Seat(y, x))
                }
            }
        }.toList()
    }

    fun tick(): String {
        val nextBoard = board.toCharArray()

        for (x in 0 until width) {
            for (y in 0 until height) {
                val neighbors = getNeighbors(board, x, y)
                val occ = neighbors.filter { board[coord(it.x, it.y)] == '#' }
                when (board[coord(x, y)]) {
                    'L' -> {
                        if (occ.isEmpty()) {
                            nextBoard[coord(x, y)] = '#'
                        }
                    }
                    '#' -> {
                        if (occ.size >= 4) {
                            nextBoard[coord(x, y)] = 'L'
                        }
                    }
                }
            }
        }

        board = nextBoard.joinToString("")
        return board.chunked(width).joinToString("\n")

    }

    fun tick2(): String {
        val nextBoard = board.toCharArray()

        for (x in 0 until width) {
            for (y in 0 until height) {
                val neighbors = getVisibleNeighbors(board, x, y)
                val occ = neighbors.filter { board[coord(it.x, it.y)] == '#' }


                when (board[coord(x, y)]) {
                    'L' -> {
                        if (occ.isEmpty()) {
                            nextBoard[coord(x, y)] = '#'
                        }
                    }
                    '#' -> {
                        if (occ.size >= 5) {
                            nextBoard[coord(x, y)] = 'L'
                        }
                    }
                }
            }
        }

        board = nextBoard.joinToString("")
        return board.chunked(width).joinToString("\n")

    }

    private fun getVisibleNeighbors(board: String, x: Int, y: Int): List<Seat> =
        getVisibleCoord(board, x, y)


    val seen = HashMap<String, List<Seat>>()
    private fun getVisibleCoord(board: String, x: Int, y: Int): List<Seat> {
        if (seen.containsKey("$x:$y")) {
            return seen["$x:$y"]!!
        }


        val seats: List<Seat> = listOf(
            -1 to -1,
            0 to -1,
            1 to -1,
            1 to 0,
            -1 to 0,
            1 to 1,
            0 to 1,
            -1 to 1
        ).mapNotNull { (dX, dY) ->
            var searchX = x + dX
            var searchY = y + dY
            var found: Seat? = null
            while (found == null && searchX > -1 && searchX < width && searchY > -1 && searchY < height) {
                if (board[coord(searchX, searchY)] != '.') {
                    found = Seat(searchX, searchY)
                }
                searchX += dX
                searchY += dY
            }
            found
        }
        seen["$x:$y"] = seats
        return seats
    }

    private fun coord(x: Int, y: Int) = (y * width) + x

    private fun getNeighbors(board: String, x: Int, y: Int): List<Seat> {
        val l = x - 1
        val r = x + 1
        val u = y - 1
        val d = y + 1

        return listOf(
            Seat(l, u), Seat(x, u), Seat(r, u),
            Seat(l, y), Seat(r, y),
            Seat(l, d), Seat(x, d), Seat(r, d),
        ).filter { it.x > -1 && it.x < width && it.y > -1 && it.y < height }
    }

    val occupiedSeats
        get() = this.board.count { it == '#' }
}


private fun untilStable(game: GameOfChairs): Int {
    var i = 0

    var board = game.tick()
    i++
    var nextBoard = game.tick()
    i++
    while (nextBoard != board) {
        board = nextBoard
        nextBoard = game.tick()
        i++
    }
    return i
}
private fun untilStable2(game: GameOfChairs): Int {
    var i = 0

    var board = game.tick2()
    i++
    var nextBoard = game.tick2()
    i++
    while (nextBoard != board) {
        board = nextBoard
        nextBoard = game.tick2()
        i++
    }
    return i
}


fun runTests() {
    val game = Day11.parse(
        """
        L.LL.LL.LL
        LLLLLLL.LL
        L.L.L..L..
        LLLL.LL.LL
        L.LL.LL.LL
        L.LLLLL.LL
        ..L.L.....
        LLLLLLLLLL
        L.LLLLLL.L
        L.LLLLL.LL
    """.trimIndent()
    )

    val round1 = game.tick()

    assertEquals(
        """
        #.##.##.##
        #######.##
        #.#.#..#..
        ####.##.##
        #.##.##.##
        #.#####.##
        ..#.#.....
        ##########
        #.######.#
        #.#####.##
    """.trimIndent(),
        round1
    )

    val round2 = game.tick()

    assertEquals(
        """
        #.LL.L#.##
        #LLLLLL.L#
        L.L.L..L..
        #LLL.LL.L#
        #.LL.LL.LL
        #.LLLL#.##
        ..L.L.....
        #LLLLLLLL#
        #.LLLLLL.L
        #.#LLLL.##
    """.trimIndent(), round2
    )

    val round3 = game.tick()

    assertEquals(
        """
        #.##.L#.##
        #L###LL.L#
        L.#.#..#..
        #L##.##.L#
        #.##.LL.LL
        #.###L#.##
        ..#.#.....
        #L######L#
        #.LL###L.L
        #.#L###.##
    """.trimIndent(), round3
    )

    val round4 = game.tick()
    assertEquals(
        """
        #.#L.L#.##
        #LLL#LL.L#
        L.L.L..#..
        #LLL.##.L#
        #.LL.LL.LL
        #.LL#L#.##
        ..L.L.....
        #L#LLLL#L#
        #.LLLLLL.L
        #.#L#L#.##
    """.trimIndent(), round4
    )

    val round5 = game.tick()
    val round6 = game.tick()
    val round7 = game.tick()
    assertEquals(
        """
        #.#L.L#.##
        #LLL#LL.L#
        L.#.L..#..
        #L##.##.L#
        #.#L.LL.LL
        #.#L#L#.##
        ..L.L.....
        #L#L##L#L#
        #.LLLLLL.L
        #.#L#L#.##
    """.trimIndent(), round5
    )

    assertEquals(
        """
        #.#L.L#.##
        #LLL#LL.L#
        L.#.L..#..
        #L##.##.L#
        #.#L.LL.LL
        #.#L#L#.##
        ..L.L.....
        #L#L##L#L#
        #.LLLLLL.L
        #.#L#L#.##
    """.trimIndent(), round6
    )

    assertEquals(
        """
        #.#L.L#.##
        #LLL#LL.L#
        L.#.L..#..
        #L##.##.L#
        #.#L.LL.LL
        #.#L#L#.##
        ..L.L.....
        #L#L##L#L#
        #.LLLLLL.L
        #.#L#L#.##
    """.trimIndent(), round7
    )


    val game2 = Day11.parse(
        """
        L.LL.LL.LL
        LLLLLLL.LL
        L.L.L..L..
        LLLL.LL.LL
        L.LL.LL.LL
        L.LLLLL.LL
        ..L.L.....
        LLLLLLLLLL
        L.LLLLLL.L
        L.LLLLL.LL
    """.trimIndent()
    )
    assertEquals(6, untilStable(game2))
    assertEquals(37, game2.occupiedSeats)


    val oblongGame = Day11.parse(
        """
            LLL
            LLL
        """.trimIndent()
    )

    assertEquals(
        """
            ###
            ###
        """.trimIndent(), oblongGame.tick()
    )


    val farSeeingGame = Day11.parse(
        """
            L.LL.LL.LL
            LLLLLLL.LL
            L.L.L..L..
            LLLL.LL.LL
            L.LL.LL.LL
            L.LLLLL.LL
            ..L.L.....
            LLLLLLLLLL
            L.LLLLLL.L
            L.LLLLL.LL
        """.trimIndent()
    )

    assertEquals(
        """
            #.##.##.##
            #######.##
            #.#.#..#..
            ####.##.##
            #.##.##.##
            #.#####.##
            ..#.#.....
            ##########
            #.######.#
            #.#####.##
        """.trimIndent(),
        farSeeingGame.tick2()
    )

    assertEquals(
        """
            #.LL.LL.L#
            #LLLLLL.LL
            L.L.L..L..
            LLLL.LL.LL
            L.LL.LL.LL
            L.LLLLL.LL
            ..L.L.....
            LLLLLLLLL#
            #.LLLLLL.L
            #.LLLLL.L#
        """.trimIndent(),
        farSeeingGame.tick2()
    )
    assertEquals(
        """
            #.L#.##.L#
            #L#####.LL
            L.#.#..#..
            ##L#.##.##
            #.##.#L.##
            #.#####.#L
            ..#.#.....
            LLL####LL#
            #.L#####.L
            #.L####.L#
        """.trimIndent(),
        farSeeingGame.tick2()
    )
    assertEquals(
        """
            #.L#.L#.L#
            #LLLLLL.LL
            L.L.L..#..
            ##LL.LL.L#
            L.LL.LL.L#
            #.LLLLL.LL
            ..L.L.....
            LLLLLLLLL#
            #.LLLLL#.L
            #.L#LL#.L#
        """.trimIndent(),
        farSeeingGame.tick2()
    )
    assertEquals(
        """
            #.L#.L#.L#
            #LLLLLL.LL
            L.L.L..#..
            ##L#.#L.L#
            L.L#.#L.L#
            #.L####.LL
            ..#.#.....
            LLL###LLL#
            #.LLLLL#.L
            #.L#LL#.L#
        """.trimIndent(),
        farSeeingGame.tick2()
    )
    assertEquals(
        """
            #.L#.L#.L#
            #LLLLLL.LL
            L.L.L..#..
            ##L#.#L.L#
            L.L#.LL.L#
            #.LLLL#.LL
            ..#.L.....
            LLL###LLL#
            #.LLLLL#.L
            #.L#LL#.L#
        """.trimIndent(),
        farSeeingGame.tick2()
    )
    assertEquals(
        """
            #.L#.L#.L#
            #LLLLLL.LL
            L.L.L..#..
            ##L#.#L.L#
            L.L#.LL.L#
            #.LLLL#.LL
            ..#.L.....
            LLL###LLL#
            #.LLLLL#.L
            #.L#LL#.L#
        """.trimIndent(),
        farSeeingGame.tick2()
    )
    assertEquals(
        """
            #.L#.L#.L#
            #LLLLLL.LL
            L.L.L..#..
            ##L#.#L.L#
            L.L#.LL.L#
            #.LLLL#.LL
            ..#.L.....
            LLL###LLL#
            #.LLLLL#.L
            #.L#LL#.L#
        """.trimIndent(),
        farSeeingGame.tick2()
    )
}