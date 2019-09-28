import Utilities.loadFile
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.math.Vector2

val EMPTY = 0
val TILE = 0xffffff
val START = 0xff0000
val END = 0xff00ff
val DISPENSER = 0xff0100
val SPIKES = 0x00ff00
val ROCKET = 0x0000ff
val MOVING_SPIKES = 0xffff00
val LASER = 0x00ffff

class Map {
    var start = Vector2(11.toFloat(), 8.toFloat())
    val tiles: Array<Array<Int>> = loadBinary()
    val player = Player(this, start.x, start.y)


    private fun loadBinary(): Array<Array<Int>> {
        val pixmap = Pixmap(loadFile("/data/levels.png"))
//        val pixmap = Pixmap(Gdx.files.internal("data/levels.png"))
        val tiles = Array(pixmap.width) { Array(pixmap.height) { 0 } }
        for (y in 0..34) {
            for (x in 0..149) {
                val pixel = pixmap.getPixel(x, y).ushr(8) and 0xffffff
                tiles[x][y] = pixel
                if (pixel == START) {
                    start = Vector2(x.toFloat(), y.toFloat())
                }
            }
        }
        return tiles
    }

    fun update(deltaTime: Float) {
        player.update(deltaTime)
    }

    fun isDeadly(tileId: Int): Boolean {
        return tileId == SPIKES
    }
}
