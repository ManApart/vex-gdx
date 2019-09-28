import Utilities.loadFile
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.FPSLogger
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.SpriteCache
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20
import com.badlogic.gdx.math.Vector3
import kotlin.math.ceil

class MapRenderer(private val map: Map) {
    private val cam: OrthographicCamera = OrthographicCamera(24f, 16f)
    private val cache: SpriteCache
    private val batch = SpriteBatch(5460)
    private val blocks: Array<IntArray>
    private val tile = TextureRegion(Texture(loadFile("/data/tile.png")), 0, 0, 20, 20)
    private val playerTexture = Texture(loadFile("/data/bob.png"))
    private val split = TextureRegion(playerTexture).split(20, 20)[0]
    private val mirror = TextureRegion(playerTexture).split(20, 20)[0]

    init {
        for (region in mirror)
            region.flip(true, false)
    }

    private val playerLeft: Animation<TextureRegion> = Animation(0.1f, mirror[0], mirror[1])
    private val playerRight: Animation<TextureRegion> = Animation(0.1f, split[0], split[1])
    private val playerJumpLeft: Animation<TextureRegion> = Animation(0.1f, mirror[2], mirror[3])
    private val playerJumpRight: Animation<TextureRegion> = Animation(0.1f, split[2], split[3])
    private val playerIdleLeft: Animation<TextureRegion> = Animation(0.5f, mirror[0], mirror[4])
    private val playerIdleRight: Animation<TextureRegion> = Animation(0.5f, split[0], split[4])
    private val fps = FPSLogger()

    private var stateTime = 0f
    private var lerpTarget = Vector3()

    init {
        this.cam.position.set(map.player.pos.x, map.player.pos.y, 0f)
        this.cache = SpriteCache(this.map.tiles.size * this.map.tiles[0].size, false)
        this.blocks = Array(ceil(this.map.tiles.size / 24.0f).toInt()) { IntArray(ceil(this.map.tiles[0].size / 16.0f).toInt()) }

        createBlocks()
    }

    private fun createBlocks() {
        val width = map.tiles.size
        val height = map.tiles[0].size
        for (blockY in 0 until blocks[0].size) {
            for (blockX in blocks.indices) {
                cache.beginCache()
                for (y in blockY * 16 until blockY * 16 + 16) {
                    for (x in blockX * 24 until blockX * 24 + 24) {
                        if (x > width) continue
                        if (y > height) continue
                        val posY = height - y - 1
                        if (map.tiles[x][y] == TILE) cache.add(tile, x.toFloat(), posY.toFloat(), 1f, 1f)
                    }
                }
                blocks[blockX][blockY] = cache.endCache()
            }
        }
        Gdx.app.debug("Cubocy", "blocks created")
    }

    fun render(deltaTime: Float) {
        cam.position.lerp(lerpTarget.set(map.player.pos.x, map.player.pos.y, 0f), 2f * deltaTime)
        cam.update()

        cache.projectionMatrix = cam.combined
        Gdx.gl.glDisable(GL20.GL_BLEND)
        cache.begin()
        var b = 0
        for (blockY in 0..3) {
            for (blockX in 0..5) {
                cache.draw(blocks[blockX][blockY])
                b++
            }
        }
        cache.end()
        stateTime += deltaTime
        batch.projectionMatrix = cam.combined
        batch.begin()
        renderPlayer()
        batch.end()
        fps.log()
    }

    private fun renderPlayer() {
        var anim: Animation<TextureRegion> = playerLeft
        var loop = true
        if (map.player.state === PlayerState.RUN) {
            anim = if (map.player.dir == LEFT)
                playerLeft
            else
                playerRight
        }
        if (map.player.state === PlayerState.IDLE) {
            anim = if (map.player.dir == LEFT)
                playerIdleLeft
            else
                playerIdleRight
        }
        if (map.player.state === PlayerState.JUMP) {
            anim = if (map.player.dir == LEFT)
                playerJumpLeft
            else
                playerJumpRight
        }

        batch.draw(anim.getKeyFrame(map.player.stateTime, loop), map.player.pos.x, map.player.pos.y, 1.toFloat(), 1.toFloat())
    }


    fun dispose() {
        cache.dispose()
        batch.dispose()
        tile.texture.dispose()
    }
}
