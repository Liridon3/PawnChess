import kotlin.system.exitProcess

var columnInitialW: Int = 0
var columnFinalW: Int = 0
var rowInitialW: Int = 0
var rowFinalW: Int = 0
var columnInitialB: Int = 0
var columnFinalB: Int = 0
var rowInitialB: Int = 0
var rowFinalB: Int = 0
const val blackPawn = 'B'
const val whitePawn = 'W'

fun main(args: Array<String>) {

    println("Pawns-Only Chess")
    var firstPlayersName: String = printAndEnter("First Player's name:")
    var secondPlayersName: String = printAndEnter("Second Player's name:")
    var gameGrid = arrayOfNulls<CharArray>(9)
    initializeGameGrid(gameGrid, whitePawn, blackPawn)
    printGrid(gameGrid)
    whitePawnMove(firstPlayersName, gameGrid, rowInitialB = 0, rowFinalB = 0, columnFinalB = 0)
    winConditionCheck(gameGrid, whitePawn, blackPawn)
    while (true) {
        blackPawnStalemate(gameGrid)
        blackPawnMove(secondPlayersName, gameGrid, rowInitialW, rowFinalW, columnFinalW)
        winConditionCheck(gameGrid, blackPawn, whitePawn)
        whitePawnStalemate(gameGrid)
        whitePawnMove(firstPlayersName, gameGrid, rowInitialB, rowFinalB, columnFinalB)
        winConditionCheck(gameGrid, whitePawn, blackPawn)
    }
}

fun printAndEnter(info: String): String {
    println(info)
    return readln()
}

fun initializeGameGrid (gameGrid: Array<CharArray?>, whitePawn: Char, blackPawn: Char) {
    gameGrid[0] = charArrayOf('8',' ',' ',' ',' ',' ',' ',' ',' ')
    gameGrid[1] = charArrayOf('7', blackPawn, blackPawn, blackPawn, blackPawn, blackPawn, blackPawn, blackPawn, blackPawn)
    gameGrid[2] = charArrayOf('6',' ',' ',' ',' ',' ',' ',' ',' ')
    gameGrid[3] = charArrayOf('5',' ',' ',' ',' ',' ',' ',' ',' ')
    gameGrid[4] = charArrayOf('4',' ',' ',' ',' ',' ',' ',' ',' ')
    gameGrid[5] = charArrayOf('3',' ',' ',' ',' ',' ',' ',' ',' ')
    gameGrid[6] = charArrayOf('2', whitePawn, whitePawn, whitePawn, whitePawn, whitePawn, whitePawn, whitePawn, whitePawn)
    gameGrid[7] = charArrayOf('1',' ',' ',' ',' ',' ',' ',' ',' ')
    gameGrid[8] = charArrayOf(' ', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h')
}
fun printGrid (gameGrid: Array<CharArray?>) {
    for (r in 0..7) {
        println("  +---+---+---+---+---+---+---+---+")
        println("${gameGrid[r]?.get(0)} | ${gameGrid[r]?.get(1)} | ${gameGrid[r]?.get(2)} | ${gameGrid[r]?.get(3)} | ${gameGrid[r]?.get(4)} |" +
                " ${gameGrid[r]?.get(5)} | ${gameGrid[r]?.get(6)} | ${gameGrid[r]?.get(7)} | ${gameGrid[r]?.get(8)} | ")

    }
    println("  +---+---+---+---+---+---+---+---+")
    println("${gameGrid[8]?.get(0)}   ${gameGrid[8]?.get(1)}   ${gameGrid[8]?.get(2)}   ${gameGrid[8]?.get(3)}   ${gameGrid[8]?.get(4)}   " +
            "${gameGrid[8]?.get(5)}   ${gameGrid[8]?.get(6)}   ${gameGrid[8]?.get(7)}   ${gameGrid[8]?.get(8)}   ")

}
fun blackPawnMove (secondPlayersName: String, gameGrid: Array<CharArray?>, rowInitialW: Int, rowFinalW: Int, columnFinalW: Int) {
    loopB@ while (true) {
        var moveList: String = printAndEnter("$secondPlayersName's turn:")
        var bStartPosition = moveList[0].toString()+moveList[1]
        if (moveList == "exit") {
            exit()
        } else {
            columnInitialB = columnBreakDown(moveList.first())
            columnFinalB = columnBreakDown(moveList[2])
            rowInitialB = rowBreakDown(moveList[1])
            rowFinalB = rowBreakDown(moveList.last())
            if (outOfBoundsOrBadInput(moveList)) {
                println("Invalid Input")
                continue@loopB //move has to be 4 characters and it has to be within bounds on array
            } else if (gameGrid[rowInitialB]!![columnInitialB] != 'B') {
                println("No black pawn at $bStartPosition")
                continue@loopB //Black Pawn is selected
            } else if (rowFinalB > rowInitialB + 2 || rowFinalB <= rowInitialB) {
                println("Invalid Input")
                continue@loopB //doesn't move more than 2 spaces or backwards
            } else if (rowInitialB != 1 && rowInitialB + 1 != rowFinalB) {
                println("Invalid Input")
                continue@loopB //if pawn is not being moved for the first time, it can't move more than 2 spaces
            } else if (gameGrid[rowInitialB]!![columnInitialB] == 'W') {
                println("Invalid Input")
                continue@loopB //it can't start on a White Pawn
            } else if (blackPawnRegularCapture(gameGrid)) {
                blackPawnCaptureUpdate(gameGrid)
                break // Accept If it lands on a White paw, and it moved one space forward, and diagonally either left or right.
            } else if (blackPawnEnPassantCapture(gameGrid)) {
                // en passant capture: accept if white last moved from start(6), white moved 2 spaces, white is next to black
                // black can now cross over to whites column and pass white and capture it
                gameGrid[rowFinalW]!![columnFinalW] = ' ' //captures white and blank the space
                blackPawnCaptureUpdate(gameGrid)
                break
            }else if (columnInitialB != columnFinalB) {
                println("Invalid Input")
                continue@loopB //it has to move sideways, unless it's capturing a pawn
            }else if (gameGrid[rowFinalB]!![columnFinalB] == 'W'){
                println("Invalid Input")
                continue@loopB //it can't capture a pawn if it's in front of it
            }
        }
        blackPawnCaptureUpdate(gameGrid)
        break
    }
}

fun whitePawnMove (firstPlayersName: String, gameGrid: Array<CharArray?>, rowInitialB: Int, rowFinalB: Int, columnFinalB: Int){
    loop@ while (true) {
        var moveList = printAndEnter("$firstPlayersName's turn:")
        var wStartPosition = moveList[0].toString()+moveList[1]
        if (moveList == "exit") {
            exit()
        }
        else {
            columnInitialW = columnBreakDown(moveList.first())
            columnFinalW = columnBreakDown(moveList[2])
            rowInitialW = rowBreakDown(moveList[1])
            rowFinalW = rowBreakDown(moveList.last())
            if (outOfBoundsOrBadInput(moveList)) {
                println("Invalid Input")
                continue@loop
            } else if (gameGrid[rowInitialW]!![columnInitialW] != 'W' ) {
                println("No white pawn at $wStartPosition")
                continue@loop //White Pawn is selected
            } else if ( rowFinalW < rowInitialW - 2 || rowFinalW >= rowInitialW) {
                println("Invalid Input")
                continue@loop //doesn't move more than 2 spaces or backwards
            } else if (rowInitialW != 6 && rowInitialW - 1 != rowFinalW) {
                println("Invalid Input")
                continue@loop //if pawn is not being moved for the first time, it can't move more than 1 space
            } else if (gameGrid[rowInitialW]!![columnInitialW] == 'B') {
                println("Invalid Input")
                continue@loop //it can't start on a Black Pawn
            } else if (whitePawnRegularCapture(gameGrid)) {
                whitePawnCaptureUpdate(gameGrid)
                winConditionCheck(gameGrid, whitePawn, blackPawn)
                break // Accept If it lands on a Black Pawn, and it moved one space forward, and diagonally either left or right.
            } else if (whitePawnEnPassantCapture(gameGrid))
            {   // en passant capture: accept if black last moved from start(1), black moved 2 spaces, white is next to black
                // white can now cross over to black column and pass black and capture it
                gameGrid[rowFinalB]!![columnFinalB] = ' '
                whitePawnCaptureUpdate(gameGrid)
                break
            }else if (columnInitialW != columnFinalW) {
                println("Invalid Input")
                continue@loop //it has to move vertically, unless it's capturing a pawn
            }else if (gameGrid[rowFinalW]!![columnFinalW] == 'B'){
                println("Invalid Input")
                continue@loop //it can't capture a pawn if it's in front of it
            }

        }
        whitePawnCaptureUpdate(gameGrid)
        break
    }

}

fun columnBreakDown(input: Char): Int {
    var col = when (input) {
        'a' -> 1
        'b' -> 2
        'c' -> 3
        'd' -> 4
        'e' -> 5
        'f' -> 6
        'g' -> 7
        'h' -> 8
        else -> 9
    }
    return col
}
fun rowBreakDown (input: Char): Int {
    var row = when (input) {
        '8' -> 0
        '7' -> 1
        '6' -> 2
        '5' -> 3
        '4' -> 4
        '3' -> 5
        '2' -> 6
        '1' -> 7
        else -> 9
    }
    return row
}

fun winConditionCheck (gameGrid: Array<CharArray?>, pawn: Char, enemyPawn: Char) {
    var pawnColor = if (pawn == 'W') { "White" } else { "Black" }
    var rowIndexForWinCalc = if (pawn == 'W') { 0 } else { 7 }
    if (gameGrid.contentDeepToString().count { it == enemyPawn } == 0) {
        println("$pawnColor Wins!")
        exit()
    } else if (checkLastRowWin(gameGrid, rowIndexForWinCalc, pawn)) {
        println("$pawnColor Wins!")
        exit()
    }
}
fun whitePawnStalemate (gameGrid: Array<CharArray?>){
    if (pawnCantMove(gameGrid) == 'W' && !whitePawnRegularCapture(gameGrid) && !whitePawnEnPassantCapture(gameGrid)) {
        stalemate()
    }
}

fun blackPawnStalemate (gameGrid: Array<CharArray?>) {
    if (pawnCantMove(gameGrid) == 'B' && !blackPawnRegularCapture(gameGrid) && !blackPawnEnPassantCapture(gameGrid)) {
        stalemate()
    }
}

fun whitePawnRegularCapture (gameGrid: Array<CharArray?>): Boolean {
    return gameGrid[rowFinalW]!![columnFinalW] == 'B' && rowFinalW == rowInitialW - 1 && (columnFinalW == columnInitialW - 1 || columnFinalW == columnInitialW + 1)
}

fun whitePawnEnPassantCapture (gameGrid: Array<CharArray?>): Boolean {
    return (rowInitialB == 1) && (rowFinalB == rowInitialW) && (rowFinalB == (rowInitialB + 2)) && (gameGrid[rowFinalW + 1]!![columnFinalW] == 'B')
            && (columnFinalW == columnInitialW - 1 || columnFinalW == columnInitialW +1)
}

fun whitePawnCaptureUpdate (gameGrid: Array<CharArray?>) {
    gameGrid[rowInitialW]!![columnInitialW] = ' '
    gameGrid[rowFinalW]!![columnFinalW] = 'W'
    printGrid(gameGrid)
}
fun blackPawnRegularCapture (gameGrid: Array<CharArray?>): Boolean {
    return gameGrid[rowFinalB]!![columnFinalB] == 'W' && rowFinalB == rowInitialB +1 && (columnFinalB ==
            columnInitialB +1 || columnFinalB == columnInitialB -1)
}

fun blackPawnEnPassantCapture (gameGrid: Array<CharArray?>): Boolean {
    return rowInitialW == 6 && rowFinalW == rowInitialB && rowFinalW == rowInitialW -2 && gameGrid[rowFinalB-1]!![columnFinalB] == 'W'
            && (columnFinalB == columnInitialB +1 || columnFinalB == columnInitialB -1)
}

fun blackPawnCaptureUpdate (gameGrid: Array<CharArray?>) {
    gameGrid[rowInitialB]!![columnInitialB] = ' '
    gameGrid[rowFinalB]!![columnFinalB] = 'B'
    printGrid(gameGrid)
}

fun outOfBoundsOrBadInput (moveList: String): Boolean {
    return columnInitialB == 9 || columnFinalB == 9 || rowInitialB == 9 || rowFinalB == 9 || moveList.length != 4
}

fun stalemate () {
    println("Stalemate!")
    exit()
}

fun checkLastRowWin (gameGrid: Array<CharArray?>, rowIndexForWinCalc: Int, pawn: Char): Boolean {
    var countOfPawnInLastRow: Int = 0
    var pawnInEnemyLastRow: Boolean = false

    for (column: Int in 0..7)
        if (gameGrid[rowIndexForWinCalc]!![column] == pawn) {
            countOfPawnInLastRow++
        }
    if (countOfPawnInLastRow > 0) {
        pawnInEnemyLastRow = true
    }
    return pawnInEnemyLastRow
}

fun exit () {
    println("Bye!")
    exitProcess(0)
}

fun pawnCantMove (gameGrid: Array<CharArray?>): Char {
    var whitePawnFreeSpaceToMove: Int = 0
    var blackPawnFreeSpaceToMove: Int = 0
    var thisPawnCantMove: Char = ' '

    for (row in 0..7)
        for (column in 1..8)
            if (gameGrid[row]!![column] == whitePawn && (gameGrid[row - 1]!![column] == ' ' || gameGrid[row - 1]!![column-1] == 'B'
                        || gameGrid[row - 1]!![if (column +1 > 8) 0 else column+ 1 ] == 'B')) {
                whitePawnFreeSpaceToMove++
            }
    for (rowB in 0..7)
        for (columnB in 1..8)
            if (gameGrid[rowB]!![columnB] == blackPawn && (gameGrid[rowB + 1]!![columnB] == ' ' || gameGrid[rowB + 1]!![columnB-1] == 'W'
                        || gameGrid[rowB + 1]!![if (columnB +1 > 8) 0 else columnB+1] == 'W')) {
                blackPawnFreeSpaceToMove++
            }
    if (whitePawnFreeSpaceToMove == 0) {
        thisPawnCantMove = whitePawn
    } else if (blackPawnFreeSpaceToMove == 0) {
        thisPawnCantMove = blackPawn
    }
    return thisPawnCantMove
}
