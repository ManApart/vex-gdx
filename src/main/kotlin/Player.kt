import com.badlogic.gdx.graphics.g2d.SpriteBatch
import input.Controller
import kotlin.math.abs

const val GRAVITY = 20.0f
private const val ACCELERATION = 20f

private const val JUMP_VELOCITY = 10f
private const val WALL_JUMP_KICKOFF_VELOCITY = 6f
private const val JUMP_TIME = .1f

private const val DASH_VELOCITY = 20f
private const val DASH_TIME = .15f


class Player(map: Map) : RigidBodyOwner {

    var state = PlayerState.FALLING
    var stateTime = 0f
    var dir = Direction.LEFT
    private var hasDoubleJump = false
    private val drawable = PlayerDrawable()
    val body = RigidBody(map, this, 0.6f, 0.8f)


    override fun onCollided(direction: Direction) {
//        println("Collided $direction")
        when (direction) {
            Direction.UP -> {
            }
            Direction.DOWN -> {
                if (state != PlayerState.DASHING) {
                    setPlayerState(PlayerState.RUNING)
                    hasDoubleJump = true
                }
            }
            Direction.LEFT -> {
                if (state == PlayerState.DASHING) {
                    setStoppedState()
                }
            }
            Direction.RIGHT -> {
                if (state == PlayerState.DASHING) {
                    setStoppedState()
                }
            }
        }
    }

    override fun onNoLongerCollided(direction: Direction) {
//        println("No longer Collided $direction")
        if (direction == Direction.DOWN) {
            if (!state.isInState(PlayerState.JUMPING, PlayerState.DASHING)) {
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
        body.update(deltaTime, ignoreXMax(), ignoreYMax())

//        println("Player is: ${body.bounds.x}, ${body.bounds.y}")
    }

    private fun processKeys() {
        if (Controller.jump.isFirstPressed()) {
            if (state != PlayerState.JUMPING && body.isCollidedAny(Direction.DOWN, Direction.LEFT, Direction.RIGHT)) {
                setPlayerState(PlayerState.JUMPING)
                if (!isGrounded()) {
                    if (body.isCollided(Direction.RIGHT)) {
                        body.vel.x = -WALL_JUMP_KICKOFF_VELOCITY
                    } else if (body.isCollided(Direction.LEFT)) {
                        body.vel.x = WALL_JUMP_KICKOFF_VELOCITY
                    }
                }
            } else if (hasDoubleJump) {
                hasDoubleJump = false
                setPlayerState(PlayerState.JUMPING)
            }
        }

        if (Controller.dashLeft.isFirstPressed()) {
            if (state != PlayerState.DASHING) {
                dir = Direction.LEFT
                setPlayerState(PlayerState.DASHING)
            }
        }

        if (Controller.dashRight.isFirstPressed()) {
            if (state != PlayerState.DASHING) {
                dir = Direction.RIGHT
                setPlayerState(PlayerState.DASHING)
            }
        }

        if (abs(Controller.xAxis.value) > 0) {
            if (isGrounded() && state == PlayerState.IDLE) {
                setPlayerState(PlayerState.RUNING)
            }
            dir = Direction.fromNumber(Controller.xAxis.value)
            body.accel.x = ACCELERATION * Controller.xAxis.value
        } else if (state == PlayerState.RUNING) {
            if (isGrounded()) {
                setPlayerState(PlayerState.IDLE)
            }
        }

        if (abs(Controller.yAxis.value) > 0) {
//            body.accel.y = ACCELERATION * Controller.yAxis.value
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
                setStoppedState()
            }
        }

        if (state.isInState(PlayerState.FALLING, PlayerState.RUNING, PlayerState.IDLE)) {
            body.accel.y = -GRAVITY
        }

        if (state == PlayerState.IDLE) {
            body.accel.x = 0f
        }
    }

    private fun isGrounded() = body.isCollided(Direction.DOWN)

    private fun ignoreXMax(): Boolean {
        return state.isInState(PlayerState.DASHING)
    }

    private fun ignoreYMax(): Boolean {
        return state.isInState(PlayerState.JUMPING)
    }

    private fun setStoppedState() {
        if (isGrounded()) {
            setPlayerState(PlayerState.IDLE)
        } else {
            setPlayerState(PlayerState.FALLING)
        }
    }

    fun draw(batch: SpriteBatch) {
        drawable.draw(batch, body.bounds, state, dir, stateTime)
    }


}