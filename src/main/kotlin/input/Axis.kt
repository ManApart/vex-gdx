package input

import input.Controller.deadZone
import input.Controller.gamePadEnabled
import org.lwjgl.input.Controller
import kotlin.math.abs

class Axis(
        private val gamePad: Controller,
        axisName: String,
        private val inverted: Boolean = false,
        private val ignorePositive: Boolean = false,
        private val ignoreNegative: Boolean = false,
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
        val rawValue = gamePad.getAxisValue(axisIndex)
        value = when {
            positiveButton.isPressed -> 1f
            negativeButton.isPressed -> -1f
            abs(rawValue) < deadZone -> 0f
            rawValue > 0 && ignorePositive -> 0f
            rawValue < 0 && ignoreNegative -> 0f
            gamePadEnabled && inverted -> -rawValue
            gamePadEnabled -> rawValue
            else -> 0f
        }
    }
}