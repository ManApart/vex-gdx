import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Pixmap

class Map {
//    internal var tiles: com.badlogic.gdx.utils.Array<IntArray>
//    var bob: Player
//
//    init {
//        loadBinary()
//    }
//
//    private fun loadBinary() {
//        val pixmap = Pixmap(Gdx.files.internal("data/levels.png"))
//        tiles = Array(pixmap.width) { IntArray(pixmap.height) }
//        for (y in 0..34) {
//            for (x in 0..149) {
//                val pix = pixmap.getPixel(x, y).ushr(8) and 0xffffff
//                if (match(pix, START)) {
//                    val dispenser = Dispenser(x, pixmap.height - 1 - y)
//                    dispensers.add(dispenser)
//                    activeDispenser = dispenser
//                    bob = Bob(this, activeDispenser!!.bounds.x, activeDispenser!!.bounds.y)
//                    bob.state = Bob.SPAWN
//                    cube = Cube(this, activeDispenser!!.bounds.x, activeDispenser!!.bounds.y)
//                    cube.state = Cube.DEAD
//                } else if (match(pix, DISPENSER)) {
//                    val dispenser = Dispenser(x, pixmap.height - 1 - y)
//                    dispensers.add(dispenser)
//                } else if (match(pix, ROCKET)) {
//                    val rocket = Rocket(this, x, pixmap.height - 1 - y)
//                    rockets.add(rocket)
//                } else if (match(pix, MOVING_SPIKES)) {
//                    movingSpikes.add(MovingSpikes(this, x, pixmap.height - 1 - y))
//                } else if (match(pix, LASER)) {
//                    lasers.add(Laser(this, x, pixmap.height - 1 - y))
//                } else if (match(pix, END)) {
//                    endDoor = EndDoor(x, pixmap.height - 1 - y)
//                } else {
//                    tiles[x][y] = pix
//                }
//            }
//        }
//
//        for (i in 0 until movingSpikes.size) {
//            movingSpikes.get(i).init()
//        }
//        for (i in 0 until lasers.size) {
//            lasers.get(i).init()
//        }
//    }
//
//    internal fun match(src: Int, dst: Int): Boolean {
//        return src == dst
//    }
//
//    fun update(deltaTime: Float) {
//        bob.update(deltaTime)
//        if (bob.state === Bob.DEAD) bob = Bob(this, activeDispenser!!.bounds.x, activeDispenser!!.bounds.y)
//        cube.update(deltaTime)
//        if (cube.state === Cube.DEAD) cube = Cube(this, bob.bounds.x, bob.bounds.y)
//        for (i in 0 until dispensers.size) {
//            if (bob.bounds.overlaps(dispensers.get(i).bounds)) {
//                activeDispenser = dispensers.get(i)
//            }
//        }
//        for (i in 0 until rockets.size) {
//            val rocket = rockets.get(i)
//            rocket.update(deltaTime)
//        }
//        for (i in 0 until movingSpikes.size) {
//            val spikes = movingSpikes.get(i)
//            spikes.update(deltaTime)
//        }
//        for (i in 0 until lasers.size) {
//            lasers.get(i).update()
//        }
//    }
//
//    fun isDeadly(tileId: Int): Boolean {
//        return tileId == SPIKES
//    }

    companion object {
        internal var EMPTY = 0
        internal var TILE = 0xffffff
        internal var START = 0xff0000
        internal var END = 0xff00ff
        internal var DISPENSER = 0xff0100
        internal var SPIKES = 0x00ff00
        internal var ROCKET = 0x0000ff
        internal var MOVING_SPIKES = 0xffff00
        internal var LASER = 0x00ffff
    }
}
