enum class Direction {
    LEFT,
    RIGHT,
    UP,
    DOWN;
    companion object {
        fun fromNumber(number: Float) : Direction {
            return if (number > 0) {
                RIGHT
            } else {
                LEFT
            }
        }
    }
}
