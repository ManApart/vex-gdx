import Utilities.loadFile
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.math.Vector2

const val TILE = 0xffffff

class Map {
    var start = Vector2(11.toFloat(), 151.toFloat())
    val tiles: Array<Array<Int>> = loadBinary()
    val player = Player(this, start.x, start.y)


    private fun loadBinary(): Array<Array<Int>> {
        val pixelMap = Pixmap(loadFile("/data/levels.png"))
        val tiles = Array(pixelMap.width) { Array(pixelMap.height) { 0 } }
        for (y in 0..34) {
            for (x in 0..149) {
                val pixel = pixelMap.getPixel(x, y).ushr(8) and 0xffffff
                tiles[x][y] = pixel
            }
        }
        return tiles
    }

    fun update(deltaTime: Float) {
        player.update(deltaTime)
    }
}
