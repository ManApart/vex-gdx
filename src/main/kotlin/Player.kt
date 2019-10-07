import com.badlogic.gdx.graphics.g2d.SpriteBatch
import input.Controller
import kotlin.math.abs

const val GRAVITY = 20.0f
private const val ACCELERATION = 20f
private const val JUMP_VELOCITY = 10f
private const val JUMP_TIME = .1f
private const val DASH_VELOCITY = 50f
private const val DASH_TIME = 50f


class Player(map: Map) : RigidBodyOwner {

    var state = PlayerState.FALLING
    var stateTime = 0f
    var dir = Direction.LEFT
    private var hasDoubleJump = false
    private val drawable = PlayerDrawable()
    val body = RigidBody(map, this, 0.6f, 0.8f)


    override fun onCollided(direction: Direction) {
//        println("Collided $direction")
        if (direction == Direction.DOWN) {
            setPlayerState(PlayerState.RUNING)
            hasDoubleJump = true
        }
    }

    override fun onNoLongerCollided(direction: Direction) {
//        println("No longer Collided $direction")
        if (direction == Direction.DOWN){
            if (state != PlayerState.JUMPING){
                setPlayerState(PlayerState.FALLING)
            }
        }
    }

    private fun setPlayerState(state: PlayerState) {
//        println("${this.state} -> $state")
        this.state = state
        this.stateTime = 0f
    }

    fun update(deltaTime: Float) {
        processKeys()
        updateState(deltaTime)
        body.update(deltaTime)

//        println("Player is: ${body.bounds.x}, ${body.bounds.y}")
    }

    private fun processKeys() {
        if (Controller.jump.isFirstPressed()) {
            if (body.isCollided(Direction.DOWN)) {
                setPlayerState(PlayerState.JUMPING)
            } else if (hasDoubleJump) {
                hasDoubleJump = false
                setPlayerState(PlayerState.JUMPING)
            }
        }

//        if (Controller.dashLeft.isFirstPressed()) {
//            if (state != PlayerState.DASHING) {
//                dir = Direction.LEFT
//                setPlayerState(PlayerState.DASHING)
//            }
//        }
//
//        if (Controller.dashRight.isFirstPressed()) {
//            if (state != PlayerState.DASHING) {
//                dir = Direction.RIGHT
//                setPlayerState(PlayerState.DASHING)
//            }
//        }

        if (abs(Controller.xAxis.value) > 0) {
            if (body.isCollided(Direction.DOWN) && state == PlayerState.IDLE) {
                setPlayerState(PlayerState.RUNING)
            }
            dir = Direction.fromNumber(Controller.xAxis.value)
            body.accel.x = ACCELERATION * Controller.xAxis.value
        } else if (state == PlayerState.RUNING) {
            if (body.isCollided(Direction.DOWN)) {
                setPlayerState(PlayerState.IDLE)
            }
        }

        if (abs(Controller.yAxis.value) > 0) {
            body.accel.y = ACCELERATION * Controller.yAxis.value
        }
    }

    private fun updateState(deltaTime: Float) {
        stateTime += deltaTime

        if (state == PlayerState.JUMPING) {
            if (stateTime < JUMP_TIME) {
                body.vel.y = JUMP_VELOCITY
            } else {
                setPlayerState(PlayerState.FALLING)
            }
        }

        if (state == PlayerState.DASHING) {
            if (stateTime < DASH_TIME) {
                body.vel.y = 0f
                body.vel.x = dir.vector.x * DASH_VELOCITY
            } else {
                setPlayerState(PlayerState.IDLE)
            }
        }

        if (state.isInState(PlayerState.FALLING, PlayerState.RUNING, PlayerState.IDLE)) {
            body.accel.y = -GRAVITY
        }

        if (state == PlayerState.IDLE) {
            body.accel.x = 0f
        }
    }

    fun draw(batch: SpriteBatch) {
        drawable.draw(batch, body.bounds, state, dir, stateTime)
    }

}