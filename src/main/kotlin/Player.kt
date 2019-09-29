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

//        accel.y = -GRAVITY
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
//        val destXTile = map.tiles[bounds.x.toInt()][bounds.y.toInt()]
        val destXTile = map.tiles[bounds.x.toInt()][map.tiles[0].size -1 - bounds.y.toInt()]
        bounds.x = when {
            destXTile == TILE && vel.x > 0 -> bounds.x.toInt() - 0.1f
            destXTile == TILE && vel.x < 0 -> bounds.x.toInt() + 1.1f
            else -> bounds.x
        }

        bounds.y += vel.y
//        val destYTile = map.tiles[bounds.x.toInt()][bounds.y.toInt()]
        val destYTile = map.tiles[bounds.x.toInt()][map.tiles[0].size -1 - bounds.y.toInt()]
        bounds.y = when {
            destYTile == TILE && vel.y > 0 -> bounds.y.toInt() - 0.1f
            destYTile == TILE && vel.y < 0 -> bounds.y.toInt() + 1.1f
            else -> bounds.y
        }

//        bounds.x += vel.x
//        fetchCollidableRects().forEach { rect ->
//            if (bounds.overlaps(rect)) {
//                if (vel.x < 0) {
//                    bounds.x = rect.x + rect.width + 0.01f
//                } else {
//                    bounds.x = rect.x - bounds.width - 0.01f
//                }
//                vel.x = 0f
//            }
//        }
//
//        bounds.y += vel.y
//        fetchCollidableRects().forEach { rect ->
//            if (bounds.overlaps(rect)) {
//                if (vel.y < 0) {
//                    bounds.y = rect.y + rect.height + 0.01f
//                } else {
//                    bounds.y = rect.y - bounds.height - 0.01f
//                }
//                vel.y = 0f
//            }
//        }
    }

    private fun fetchCollidableRects(): List<Rectangle> {
        val p1x = bounds.x.toInt()
        val p1y = floor(bounds.y).toInt()
        val p2x = (bounds.x + bounds.width).toInt()
        val p2y = floor(bounds.y).toInt()
        val p3x = (bounds.x + bounds.width).toInt()
        val p3y = (bounds.y + bounds.height).toInt()
        val p4x = bounds.x.toInt()
        val p4y = (bounds.y + bounds.height).toInt()

        val tiles = map.tiles

        if (outOfBounds(p1x, tiles.size)
                || outOfBounds(p2x, tiles.size)
                || outOfBounds(p3x, tiles.size)
                || outOfBounds(p4x, tiles.size)
        ) {
            return listOf()
        }

        val r = listOf(Rectangle(), Rectangle(), Rectangle(), Rectangle(), Rectangle())

        val tile1 = tiles[p1x][tiles[0].size - 1 - p1y]
        val tile2 = tiles[p2x][tiles[0].size - 1 - p2y]
        val tile3 = tiles[p3x][tiles[0].size - 1 - p3y]
        val tile4 = tiles[p4x][tiles[0].size - 1 - p4y]

        if (tile1 == TILE)
            r[0].set(p1x.toFloat(), p1y.toFloat(), 1f, 1f)
        else
            r[0].set(-1f, -1f, 0f, 0f)

        if (tile2 == TILE)
            r[1].set(p2x.toFloat(), p2y.toFloat(), 1f, 1f)
        else
            r[1].set(-1f, -1f, 0f, 0f)

        if (tile3 == TILE)
            r[2].set(p3x.toFloat(), p3y.toFloat(), 1f, 1f)
        else
            r[2].set(-1f, -1f, 0f, 0f)

        if (tile4 == TILE)
            r[3].set(p4x.toFloat(), p4y.toFloat(), 1f, 1f)
        else {
            r[3].set(-1f, -1f, 0f, 0f)
        }

        r[4].set(-1f, -1f, 0f, 0f)
        return r
    }

    private fun outOfBounds(check: Int, max: Int): Boolean {
        return check < 0 || check >= max
    }

}