enum class PlayerDirection {
    LEFT,
    RIGHT;
    companion object {
        fun fromNumber(number: Float) : PlayerDirection {
            return if (number > 0) {
                RIGHT
            } else {
                LEFT
            }
        }
    }
}
