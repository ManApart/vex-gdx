package input

import kotlin.math.abs

object ControllerDebugger {
    private val gamePad = Controller.gamePad
    private val ignoredButtons = listOf<Int>()
    private val ignoredAxes = listOf(4)

    fun update() {
        debugButtons()
        debugAxis()
    }

    private fun debugButtons() {
        (0 until gamePad.buttonCount).forEach { i ->
            if (gamePad.isButtonPressed(i) && !ignoredButtons.contains(i)) {
                logButton(i)
            }
        }
    }

    private fun logButton(i: Int) {
        println("${gamePad.getButtonName(i)} [$i] is pressed.")
    }

    private fun debugAxis() {
        (0 until gamePad.axisCount).forEach { i ->
            if (abs(gamePad.getAxisValue(i)) > Controller.deadZone && !ignoredAxes.contains(i)) {
                logAxis(i)
            }
        }
    }

    private fun logAxis(i: Int) {
        println("${gamePad.getAxisName(i)} [$i] is ${gamePad.getAxisValue(i)}.")
    }
}