# Setup 
gameActive = True
winner              = None
currentPlayer       = "X"
board               = ["-", "-", "-", 
                        "-", "-", "-", 
                        "-", "-", "-", ]

def displayBoard():
    print(board[0] + " | " + board[1] + " | " + board[2])
    print(board[3] + " | " + board[4] + " | " + board[5])
    print(board[6] + " | " + board[7] + " | " + board[8])


# Player Turns
def handleTurn(player):
    print(player + "'s turn")
    position = input("Choose a position 1-9: ")

    valid = False
    while not valid:

        while(position not in ["1", "2", "3", "4", "5", "6", "7", "8", "9"]):
            position = input("Choose a position 1-9: ")

        position = int(position) - 1

        if board[position] == "-":
            valid = True
        else:
            print("Pick an empty square!")

    board[position] = player
    displayBoard()

def flipPlayer():
    global currentPlayer

    if(currentPlayer == "X"):
        currentPlayer = "O"
    else:
        currentPlayer = "X"


# Check game over conditions
def checkOver():
    checkWin()
    checkTie()

def checkWin():
    global winner
    global gameActive

    # Check rows
    row1 = board[0] == board[1] == board[2] != "-"
    row2 = board[3] == board[4] == board[5] != "-"
    row3 = board[6] == board[7] == board[8] != "-"

    if(row1 or row2 or row3):
        gameActive = False
    
        if(row1): 
            winner = board[0]
        elif(row2):
            winner = board[3]
        elif(row3): 
            winner = board[6]

    # Check columns
    col1 = board[0] == board[3] == board[6] != "-"
    col2 = board[1] == board[4] == board[7] != "-"
    col3 = board[2] == board[5] == board[8] != "-"

    if(col1 or col2 or col3):
        gameActive = False

        if(col1): 
            winner = board[0]
        elif(col2):
            winner = board[1]
        elif(col3): 
            winner = board[2]

    # Check diagonals
    dia1 = board[0] == board[4] == board[8] != "-"
    dia2 = board[6] == board[4] == board[2] != "-"

    if(dia1 or dia2):
        gameActive = False

        if(dia1): 
            winner = board[0]
        elif(dia2):
            winner = board[6]

    return winner


def checkTie():
    global gameActive

    if("-" not in board):
        gameActive = False


# Play game
def playGame():
    displayBoard()

    while(gameActive):
        handleTurn(currentPlayer)
        checkOver()
        flipPlayer()

    if winner != None:
        print(winner + " won!")
    else:
        print("Tie game!")  


# Start
if __name__ == "__main__":
    playGame()