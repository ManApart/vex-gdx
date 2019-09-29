package input

import com.badlogic.gdx.Input

object Controller {
    //        Controllers.getController(0).getAxisName()
//    val leftStick
    val leftButton = Button(Input.Keys.A, Input.Keys.LEFT)
    val rightButton = Button(Input.Keys.D, Input.Keys.RIGHT)
    val upButton = Button(Input.Keys.W, Input.Keys.UP)
    val downButton = Button(Input.Keys.S, Input.Keys.DOWN)
    val jumpButton = Button(Input.Keys.SPACE)

    fun update(deltaTime: Float) {
        leftButton.update(deltaTime)
        rightButton.update(deltaTime)
        upButton.update(deltaTime)
        downButton.update(deltaTime)
        jumpButton.update(deltaTime)
    }

}