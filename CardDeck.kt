package indigo

class CardDeck {

    private val deck = mutableListOf<Card>()

    init {
        reset()
    }

    fun reset() {
        deck.clear()
        for (rank in Ranks.values()) {
            for (suit in Suits.values()) {
                deck.add(Card(rank, suit))
            }
        }
    }

    fun shuffle() {
        deck.shuffle()
    }

    fun get(numberOfCards: Int): List<Card> {
        if (deck.size < numberOfCards) throw RuntimeException("The remaining cards are insufficient to meet the request.")
        val removedCards = mutableListOf<Card>()
        for (i in 0 until numberOfCards) {
            removedCards.add(deck.removeLast())
        }
        return removedCards
    }

    fun isEmpty(): Boolean {
        return deck.isEmpty()
    }

}