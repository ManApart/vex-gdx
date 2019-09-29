package input

import com.badlogic.gdx.Input

object Controller {
    //        Controllers.getController(0).getAxisName()
//    val leftStick
    val leftButton = Button(Input.Keys.A, Input.Keys.LEFT)
    val rightButton = Button(Input.Keys.D, Input.Keys.RIGHT)
    val jumpButton = Button(Input.Keys.SPACE)

    fun update(deltaTime: Float) {
        leftButton.update(deltaTime)
        rightButton.update(deltaTime)
        jumpButton.update(deltaTime)
    }

}