package indigo

open class Player {
    val cards = mutableListOf<Card>()

    fun takeCardByNumber(number: Int): Card {
        return cards[number - 1]
    }

    fun charge(cards: List<Card>) {
        this.cards.addAll(cards)
    }

}