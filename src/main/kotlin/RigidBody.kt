import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

const val GRAVITY = 20.0f
const val MAX_VEL = 6f
const val DAMP = 0.90f

class RigidBody(private val map: Map, private val owner: RigidBodyOwner, width: Float, height: Float) {
    var accel = Vector2()
    var vel = Vector2()
    var bounds = Rectangle(0f, 0f, width, height)

    fun update(deltaTime: Float) {
        accel.y = -GRAVITY
        accel.scl(deltaTime)
        vel.add(accel.x, accel.y)
        if (accel.x == 0f) vel.x *= DAMP
        if (vel.x > MAX_VEL) vel.x = MAX_VEL
        if (vel.x < -MAX_VEL) vel.x = -MAX_VEL
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
                collides(bounds, Vector2(vel.x, 0f))
                val farEdge = (bounds.x + vel.x + bounds.width).toInt()
                bounds.x = farEdge - bounds.width
                vel.x = 0f
                owner.onCollided(Direction.RIGHT)
            } else {
                collides(bounds, Vector2(vel.x, 0f))
                bounds.x = (bounds.x + vel.x).toInt() + 1f
                vel.x = 0f
                owner.onCollided(Direction.LEFT)
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
                owner.onCollided(Direction.UP)
            } else {
                collides(bounds, Vector2(0f, vel.y))
                bounds.y = (bounds.y + vel.y).toInt() + 1f
                vel.y = 0f
                owner.onCollided(Direction.DOWN)
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