import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import kotlin.math.abs
import kotlin.math.floor

val LEFT = -1
val RIGHT = 1
private val ACCELERATION = 20f
private val JUMP_VELOCITY = 10f
private val GRAVITY = 20.0f
private val MAX_VEL = 6f
private val DAMP = 0.90f

class Player(private val map: Map, x: Float, y: Float) {


    var pos = Vector2(x, y)
    private var accel = Vector2()
    private var vel = Vector2()
    private var bounds = Rectangle(x, y, 0.6f, 0.8f)

    var state = PlayerState.SPAWN
    var stateTime = 0f
    var dir = LEFT
    private var grounded = false

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

        if (state == PlayerState.SPAWN) {
            if (stateTime > 0.4f) {
                state = PlayerState.IDLE
            }
        }

        if (state == PlayerState.DYING) {
            if (stateTime > 0.4f) {
                state = PlayerState.DEAD
            }
        }

        stateTime += deltaTime
//        println("Player is: ${pos.x}, ${pos.y}")
    }

    private fun processKeys() {
        if (state == PlayerState.SPAWN || state == PlayerState.DYING) return

        val x0 = Gdx.input.getX(0) / Gdx.graphics.width.toFloat() * 480
        val x1 = Gdx.input.getX(1) / Gdx.graphics.width.toFloat() * 480
        val y0 = 320 - Gdx.input.getY(0) / Gdx.graphics.height.toFloat() * 320

        val leftButton = Gdx.input.isTouched(0) && x0 < 70 || Gdx.input.isTouched(1) && x1 < 70
        val rightButton = Gdx.input.isTouched(0) && x0 > 70 && x0 < 134 || Gdx.input.isTouched(1) && x1 > 70 && x1 < 134
        val jumpButton = Gdx.input.isTouched(0) && x0 > 416 && x0 < 480 && y0 < 64 || Gdx.input.isTouched(1) && x1 > 416 && x1 < 480 && y0 < 64

        if ((Gdx.input.isKeyPressed(Input.Keys.W) || jumpButton) && state != PlayerState.JUMP) {
            state = PlayerState.JUMP
            vel.y = JUMP_VELOCITY
            grounded = false
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A) || leftButton) {
            if (state != PlayerState.JUMP) state = PlayerState.RUN
            dir = LEFT
            accel.x = ACCELERATION * dir
        } else if (Gdx.input.isKeyPressed(Input.Keys.D) || rightButton) {
            if (state != PlayerState.JUMP) state = PlayerState.RUN
            dir = RIGHT
            accel.x = ACCELERATION * dir
        } else {
            if (state != PlayerState.JUMP) state = PlayerState.IDLE
            accel.x = 0f
        }
    }

    private var r = arrayOf(Rectangle(), Rectangle(), Rectangle(), Rectangle(), Rectangle())

    private fun tryMove() {
        bounds.x += vel.x
        fetchCollidableRects()
        for (i in r.indices) {
            val rect = r[i]
            if (bounds.overlaps(rect)) {
                if (vel.x < 0)
                    bounds.x = rect.x + rect.width + 0.01f
                else
                    bounds.x = rect.x - bounds.width - 0.01f
                vel.x = 0f
            }
        }

        bounds.y += vel.y
        fetchCollidableRects()
        for (i in r.indices) {
            val rect = r[i]
            if (bounds.overlaps(rect)) {
                if (vel.y < 0) {
                    bounds.y = rect.y + rect.height + 0.01f
                    grounded = true
                    if (state != PlayerState.DYING && state != PlayerState.SPAWN) state = if (abs(accel.x) > 0.1f) PlayerState.RUN else PlayerState.IDLE
                } else
                    bounds.y = rect.y - bounds.height - 0.01f
                vel.y = 0f
            }
        }

        pos.x = bounds.x - 0.2f
        pos.y = bounds.y
    }

    private fun fetchCollidableRects() {
        val p1x = bounds.x.toInt()
        val p1y = floor(bounds.y.toDouble()).toInt()
        val p2x = (bounds.x + bounds.width).toInt()
        val p2y = floor(bounds.y.toDouble()).toInt()
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
            return
        }

        val tile1 = tiles[p1x][tiles[0].size - 1 - p1y]
        val tile2 = tiles[p2x][tiles[0].size - 1 - p2y]
        val tile3 = tiles[p3x][tiles[0].size - 1 - p3y]
        val tile4 = tiles[p4x][tiles[0].size - 1 - p4y]

        if (state != PlayerState.DYING && (map.isDeadly(tile1) || map.isDeadly(tile2) || map.isDeadly(tile3) || map.isDeadly(tile4))) {
            state = PlayerState.DYING
            stateTime = 0f
        }

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
    }

    private fun outOfBounds(check: Int, max: Int): Boolean {
        return check < 0 || check >= max
    }

}