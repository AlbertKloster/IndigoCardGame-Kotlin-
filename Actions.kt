package indigo

enum class Actions(val string: String) {
    RESET("reset"),
    SHUFFLE("shuffle"),
    GET("get"),
    EXIT("exit");

    companion object {
        fun getAction(input: String): Actions {
            for (action in Actions.values()) {
                if (action.string == input.lowercase()) return action
            }
            throw RuntimeException("Wrong action.")
        }
    }

}