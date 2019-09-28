import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import kotlin.collections.Map

class Player {
//    internal val IDLE = 0
//    internal val RUN = 1
//    internal val JUMP = 2
//    internal val SPAWN = 3
//    internal val DYING = 4
//    internal val DEAD = 5
//    internal val LEFT = -1
//    internal val RIGHT = 1
//    internal val ACCELERATION = 20f
//    internal val JUMP_VELOCITY = 10f
//    internal val GRAVITY = 20.0f
//    internal val MAX_VEL = 6f
//    internal val DAMP = 0.90f
//
//    internal var pos = Vector2()
//    internal var accel = Vector2()
//    internal var vel = Vector2()
//    var bounds = Rectangle()
//
//    internal var state = SPAWN
//    internal var stateTime = 0f
//    internal var dir = LEFT
//    internal var map: Map
//    internal var grounded = false
//
//    fun Bob(map: Map, x: Float, y: Float): ??? {
//        this.map = map
//        pos.x = x
//        pos.y = y
//        bounds.width = 0.6f
//        bounds.height = 0.8f
//        bounds.x = pos.x + 0.2f
//        bounds.y = pos.y
//        state = SPAWN
//        stateTime = 0f
//    }
//
//    fun update(deltaTime: Float) {
//        processKeys()
//
//        accel.y = -GRAVITY
//        accel.scl(deltaTime)
//        vel.add(accel.x, accel.y)
//        if (accel.x == 0f) vel.x *= DAMP
//        if (vel.x > MAX_VEL) vel.x = MAX_VEL
//        if (vel.x < -MAX_VEL) vel.x = -MAX_VEL
//        vel.scl(deltaTime)
//        tryMove()
//        vel.scl(1.0f / deltaTime)
//
//        if (state == SPAWN) {
//            if (stateTime > 0.4f) {
//                state = IDLE
//            }
//        }
//
//        if (state == DYING) {
//            if (stateTime > 0.4f) {
//                state = DEAD
//            }
//        }
//
//        stateTime += deltaTime
//    }
//
//    private fun processKeys() {
//        if (map.cube.state === Cube.CONTROLLED || state == SPAWN || state == DYING) return
//
//        val x0 = Gdx.input.getX(0) / Gdx.graphics.width.toFloat() * 480
//        val x1 = Gdx.input.getX(1) / Gdx.graphics.width.toFloat() * 480
//        val y0 = 320 - Gdx.input.getY(0) / Gdx.graphics.height.toFloat() * 320
//
//        val leftButton = Gdx.input.isTouched(0) && x0 < 70 || Gdx.input.isTouched(1) && x1 < 70
//        val rightButton = Gdx.input.isTouched(0) && x0 > 70 && x0 < 134 || Gdx.input.isTouched(1) && x1 > 70 && x1 < 134
//        val jumpButton = Gdx.input.isTouched(0) && x0 > 416 && x0 < 480 && y0 < 64 || Gdx.input.isTouched(1) && x1 > 416 && x1 < 480 && y0 < 64
//
//        if ((Gdx.input.isKeyPressed(Input.Keys.W) || jumpButton) && state != JUMP) {
//            state = JUMP
//            vel.y = JUMP_VELOCITY
//            grounded = false
//        }
//
//        if (Gdx.input.isKeyPressed(Input.Keys.A) || leftButton) {
//            if (state != JUMP) state = RUN
//            dir = LEFT
//            accel.x = ACCELERATION * dir
//        } else if (Gdx.input.isKeyPressed(Input.Keys.D) || rightButton) {
//            if (state != JUMP) state = RUN
//            dir = RIGHT
//            accel.x = ACCELERATION * dir
//        } else {
//            if (state != JUMP) state = IDLE
//            accel.x = 0f
//        }
//    }
//
//    internal var r = arrayOf(Rectangle(), Rectangle(), Rectangle(), Rectangle(), Rectangle())
//
//    private fun tryMove() {
//        bounds.x += vel.x
//        fetchCollidableRects()
//        for (i in r.indices) {
//            val rect = r[i]
//            if (bounds.overlaps(rect)) {
//                if (vel.x < 0)
//                    bounds.x = rect.x + rect.width + 0.01f
//                else
//                    bounds.x = rect.x - bounds.width - 0.01f
//                vel.x = 0f
//            }
//        }
//
//        bounds.y += vel.y
//        fetchCollidableRects()
//        for (i in r.indices) {
//            val rect = r[i]
//            if (bounds.overlaps(rect)) {
//                if (vel.y < 0) {
//                    bounds.y = rect.y + rect.height + 0.01f
//                    grounded = true
//                    if (state != DYING && state != SPAWN) state = if (Math.abs(accel.x) > 0.1f) RUN else IDLE
//                } else
//                    bounds.y = rect.y - bounds.height - 0.01f
//                vel.y = 0f
//            }
//        }
//
//        pos.x = bounds.x - 0.2f
//        pos.y = bounds.y
//    }
//
//    private fun fetchCollidableRects() {
//        val p1x = bounds.x.toInt()
//        val p1y = Math.floor(bounds.y.toDouble()).toInt()
//        val p2x = (bounds.x + bounds.width).toInt()
//        val p2y = Math.floor(bounds.y.toDouble()).toInt()
//        val p3x = (bounds.x + bounds.width).toInt()
//        val p3y = (bounds.y + bounds.height).toInt()
//        val p4x = bounds.x.toInt()
//        val p4y = (bounds.y + bounds.height).toInt()
//
//        val tiles = map.tiles
//        val tile1 = tiles[p1x][map.tiles[0].length - 1 - p1y]
//        val tile2 = tiles[p2x][map.tiles[0].length - 1 - p2y]
//        val tile3 = tiles[p3x][map.tiles[0].length - 1 - p3y]
//        val tile4 = tiles[p4x][map.tiles[0].length - 1 - p4y]
//
//        if (state != DYING && (map.isDeadly(tile1) || map.isDeadly(tile2) || map.isDeadly(tile3) || map.isDeadly(tile4))) {
//            state = DYING
//            stateTime = 0f
//        }
//
//        if (tile1 == Map.TILE)
//            r[0].set(p1x.toFloat(), p1y.toFloat(), 1f, 1f)
//        else
//            r[0].set(-1f, -1f, 0f, 0f)
//        if (tile2 == Map.TILE)
//            r[1].set(p2x.toFloat(), p2y.toFloat(), 1f, 1f)
//        else
//            r[1].set(-1f, -1f, 0f, 0f)
//        if (tile3 == Map.TILE)
//            r[2].set(p3x.toFloat(), p3y.toFloat(), 1f, 1f)
//        else
//            r[2].set(-1f, -1f, 0f, 0f)
//        if (tile4 == Map.TILE)
//            r[3].set(p4x.toFloat(), p4y.toFloat(), 1f, 1f)
//        else
//            r[3].set(-1f, -1f, 0f, 0f)
//
//        if (map.cube.state === Cube.FIXED) {
//            r[4].x = map.cube.bounds.x
//            r[4].y = map.cube.bounds.y
//            r[4].width = map.cube.bounds.width
//            r[4].height = map.cube.bounds.height
//        } else
//            r[4].set(-1f, -1f, 0f, 0f)
//    }
}