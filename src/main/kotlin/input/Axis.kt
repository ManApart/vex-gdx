package input

import input.Controller.deadZone
import input.Controller.gamePadEnabled
import org.lwjgl.input.Controller
import kotlin.math.abs

class Axis(
        private val gamePad: Controller,
        axisName: String,
        private val inverted: Boolean = false,
        private val positiveButton: Button = Button(),
        private val negativeButton: Button = Button()
) {
    private val axisIndex = getAxisIndex(gamePad, axisName)
    var value = 0f

    private fun getAxisIndex(gamePad: Controller, axisName: String): Int {
        return (0 until gamePad.axisCount).first { gamePad.getAxisName(it) == axisName }
    }

    fun update(deltaTime: Float) {
        positiveButton.update(deltaTime)
        negativeButton.update(deltaTime)
        value = when {
            positiveButton.isPressed -> 1f
            negativeButton.isPressed -> -1f
            abs(gamePad.getAxisValue(axisIndex)) < deadZone -> 0f
            gamePadEnabled && inverted -> -gamePad.getAxisValue(axisIndex)
            gamePadEnabled -> gamePad.getAxisValue(axisIndex)
            else -> 0f
        }
    }
}