package indigo

val cardDeckService = CardDeckService()
val cardsOnTable = mutableListOf<Card>()
val players = mutableListOf<Player>()

fun main() {
    val human: Player = PlayerHuman()
    val computer: Player = PlayerComputer()
    var lastWinner = human
    println("Indigo Card Game")
    val playFirst = getPlayFirst()
    reset()
    shuffle()
    cardsOnTable.addAll(get(4))

    println("Initial cards on the table: ${cardsOnTable.joinToString(" ") { getCardSymbol(it) }}")

    players.addAll(listOf(human, computer))

    var currentPlayerIndex = if (playFirst) 0 else 1
    var exit = false
    while (!exit) {
        val currentPlayer = players[currentPlayerIndex]

        println()
        if (cardsOnTable.isEmpty())
            println("No cards on the table")
        else
            println("${cardsOnTable.size} cards on the table, and the top card is ${getCardSymbol(cardsOnTable.last())}")

        if (cardDeckService.isEmptyDeck() && currentPlayer.cardsInHand.isEmpty()) {
            lastWinner.putToCardsWon(cardsOnTable)
            printFinalScore(human, computer, playFirst)
            break
        }

        if (cardsOnTable.size >= 52) break
        if (currentPlayer.cardsInHand.isEmpty()) currentPlayer.charge(get(6))
        if (currentPlayer is PlayerHuman) {
            val builder = StringBuilder("Cards in hand: ")
            for (i in currentPlayer.cardsInHand.indices) {
                builder.append(i + 1)
                builder.append(")")
                builder.append(getCardSymbol(currentPlayer.cardsInHand[i]))
                builder.append(" ")
            }
            println(builder)
            while (true) {
                println("Choose a card to play (1-${currentPlayer.cardsInHand.size}):")
                val input = readln()

                if (!input.matches(Regex("exit|\\d+"))) continue

                if (input == "exit") {
                    exit = true
                    break
                } else {
                    val cardIndex = input.toInt()
                    if (cardIndex in 1..currentPlayer.cardsInHand.size) {
                        cardsOnTable.add(currentPlayer.takeCardByNumber(cardIndex))
                        break
                    }
                }
            }
        } else {
            val card = currentPlayer.cardsInHand.removeAt(0)
            cardsOnTable.add(card)
            println("Computer plays ${getCardSymbol(card)}")
        }

        if (cardsOnTable.size > 1 && !exit) {
            if (getLastCardOnTable().rank == getSecondLastCardOnTable().rank ||
                getLastCardOnTable().suit == getSecondLastCardOnTable().suit
                ) {
                currentPlayer.putToCardsWon(cardsOnTable)
                cardsOnTable.clear()
                println("${if (currentPlayer is PlayerHuman) "Player" else "Computer"} wins cards")
                printScore(human, computer)
                lastWinner = currentPlayer
            }
        }

        currentPlayerIndex = (currentPlayerIndex + 1) % 2
    }
    println("Game Over")
}

private fun printScore(human: Player, computer: Player) {
    println("Score: Player ${human.getScore()} - Computer ${computer.getScore()}")
    println("Cards: Player ${human.getCards()} - Computer ${computer.getCards()}")
}

private fun printFinalScore(human: Player, computer: Player, playFirst: Boolean) {
    var scoreHuman = human.getScore()
    var scoreComputer = computer.getScore()

    if (scoreHuman == scoreComputer) {
        if (playFirst) scoreHuman += 3
        else scoreComputer += 3
    }

    if (scoreHuman > scoreComputer) scoreHuman += 3
    else scoreComputer += 3

    println("Score: Player $scoreHuman - Computer $scoreComputer")
    println("Cards: Player ${human.getCards()} - Computer ${computer.getCards()}")
}

private fun getLastCardOnTable(): Card {
    return cardsOnTable[cardsOnTable.lastIndex]
}

private fun getSecondLastCardOnTable(): Card {
    return cardsOnTable[cardsOnTable.lastIndex - 1]
}

private fun getCardSymbol(card: Card) = card.rank.string + card.suit.string

private fun getPlayFirst(): Boolean {
    while (true) {
        println("Play first?")
        val input = readln()
        if (input == "yes") return true
        if (input == "no") return false
    }
}

private fun reset() {
    cardDeckService.reset()
}

private fun shuffle() {
    cardDeckService.shuffle()
}

private fun get(numberOfCards: Int) = cardDeckService.get(numberOfCards)
