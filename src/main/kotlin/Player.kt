import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import input.Controller
import kotlin.math.abs

private const val ACCELERATION = 20f
private const val JUMP_VELOCITY = 10f
private const val GRAVITY = 20.0f
private const val MAX_VEL = 6f
private const val DAMP = 0.90f

class Player(private val map: Map, x: Float, y: Float) {
    private var accel = Vector2()
    private var vel = Vector2()
    var bounds = Rectangle(x, y, 0.6f, 0.8f)

    var state = PlayerState.IDLE
    var stateTime = 0f
    var dir = PlayerDirection.LEFT
    private var grounded = false
    private val drawable = PlayerDrawable()

    fun update(deltaTime: Float) {
        processKeys()

        accel.y = -GRAVITY
        accel.scl(deltaTime)
        vel.add(accel.x, accel.y)
        if (accel.x == 0f) vel.x *= DAMP
        if (vel.x > MAX_VEL) vel.x = MAX_VEL
        if (vel.x < -MAX_VEL) vel.x = -MAX_VEL
        vel.scl(deltaTime)
        tryMove()
        vel.scl(1.0f / deltaTime)

        stateTime += deltaTime
        println("Player is: ${bounds.x}, ${bounds.y}")
    }

    private fun processKeys() {
        if (Controller.jumpButton.isPressed && state != PlayerState.JUMP) {
            state = PlayerState.JUMP
            vel.y = JUMP_VELOCITY
            grounded = false
        }

        when {
            abs(Controller.xAxis.value) > 0 -> {
                if (state != PlayerState.JUMP) state = PlayerState.RUN
                dir = PlayerDirection.fromNumber(Controller.xAxis.value)
                accel.x = ACCELERATION * Controller.xAxis.value
            }

            abs(Controller.yAxis.value) > 0 -> {
                accel.y = ACCELERATION * Controller.yAxis.value
            }
            else -> {
                if (state != PlayerState.JUMP) state = PlayerState.IDLE
                accel.x = 0f
            }
        }
    }

    fun draw(batch: SpriteBatch) {
        drawable.draw(batch, bounds, state, dir, stateTime)
    }

    private fun tryMove() {
        moveX()
        moveY()
    }

    private fun moveX() {
        if (collides(bounds, Vector2(vel.x, 0f))) {
            if (vel.x > 0) {
                collides(bounds, Vector2(vel.x, 0f))
                val farEdge = (bounds.x + vel.x + bounds.width).toInt()
                bounds.x = farEdge - bounds.width
                vel.x = 0f
            } else {
                collides(bounds, Vector2(vel.x, 0f))
                bounds.x = (bounds.x + vel.x).toInt() + 1f
                vel.x = 0f
            }
        } else {
            bounds.x += vel.x
        }
    }

    private fun moveY() {
        if (collides(bounds, Vector2(0f, vel.y))) {
            if (vel.y > 0) {
                collides(bounds, Vector2(0f, vel.y))
                val farEdge = (bounds.y + vel.y + bounds.height).toInt()
                bounds.y = farEdge - bounds.height
                vel.y = 0f
            } else {
                collides(bounds, Vector2(0f, vel.y))
                bounds.y = (bounds.y + vel.y).toInt() + 1f
                vel.y = 0f
                state = PlayerState.IDLE
                grounded = true
            }
        } else {
            bounds.y += vel.y
        }
    }

    private fun collides(current: Rectangle, vel: Vector2): Boolean {
        return collidesRight(current, vel) || collidesLeft(current, vel) || collidesUp(current, vel) || collidesDown(current, vel)
    }

    private fun collidesRight(current: Rectangle, vel: Vector2): Boolean {
        val farEdge = current.x + current.width + vel.x - .1f
        val destTile = map.getTile(farEdge, current.y + vel.y)
        return vel.x >= 0 && destTile == TILE
    }

    private fun collidesLeft(current: Rectangle, vel: Vector2): Boolean {
        val destTile = map.getTile(current.x + vel.x + .1f, current.y + vel.y)
        return vel.x <= 0 && destTile == TILE
    }

    private fun collidesUp(current: Rectangle, vel: Vector2): Boolean {
        val farEdge = current.y + vel.y + current.height - .1f
        val destTile = map.getTile(current.x + vel.x, farEdge)
        return vel.y >= 0 && destTile == TILE
    }

    private fun collidesDown(current: Rectangle, vel: Vector2): Boolean {
        val destTile = map.getTile(current.x + vel.x, current.y + vel.y +.1f)
        return vel.y >= 0 && destTile == TILE
    }

}