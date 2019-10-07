import com.badlogic.gdx.math.Vector2

enum class Direction(val vector: Vector2) {
    LEFT(Vector2(-1f, 0f)),
    RIGHT(Vector2(1f, 0f)),
    UP(Vector2(0f, 1f)),
    DOWN(Vector2(0f, -1f));

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
