import com.badlogic.gdx.graphics.g2d.SpriteBatch
import input.Controller
import kotlin.math.abs

private const val ACCELERATION = 20f
private const val JUMP_VELOCITY = 10f


class Player(map: Map) : RigidBodyOwner {

    var state = PlayerState.IDLE
    var stateTime = 0f
    var dir = Direction.LEFT
    private var grounded = false
    private var hasDoubleJump = false
    private val drawable = PlayerDrawable()
    val body = RigidBody(map, this, 0.6f, 0.8f)


    override fun onCollided(direction: Direction) {
        println("Collided $direction")
        if (direction == Direction.DOWN) {
            state = PlayerState.IDLE
            grounded = true
            hasDoubleJump = true
        }
    }

    override fun onNoLongerCollided(direction: Direction) {
        println("No longer Collided $direction")
    }

    fun update(deltaTime: Float) {
        processKeys()

        grounded = false
        body.update(deltaTime)

        stateTime += deltaTime
//        println("Player is: ${body.bounds.x}, ${body.bounds.y}")
    }

    private fun processKeys() {
        if (Controller.jumpButton.isFirstPressed()) {
            if (state != PlayerState.JUMP) {
                state = PlayerState.JUMP
                body.vel.y = JUMP_VELOCITY
            } else if (hasDoubleJump) {
                hasDoubleJump = false
                body.vel.y = JUMP_VELOCITY
            }
        }

        when {
            abs(Controller.xAxis.value) > 0 -> {
                if (state == PlayerState.IDLE) {
                    state = PlayerState.RUN
                }
                dir = Direction.fromNumber(Controller.xAxis.value)
                body.accel.x = ACCELERATION * Controller.xAxis.value
            }
            abs(Controller.yAxis.value) > 0 -> {
                body.accel.y = ACCELERATION * Controller.yAxis.value
            }
            else -> {
                if (state != PlayerState.JUMP) {
                    state = PlayerState.IDLE
                }
                body.accel.x = 0f
            }
        }
    }

    fun draw(batch: SpriteBatch) {
        drawable.draw(batch, body.bounds, state, dir, stateTime)
    }

}