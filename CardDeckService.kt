package indigo

class CardDeckService {

    private val cardDeck = CardDeck()

    fun reset() {
        cardDeck.reset()
    }

    fun shuffle() {
        cardDeck.shuffle()
    }

    fun get(numberOfCards: Int): List<Card> {
        return cardDeck.get(numberOfCards)
    }


}