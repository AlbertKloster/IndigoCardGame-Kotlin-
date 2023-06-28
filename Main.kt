package indigo

val cardDeckService = CardDeckService()

fun main() {
    var exit = false
    while (!exit) {
        try {
            println("Choose an action (reset, shuffle, get, exit):")
            when (Actions.getAction(readln())) {
                Actions.EXIT -> exit = true
                Actions.RESET -> reset()
                Actions.SHUFFLE -> shuffle()
                Actions.GET -> get()
            }
        } catch (e: RuntimeException) {
            println(e.message)
        }

    }
    println("Bye")
}

private fun reset() {
    cardDeckService.reset()
    println("Card deck is reset.")
}

private fun shuffle() {
    cardDeckService.shuffle()
    println("Card deck is shuffled.")
}

private fun get() {
    println("Number of cards:")
    val input = readln()
    if (!input.matches(Regex("[1-4]?[0-9]|5[0-2]"))) throw RuntimeException("Invalid number of cards.")
    val numberOfCards = input.toInt()
    if (numberOfCards == 0) throw RuntimeException("Invalid number of cards.")
    val removedCards = cardDeckService.get(numberOfCards)
    println(removedCards.joinToString(" ") { "${it.rank.string}${it.suit.string}" })
}