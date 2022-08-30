# PawnChess
Chess has many variations. One of them is Pawns-only chess. It has only pawns. Not even a king. In this project, you create a two-player program for this chess variant.

The white pawns are denoted with the capital W, the black pawns are the capital B characters.

A unique pair of coordinates (a letter and a number) identifies each square of the chessboard. From White's left to right, the squares in the vertical lines are called files. They are labeled from a to h. The horizontal lines, known as ranks, are numbered from 1 to 8. These are legit coordinate pairs: a2, c6, d8, h5, and so on. This corresponds to the chess algebraic notation for recording moves in chess.

The Pawns-only chess has very simple rules. The pawns can make any standard pawn moves, except promotion. A player wins the game when one of the pawns reaches the opponent's last row, or if all opponent pawns are captured. A draw occurs when one of the players is unable to make a valid move. This is called a stalemate.

First, it will ask players for their names. After that, the software will prompt each player to take turns and make a move. It should follow the x0y1 format where x0 is the coordinates of a pawn that the user wants to move, and y1 are the coordinates of the final position. For example, at the current stage, the valid moves are a2a4, d4d8, a1h8, and so on. Moves like j2j4, h0h4, a2a4a are deemed to be invalid.
