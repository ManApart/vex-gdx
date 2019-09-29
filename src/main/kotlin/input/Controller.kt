package input

import com.badlogic.gdx.Input
import org.lwjgl.input.Controllers
import org.lwjgl.input.Controller

object Controller {
    val gamePad = registerController()
    const val deadZone = 0.3f

    val xAxis = Axis(gamePad, "X Axis", false, Button(Input.Keys.D, Input.Keys.RIGHT), Button(Input.Keys.A, Input.Keys.LEFT))
    val yAxis = Axis(gamePad, "Y Axis", true, Button(Input.Keys.W, Input.Keys.UP), Button(Input.Keys.S, Input.Keys.DOWN))

    val jumpButton = Button(listOf(Input.Keys.SPACE), gamePad, listOf("Button 0"))

    fun update(deltaTime: Float) {
        xAxis.update(deltaTime)
        yAxis.update(deltaTime)
        jumpButton.update(deltaTime)
    }

    private fun registerController() : Controller{
        Controllers.create()
        return Controllers.getController(7)
    }

}