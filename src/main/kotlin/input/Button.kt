package input

import com.badlogic.gdx.Gdx

class Button(private vararg val inputKeys: Int) {
    var isPressed = false
    var isFirstFrameSinceChanged = false
    var stateTime = 0f

    fun update(deltaTime: Float) {
        val pressed = inputKeys.any { Gdx.input.isKeyPressed(it) }

        if (pressed == isPressed) {
            isFirstFrameSinceChanged = false
            stateTime += deltaTime
        } else {
            isPressed = pressed
            isFirstFrameSinceChanged = true
            stateTime = 0f
        }
    }

}