import com.badlogic.gdx.math.MathUtils.clamp
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

const val MAX_X_VEL = 6f
const val MAX_Y_VEL = 10f
const val DAMP = 0.90f

class RigidBody(private val map: Map, private val owner: RigidBodyOwner, width: Float, height: Float) {
    var accel = Vector2()
    var vel = Vector2()
    var bounds = Rectangle(0f, 0f, width, height)
    private val collided = createCollidedMap()

    private fun createCollidedMap(): MutableMap<Direction, Boolean> {
        val map = mutableMapOf<Direction, Boolean>()
        Direction.values().forEach { map[it] = false }
        return map
    }

    fun isCollidedAny(vararg directions: Direction): Boolean {
        return directions.any { isCollided(it) }
    }

    fun isCollided(direction: Direction): Boolean {
        return collided[direction]!!
    }

    private fun setNowCollided(direction: Direction) {
        val wasCollided = isCollided(direction)
        collided[direction] = true
        if (!wasCollided) {
            owner.onCollided(direction)
        }
    }

    private fun setNoLongerCollided(direction: Direction) {
        val wasCollided = isCollided(direction)
        collided[direction] = false
        if (wasCollided) {
            owner.onNoLongerCollided(direction)
        }
    }

    fun update(deltaTime: Float, ignoreXMax: Boolean = false, ignoreYMax: Boolean = false) {
        accel.scl(deltaTime)
        vel.add(accel.x, accel.y)
        if (accel.x == 0f) vel.x *= DAMP

        if (!ignoreXMax) {
            vel.x = clamp(vel.x, -MAX_X_VEL, MAX_X_VEL)
        }

        if (!ignoreYMax) {
            vel.y = clamp(vel.y, -MAX_Y_VEL, MAX_Y_VEL)
        }

        vel.scl(deltaTime)
        tryMove()
        vel.scl(1.0f / deltaTime)
    }

    private fun tryMove() {
        moveX()
        moveY()
    }

    private fun moveX() {
        if (collides(bounds, Vector2(vel.x, 0f))) {
            if (vel.x > 0) {
                val farEdge = (bounds.x + vel.x + bounds.width).toInt()
                bounds.x = farEdge - bounds.width
                vel.x = 0f
                setNowCollided(Direction.RIGHT)
                checkDirectionNoLongerCollides(Direction.LEFT)
            } else {
                bounds.x = (bounds.x + vel.x).toInt() + 1f
                vel.x = 0f
                setNowCollided(Direction.LEFT)
                checkDirectionNoLongerCollides(Direction.RIGHT)
            }
        } else {
            bounds.x += vel.x
            checkDirectionNoLongerCollides(Direction.LEFT)
            checkDirectionNoLongerCollides(Direction.RIGHT)
        }
    }

    private fun moveY() {
        if (collides(bounds, Vector2(0f, vel.y))) {
            if (vel.y > 0) {
                val farEdge = (bounds.y + vel.y + bounds.height).toInt()
                bounds.y = farEdge - bounds.height
                vel.y = 0f
                setNowCollided(Direction.UP)
                checkDirectionNoLongerCollides(Direction.DOWN)
            } else {
                bounds.y = (bounds.y + vel.y).toInt() + 1f
                vel.y = 0f
                setNowCollided(Direction.DOWN)
                checkDirectionNoLongerCollides(Direction.UP)
            }
        } else {
            bounds.y += vel.y
            checkDirectionNoLongerCollides(Direction.UP)
            checkDirectionNoLongerCollides(Direction.DOWN)
        }
    }

    private fun checkDirectionNoLongerCollides(direction: Direction) {
        if (!collides(direction, bounds, direction.vector * .2f)) {
            setNoLongerCollided(direction)
        }
    }

    private fun collides(current: Rectangle, vel: Vector2): Boolean {
        return collidesRight(current, vel) || collidesLeft(current, vel) || collidesUp(current, vel) || collidesDown(current, vel)
    }

    private fun collides(direction: Direction, current: Rectangle, vel: Vector2): Boolean {
        return when (direction) {
            Direction.RIGHT -> collidesRight(current, vel)
            Direction.LEFT -> collidesLeft(current, vel)
            Direction.UP -> collidesUp(current, vel)
            Direction.DOWN -> collidesDown(current, vel)
        }
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
        val destTile = map.getTile(current.x + vel.x, current.y + vel.y + .1f)
        return vel.y >= 0 && destTile == TILE
    }

}