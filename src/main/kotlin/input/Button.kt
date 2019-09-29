package input

import org.lwjgl.input.Controller
import com.badlogic.gdx.Gdx

class Button(private val inputKeys: List<Int> = listOf(), private val gamePad: Controller? = null, inputNames: List<String> = listOf()) {
    constructor(vararg inputKeys: Int) : this(inputKeys.toList())
    private val gamePadButtonIds = getIds(gamePad, inputNames)
    var isPressed = false
    var isFirstFrameSinceChanged = false
    var stateTime = 0f


    fun update(deltaTime: Float) {
        val pressed = isGamepadPressed() || inputKeys.any { Gdx.input.isKeyPressed(it) }

        if (pressed == isPressed) {
            isFirstFrameSinceChanged = false
            stateTime += deltaTime
        } else {
            isPressed = pressed
            isFirstFrameSinceChanged = true
            stateTime = 0f
        }
    }

    private fun getIds(gamePad: Controller?, inputNames: List<String>): List<Int> {
        if (gamePad == null) {
            return listOf()
        }
        return inputNames.map { buttonName ->
            (0 until gamePad.buttonCount).first { gamePad.getButtonName(it) == buttonName }
        }
    }

    private fun isGamepadPressed(): Boolean {
        if (gamePad == null || !input.Controller.gamePadEnabled) {
            return false
        }
        return gamePadButtonIds.any {gamePad.isButtonPressed(it)}
    }

}