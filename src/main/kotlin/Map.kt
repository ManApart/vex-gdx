import Utilities.loadFile
import com.badlogic.gdx.graphics.Pixmap

const val TILE = 0xffffff

class Map {
    val tiles: Array<Array<Int>> = loadBinary()
    val player = Player(this, 11f, 155f)
//    val player = Player(this, 11f, 5f)

    private fun loadBinary(): Array<Array<Int>> {
        val pixelMap = Pixmap(loadFile("/data/levels.png"))
        val tiles = Array(pixelMap.width) { Array(pixelMap.height) { 0 } }
        for (x in 0 until tiles.size) {
            val maxY = tiles[x].size
            for (y in 0 until maxY) {
                val pixel = pixelMap.getPixel(x, y).ushr(8) and 0xffffff
                tiles[x][y] = pixel
//                tiles[x][maxY -1 - y] = pixel
            }
        }
        return tiles
    }

    fun update(deltaTime: Float) {
        player.update(deltaTime)
    }
}
