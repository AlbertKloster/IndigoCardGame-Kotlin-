package indigo

open class Player {
    val cardsInHand = mutableListOf<Card>()
    private val cardsWon = mutableListOf<Card>()

    fun takeCardByNumber(number: Int): Card {
        return cardsInHand.removeAt(number - 1)
    }

    fun charge(cards: List<Card>) {
        this.cardsInHand.addAll(cards)
    }

    fun putToCardsWon(cards: List<Card>) {
        cardsWon.addAll(cards)
    }

    fun getScore(): Int {
        return getScore(cardsWon)
    }

    fun getCards(): Int {
        return cardsWon.size
    }

    private fun getScore(cards: List<Card>): Int {
        return cards.count {
            it.rank == Ranks.ACE ||
                    it.rank == Ranks.TEN ||
                    it.rank == Ranks.JACK ||
                    it.rank == Ranks.QUEEN ||
                    it.rank == Ranks.KING
        }
    }


}