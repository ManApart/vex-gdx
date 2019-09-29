import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import input.Controller
import kotlin.math.floor

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
            Controller.leftButton.isPressed -> {
                if (state != PlayerState.JUMP) state = PlayerState.RUN
                dir = PlayerDirection.LEFT
                accel.x = -ACCELERATION
            }
            Controller.rightButton.isPressed -> {
                if (state != PlayerState.JUMP) state = PlayerState.RUN
                dir = PlayerDirection.RIGHT
                accel.x = ACCELERATION
            }
            Controller.upButton.isPressed -> {
                accel.y = ACCELERATION
            }
            Controller.downButton.isPressed -> {
                accel.y = -ACCELERATION
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
        bounds.x += vel.x
        if (vel.x > 0) {
            val farEdge = (bounds.x + bounds.width).toInt()
            val destTile = getTile(farEdge, bounds.y.toInt())
            if (destTile == TILE) {
                bounds.x = farEdge - bounds.width
                vel.x = 0f
            }
        } else {
            val destTile = getTile(bounds.x.toInt(), bounds.y.toInt())
            if (destTile == TILE) {
                bounds.x = bounds.x.toInt() + 1f
                vel.x = 0f
            }
        }

        bounds.y += vel.y
        if (vel.y > 0) {
            val farEdge = (bounds.y + bounds.height).toInt()
            val destTile = getTile(bounds.x.toInt(), farEdge)
            if (destTile == TILE) {
                bounds.y = farEdge - bounds.height
                vel.y = 0f
            }
        } else {
            val destTile = getTile(bounds.x.toInt(), bounds.y.toInt())
            if (destTile == TILE) {
                bounds.y = bounds.y.toInt() + 1f
                vel.y = 0f
                state = PlayerState.IDLE
                grounded = true
            }
        }
    }

    private fun getTile(x: Int, y: Int): Int {
        if (x < 0 || x >= map.tiles.size || y < 0 || y >= map.tiles[x].size) {
            return 0
        }
        return map.tiles[x][map.tiles[x].size - 1 - y]
    }

}