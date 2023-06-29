package indigo

const val TOTAL_CARDS = 52
const val FIRST_DECK = 4
val cardDeckService = CardDeckService()
val cardsOnTable = mutableListOf<Card>()
val players = mutableListOf<Player>()
var exit = false
class Game {
    fun play() {
        val human: Player = PlayerHuman()
        val computer: Player = PlayerComputer()
        var lastWinner = human
        println("Indigo Card Game")
        val playFirst = getPlayFirst()

        prepareDeck()

        println("Initial cards on the table: ${cardsOnTable.joinToString(" ") { getCardSymbol(it) }}")

        players.addAll(listOf(human, computer))

        var currentPlayerIndex = if (playFirst) 0 else 1
        while (!exit) {
            val currentPlayer = players[currentPlayerIndex]

            printCardsOnTable()

            if (cardDeckService.isEmptyDeck() && currentPlayer.cardsInHand.isEmpty()) {
                lastWinner.putToCardsWon(cardsOnTable)
                printFinalScore(human, computer, playFirst)
                break
            }

            if (cardsOnTable.size >= TOTAL_CARDS) break
            if (currentPlayer.cardsInHand.isEmpty()) currentPlayer.charge(get(6))

            playCard(currentPlayer)

            if (hasWon()) {
                currentPlayer.putToCardsWon(cardsOnTable)
                cardsOnTable.clear()
                println("${if (currentPlayer is PlayerHuman) "Player" else "Computer"} wins cards")
                printScore(human, computer)
                lastWinner = currentPlayer
            }

            currentPlayerIndex = (currentPlayerIndex + 1) % 2
        }
        println("Game Over")
    }
}


private fun prepareDeck() {
    reset()
    shuffle()
    cardsOnTable.addAll(get(FIRST_DECK))
}

private fun hasWon(): Boolean {
    if (cardsOnTable.size > 1 && !exit) {
        if (getLastCardOnTable().rank == getSecondLastCardOnTable().rank ||
            getLastCardOnTable().suit == getSecondLastCardOnTable().suit
        ) return true
    }
    return false
}


private fun playCard(currentPlayer: Player) {
    if (currentPlayer is PlayerHuman) {
        printCardsInHand(currentPlayer)
        playCardHuman(currentPlayer)
    } else {
        println(currentPlayer.cardsInHand.joinToString(" ") { it.rank.string + it.suit.string })
        val card = playCardComputer(currentPlayer)
        currentPlayer.cardsInHand.remove(card)
        cardsOnTable.add(card)
        println("Computer plays ${getCardSymbol(card)}")
    }
}

private fun playCardHuman(currentPlayer: Player) {
    while (!exit) {
        println("Choose a card to play (1-${currentPlayer.cardsInHand.size}):")
        val input = readln()
        if (isNotExitOrNumber(input)) continue
        exit = isExit(input)

        if (isNumber(input)) {
            val cardIndex = input.toInt()
            if (cardIndex in 1..currentPlayer.cardsInHand.size) {
                cardsOnTable.add(currentPlayer.takeCardByNumber(cardIndex))
                break
            }
        }
    }
}

private fun playCardComputer(player: Player): Card {
    if (player.cardsInHand.size == 1) return player.cardsInHand[0]

    if (cardsOnTable.isEmpty()) return getSameSuitOrRankCard(player)

    val candidateCards = getCandidateCards(player)
    if (candidateCards.size == 1) return candidateCards.first()

    if (candidateCards.isEmpty()) return getSameSuitOrRankCard(player)

    val sameSuitCards = getSameSuitCards(candidateCards)
    if (sameSuitCards.isNotEmpty()) return sameSuitCards.first()

    val sameRankCards = getSameRankCards(candidateCards)
    if (sameRankCards.isNotEmpty()) return sameRankCards.first()

    return candidateCards.first()
}

private fun getSameSuitOrRankCard(player: Player): Card {
    val sameSuitCards = getSameSuitCards(player.cardsInHand.toSet())
    if (sameSuitCards.isNotEmpty()) return sameSuitCards.first()

    val sameRankCards = getSameRankCards(player.cardsInHand.toSet())
    if (sameRankCards.isNotEmpty()) return sameRankCards.first()

    return player.cardsInHand[0]
}

private fun getSameRankCards(cards: Set<Card>): Set<Card> {
    val sameRankCards = mutableSetOf<Card>()
    for (card in cards) {
        if (cards.count { it.rank == card.rank } > 1)
            sameRankCards.add(card)
    }
    return sameRankCards
}

private fun getSameSuitCards(cards: Set<Card>): Set<Card> {
    val sameSuitCards = mutableSetOf<Card>()
    for (card in cards) {
        if (cards.count { it.suit == card.suit } > 1)
            sameSuitCards.add(card)
    }
    return sameSuitCards
}

private fun getCandidateCards(player: Player): Set<Card> {
    val candidateCards = mutableSetOf<Card>()
    for (cardInHand in player.cardsInHand) {
        if (cardsOnTable.last().suit == cardInHand.suit || cardsOnTable.last().rank == cardInHand.rank)
            candidateCards.add(cardInHand)
    }
    return candidateCards
}

private fun isNumber(input: String) = input.matches(Regex("\\d+"))

private fun isExit(input: String): Boolean = input == "exit"

private fun isNotExitOrNumber(input: String) = !(isExit(input) || isNumber(input))

private fun printCardsInHand(currentPlayer: Player) {
    val builder = StringBuilder("Cards in hand: ")
    for (i in currentPlayer.cardsInHand.indices) {
        builder.append(i + 1)
        builder.append(")")
        builder.append(getCardSymbol(currentPlayer.cardsInHand[i]))
        builder.append(" ")
    }
    println(builder)
}

private fun printCardsOnTable() = println(
    if (cardsOnTable.isEmpty()) "\nNo cards on the table" else "\n${cardsOnTable.size} cards on the table, and the top card is ${
        getCardSymbol(cardsOnTable.last())
    }"
)

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
    } else if (scoreHuman > scoreComputer) {
        scoreHuman += 3
    } else {
        scoreComputer += 3
    }

    println("Score: Player $scoreHuman - Computer $scoreComputer")
    println("Cards: Player ${human.getCards()} - Computer ${computer.getCards()}")
}

private fun getLastCardOnTable() = cardsOnTable[cardsOnTable.lastIndex]

private fun getSecondLastCardOnTable() = cardsOnTable[cardsOnTable.lastIndex - 1]

private fun getCardSymbol(card: Card) = card.rank.string + card.suit.string

private fun getPlayFirst(): Boolean {
    while (true) {
        println("Play first?")
        val input = readln()
        if (input == "yes") return true
        if (input == "no") return false
    }
}

private fun reset() = cardDeckService.reset()

private fun shuffle() = cardDeckService.shuffle()

private fun get(numberOfCards: Int) = cardDeckService.get(numberOfCards)
