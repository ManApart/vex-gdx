import com.badlogic.gdx.math.Vector2

operator fun Vector2.times(magnitude: Float): Vector2 {
    return Vector2(x * magnitude, y * magnitude)
}