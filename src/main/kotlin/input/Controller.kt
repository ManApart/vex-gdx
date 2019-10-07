package input

import com.badlogic.gdx.Input
import org.lwjgl.input.Controllers
import org.lwjgl.input.Controller

object Controller {
    val gamePad = registerController()
    const val deadZone = 0.3f
    const val gamePadEnabled = true

    val xAxis = Axis(gamePad, "X Axis", positiveButton = Button(Input.Keys.RIGHT), negativeButton = Button(Input.Keys.LEFT))
    val yAxis = Axis(gamePad, "Y Axis", true, positiveButton = Button(Input.Keys.UP), negativeButton = Button(Input.Keys.DOWN))

    val xAim = Axis(gamePad, "X Rotation", positiveButton = Button(Input.Keys.D), negativeButton = Button(Input.Keys.A))
    val yAim = Axis(gamePad, "Y Rotation", true, positiveButton = Button(Input.Keys.W), negativeButton = Button(Input.Keys.S))

    val grapple = Axis(gamePad, "Z Axis", ignorePositive = true, positiveButton =  Button(Input.Keys.SHIFT_LEFT))
    val ascend = Axis(gamePad, "Z Axis", ignoreNegative = true, positiveButton =  Button(Input.Keys.SHIFT_RIGHT))

    val jump = Button(listOf(Input.Keys.SPACE), gamePad, listOf("Button 0"))
    val dashLeft = Button(listOf(Input.Keys.Z), gamePad, listOf("Button 4"))
    val dashRight = Button(listOf(Input.Keys.X), gamePad, listOf("Button 5"))

    fun update(deltaTime: Float) {
        xAxis.update(deltaTime)
        yAxis.update(deltaTime)
        yAim.update(deltaTime)
        yAim.update(deltaTime)
        grapple.update(deltaTime)
        ascend.update(deltaTime)
        jump.update(deltaTime)
        dashLeft.update(deltaTime)
        dashRight.update(deltaTime)
    }

    private fun registerController(): Controller {
        Controllers.create()
        return Controllers.getController(7)
    }

}