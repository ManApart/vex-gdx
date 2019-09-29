import Utilities.loadFile
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.FPSLogger
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.SpriteCache
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector3
import kotlin.math.ceil

class MapRenderer(private val map: Map) {
    private val viewportWidth = 24
    private val viewportHeight = 16
    private val cam: OrthographicCamera = OrthographicCamera(viewportWidth.toFloat(), viewportHeight.toFloat())
    private val cache = SpriteCache(this.map.tiles.size * this.map.tiles[0].size, false)
    private val batch = SpriteBatch(5460)
    private val tileTextureRegion = TextureRegion(Texture(loadFile("/data/tile.png")), 0, 0, 20, 20)
    private val blocks = createBlocks()
    private val fps = FPSLogger()

    private var stateTime = 0f
    private var lerpTarget = Vector3()

    init {
        this.cam.position.set(map.player.bounds.x, map.player.bounds.y, 0f)
    }

    private fun createBlocks(): Array<IntArray> {
        val numberOfChunksAlongX = ceil(this.map.tiles.size / viewportWidth.toFloat()).toInt()
        val numberOfChunksAlongY = ceil(this.map.tiles[0].size / viewportHeight.toFloat()).toInt()
        val chunks = Array(numberOfChunksAlongX) { IntArray(numberOfChunksAlongY) }

        for (chunkX in 0 until numberOfChunksAlongX) {
            for (chunkY in 0 until numberOfChunksAlongY) {
                cache.beginCache()
                buildChunk(chunkX, chunkY, cache)
                chunks[chunkX][chunkY] = cache.endCache()
            }
        }
        Gdx.app.debug("Cubocy", "blocks created")
        return chunks
    }

    private fun buildChunk(chunkX: Int, chunkY: Int, cache: SpriteCache) {
        for (x in chunkX * viewportWidth until chunkX * viewportWidth + viewportWidth) {
            for (y in chunkY * viewportHeight until chunkY * viewportHeight + viewportHeight) {
                if (map.getTileInverted(x, y) == TILE) {
                    val posY = map.tiles[0].size - y - 1
                    cache.add(tileTextureRegion, x.toFloat(), posY.toFloat(), 1f, 1f)
                    //                        if (map.tiles[x][posY] == TILE) cache.add(tile, x.toFloat(), y.toFloat(), 1f, 1f)
                }
            }
        }
    }

    fun render(deltaTime: Float) {
        cam.position.lerp(lerpTarget.set(map.player.bounds.x, map.player.bounds.y, 0f), 2f * deltaTime)
        cam.update()

        cache.projectionMatrix = cam.combined
        Gdx.gl.glDisable(GL20.GL_BLEND)
        cache.begin()
        for (x in 0 until blocks.size) {
            for (y in 0 until blocks[x].size) {
                cache.draw(blocks[x][y])
            }
        }
        cache.end()
        stateTime += deltaTime
        batch.projectionMatrix = cam.combined
        batch.begin()
        map.player.draw(batch)
        batch.end()
        fps.log()
    }

    fun dispose() {
        cache.dispose()
        batch.dispose()
        tileTextureRegion.texture.dispose()
    }
}
