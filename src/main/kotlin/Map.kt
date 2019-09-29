import Utilities.loadFile
import com.badlogic.gdx.graphics.Pixmap

const val TILE = 0xffffff
const val SPAWN = 6524333

class Map {
    val player = Player(this, 3f, 2f)
    val tiles: Array<Array<Int>> = loadBinary()

    private fun loadBinary(): Array<Array<Int>> {
        val pixelMap = Pixmap(loadFile("/data/small-level.png"))
//        val pixelMap = Pixmap(loadFile("/data/levels.png"))
        val tiles = Array(pixelMap.width) { Array(pixelMap.height) { 0 } }
        for (x in 0 until tiles.size) {
            val maxY = tiles[x].size
            for (y in 0 until maxY) {
                val pixel = pixelMap.getPixel(x, y).ushr(8) and 0xffffff
                if (pixel == SPAWN){
                    player.bounds.x = x.toFloat()
                    player.bounds.y = (maxY -1 - y).toFloat()
                }

                //Invert the y here so it's logical everywhere else
                tiles[x][maxY -1 - y] = pixel
            }
        }
        return tiles
    }

    fun update(deltaTime: Float) {
        player.update(deltaTime)
    }

    fun getTile(x: Int, y: Int): Int {
        if (x < 0 || x >= tiles.size || y < 0 || y >= tiles[x].size) {
            return 0
        }
        return tiles[x][y]
    }

}
