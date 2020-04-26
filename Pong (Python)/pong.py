import turtle
import winsound

# Window Setup
# ##############################
window = turtle.Screen()
window.title("Pong")
window.bgcolor("black")
window.setup(width=800, height=600)
window.tracer(0)


# Player 1 Paddle (A)
# ##############################
paddleA = turtle.Turtle()
paddleA.speed(0)
paddleA.shape("square")
paddleA.shapesize(stretch_wid=5, stretch_len=1)
paddleA.color("white")
paddleA.penup()
paddleA.goto(-350, 0)

def paddleAUp():
    y = paddleA.ycor()
    if (y < 250):
        y += 20
    paddleA.sety(y)

def paddleADown():
    y = paddleA.ycor()
    if (y > -250):
        y -= 20
    paddleA.sety(y)


# Player 2 Paddle (B)
# ##############################
paddleB = turtle.Turtle()
paddleB.speed(0)
paddleB.shape("square")
paddleB.shapesize(stretch_wid=5, stretch_len=1)
paddleB.color("white")
paddleB.penup()
paddleB.goto(350, 0)

def paddleBUp():
    y = paddleB.ycor()
    if (y < 250):
        y += 20
    paddleB.sety(y)

def paddleBDown():
    y = paddleB.ycor()
    if (y > -250):
        y -= 20
    paddleB.sety(y)


# Ball
# ##############################
ball = turtle.Turtle()
ball.speed(0)
ball.shape("square")
ball.color("white")
ball.penup()
ball.goto(0, 0)

# defines how quickly ball moves (e.g. 0.15 pixels each refresh)
ball.dx = 0.15
ball.dy = 0.15


# Score
# ##############################
scoreA = 0
scoreB = 0

score = turtle.Turtle()
score.color("white")
score.speed(0)
score.penup()
score.hideturtle()
score.goto(0, 260)
score.write("Player A: " + str(scoreA) + " | Player B: " + str(scoreB), align="center", font=("Courier", 24, "normal"))

def updateScore():
    score.clear()
    score.write("Player A: " + str(scoreA) + " | Player B: " + str(scoreB), align="center", font=("Courier", 24, "normal"))


# Keyboard Bindings
# ##############################
window.listen()
window.onkeypress(paddleAUp, "w")
window.onkeypress(paddleADown, "s")
window.onkeypress(paddleBUp, "Up")
window.onkeypress(paddleBDown, "Down")


# Sound
# ##############################
def playSound():
    winsound.PlaySound("pong.wav", winsound.SND_ASYNC)


# Main Game Loop
# ##############################
while True:
    window.update()

    # Move ball
    ball.setx(ball.xcor() + ball.dx)
    ball.sety(ball.ycor() + ball.dy)

    # Ball border checking
    if (ball.ycor() > 290):
        ball.sety(290)
        ball.dy *= -1
        playSound()

    if (ball.ycor() < -290):
        ball.sety(-290)
        ball.dy *= -1
        playSound()

    # Player A scores
    if (ball.xcor() > 390):
        ball.goto(0, 0)
        ball.dx *= -1
        scoreA += 1
        updateScore()

    # Player B scores
    if (ball.xcor() < -390):
        ball.goto(0, 0)
        ball.dx *= -1
        scoreB += 1
        updateScore()

    # Paddle and ball collision 
    if (ball.xcor() > 340 and ball.xcor() < 350) and (ball.ycor() < paddleB.ycor() + 40) and (ball.ycor() > paddleB.ycor() - 40):
        ball.setx(340)
        ball.dx *= -1
        playSound()

    if (ball.xcor() < -340 and ball.xcor() > -350) and (ball.ycor() < paddleA.ycor() + 40) and (ball.ycor() > paddleA.ycor() - 40):
        ball.setx(-340)
        ball.dx *= -1
        playSound()

