package indigo

val cardDeckService = CardDeckService()
val cardsOnTable = mutableListOf<Card>()
val players = mutableListOf<Player>()

fun main() {
    println("Indigo Card Game")
    val playFirst = getPlayFirst()
    reset()
    shuffle()
    cardsOnTable.addAll(get(4))

    println("Initial cards on the table: ${cardsOnTable.joinToString(" ") { getCardSymbol(it) }}")

    val human: Player = PlayerHuman()
    val ai: Player = PlayerAI()

    players.addAll(listOf(human, ai))

    var currentPlayerIndex = if (playFirst) 0 else 1
    var exit = false
    while (!exit) {
        val player = players[currentPlayerIndex]
        println("${cardsOnTable.size} cards on the table, and the top card is ${getCardSymbol(cardsOnTable.last())}")
        if (cardsOnTable.size >= 52) break
        if (player.cards.isEmpty()) player.charge(get(6))
        if (player is PlayerHuman) {
            val builder = StringBuilder("Cards in hand: ")
            for (i in player.cards.indices) {
                builder.append(i + 1)
                builder.append(")")
                builder.append(getCardSymbol(player.cards[i]))
                builder.append(" ")
            }
            println(builder)
            while (true) {
                println("Choose a card to play (1-${player.cards.size}):")
                val input = readln()

                if (!input.matches(Regex("exit|\\d+"))) continue

                if (input == "exit") {
                    exit = true
                    break
                } else {
                    val cardIndex = input.toInt() - 1
                    if (cardIndex in 0 until player.cards.size) {
                        cardsOnTable.add(player.cards.removeAt(cardIndex))
                        break
                    }
                }
            }
        } else {
            val card = player.cards.removeAt(0)
            cardsOnTable.add(card)
            println("Computer plays ${getCardSymbol(card)}")
        }
        currentPlayerIndex = (currentPlayerIndex + 1) % 2
    }
    println("Game Over")
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
