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

class MapRenderer(internal var map: Map) {
//    internal var cam: OrthographicCamera
//    internal var cache: SpriteCache
//    internal var batch = SpriteBatch(5460)
//    internal var renderer = ImmediateModeRenderer20(false, true, 0)
//    internal var blocks: Array<IntArray>
//    internal var tile: TextureRegion
//    internal var bobLeft: Animation<TextureRegion>
//    internal var bobRight: Animation<TextureRegion>
//    internal var bobJumpLeft: Animation<TextureRegion>
//    internal var bobJumpRight: Animation<TextureRegion>
//    internal var bobIdleLeft: Animation<TextureRegion>
//    internal var bobIdleRight: Animation<TextureRegion>
//    internal var bobDead: Animation<TextureRegion>
//    internal var zap: Animation<TextureRegion>? = null
//    internal var cube: TextureRegion
//    internal var cubeFixed: Animation<TextureRegion>
//    internal var cubeControlled: TextureRegion
//    internal var dispenser: TextureRegion
//    internal var spawn: Animation<TextureRegion>
//    internal var dying: Animation<TextureRegion>
//    internal var spikes: TextureRegion
//    internal var rocket: Animation<TextureRegion>
//    internal var rocketExplosion: Animation<TextureRegion>
//    internal var rocketPad: TextureRegion
//    internal var endDoor: TextureRegion
//    internal var movingSpikes: TextureRegion
//    internal var laser: TextureRegion
//    internal var fps = FPSLogger()
//
//    internal var stateTime = 0f
//    internal var lerpTarget = Vector3()
//
//    init {
//        this.cam = OrthographicCamera(24f, 16f)
//        this.cam.position.set(map.bob.pos.x, map.bob.pos.y, 0f)
//        this.cache = SpriteCache(this.map.tiles.length * this.map.tiles[0].length, false)
//        this.blocks = Array(Math.ceil(this.map.tiles.length / 24.0f).toInt()) { IntArray(Math.ceil(this.map.tiles[0].length / 16.0f).toInt()) }
//
//        createAnimations()
//        createBlocks()
//    }
//
//    private fun createBlocks() {
//        val width = map.tiles.length
//        val height = map.tiles[0].length
//        for (blockY in 0 until blocks[0].size) {
//            for (blockX in blocks.indices) {
//                cache.beginCache()
//                for (y in blockY * 16 until blockY * 16 + 16) {
//                    for (x in blockX * 24 until blockX * 24 + 24) {
//                        if (x > width) continue
//                        if (y > height) continue
//                        val posY = height - y - 1
//                        if (map.match(map.tiles[x][y], Map.TILE)) cache.add(tile, x.toFloat(), posY.toFloat(), 1f, 1f)
//                        if (map.match(map.tiles[x][y], Map.SPIKES)) cache.add(spikes, x.toFloat(), posY.toFloat(), 1f, 1f)
//                    }
//                }
//                blocks[blockX][blockY] = cache.endCache()
//            }
//        }
//        Gdx.app.debug("Cubocy", "blocks created")
//    }
//
//    private fun createAnimations() {
//        this.tile = TextureRegion(Texture(Gdx.files.internal("data/tile.png")), 0, 0, 20, 20)
//        val bobTexture = Texture(Gdx.files.internal("data/bob.png"))
//        var split = TextureRegion(bobTexture).split(20, 20)[0]
//        val mirror = TextureRegion(bobTexture).split(20, 20)[0]
//        for (region in mirror)
//            region.flip(true, false)
//        spikes = split[5]
//        bobRight = Animation(0.1f, split[0], split[1])
//        bobLeft = Animation(0.1f, mirror[0], mirror[1])
//        bobJumpRight = Animation(0.1f, split[2], split[3])
//        bobJumpLeft = Animation(0.1f, mirror[2], mirror[3])
//        bobIdleRight = Animation(0.5f, split[0], split[4])
//        bobIdleLeft = Animation(0.5f, mirror[0], mirror[4])
//        bobDead = Animation(0.2f, split[0])
//        split = TextureRegion(bobTexture).split(20, 20)[1]
//        cube = split[0]
//        cubeFixed = Animation(1, split[1], split[2], split[3], split[4], split[5])
//        split = TextureRegion(bobTexture).split(20, 20)[2]
//        cubeControlled = split[0]
//        spawn = Animation(0.1f, split[4], split[3], split[2], split[1])
//        dying = Animation(0.1f, split[1], split[2], split[3], split[4])
//        dispenser = split[5]
//        split = TextureRegion(bobTexture).split(20, 20)[3]
//        rocket = Animation(0.1f, split[0], split[1], split[2], split[3])
//        rocketPad = split[4]
//        split = TextureRegion(bobTexture).split(20, 20)[4]
//        rocketExplosion = Animation(0.1f, split[0], split[1], split[2], split[3], split[4], split[4])
//        split = TextureRegion(bobTexture).split(20, 20)[5]
//        endDoor = split[2]
//        movingSpikes = split[0]
//        laser = split[1]
//    }
//
//    fun render(deltaTime: Float) {
//        if (map.cube.state !== Cube.CONTROLLED)
//            cam.position.lerp(lerpTarget.set(map.bob.pos.x, map.bob.pos.y, 0f), 2f * deltaTime)
//        else
//            cam.position.lerp(lerpTarget.set(map.cube.pos.x, map.cube.pos.y, 0f), 2f * deltaTime)
//        cam.update()
//
//        renderLaserBeams()
//
//        cache.projectionMatrix = cam.combined
//        Gdx.gl.glDisable(GL20.GL_BLEND)
//        cache.begin()
//        var b = 0
//        for (blockY in 0..3) {
//            for (blockX in 0..5) {
//                cache.draw(blocks[blockX][blockY])
//                b++
//            }
//        }
//        cache.end()
//        stateTime += deltaTime
//        batch.projectionMatrix = cam.combined
//        batch.begin()
//        renderDispensers()
//        if (map.endDoor != null) batch.draw(endDoor, map.endDoor.bounds.x, map.endDoor.bounds.y, 1, 1)
//        renderLasers()
//        renderMovingSpikes()
//        renderBob()
//        renderCube()
//        renderRockets()
//        batch.end()
//        renderLaserBeams()
//
//        fps.log()
//    }
//
//    private fun renderBob() {
//        var anim: Animation<TextureRegion>? = null
//        var loop = true
//        if (map.bob.state === Bob.RUN) {
//            if (map.bob.dir === Bob.LEFT)
//                anim = bobLeft
//            else
//                anim = bobRight
//        }
//        if (map.bob.state === Bob.IDLE) {
//            if (map.bob.dir === Bob.LEFT)
//                anim = bobIdleLeft
//            else
//                anim = bobIdleRight
//        }
//        if (map.bob.state === Bob.JUMP) {
//            if (map.bob.dir === Bob.LEFT)
//                anim = bobJumpLeft
//            else
//                anim = bobJumpRight
//        }
//        if (map.bob.state === Bob.SPAWN) {
//            anim = spawn
//            loop = false
//        }
//        if (map.bob.state === Bob.DYING) {
//            anim = dying
//            loop = false
//        }
//        batch.draw(anim!!.getKeyFrame(map.bob.stateTime, loop), map.bob.pos.x, map.bob.pos.y, 1, 1)
//    }
//
//    private fun renderCube() {
//        if (map.cube.state === Cube.FOLLOW) batch.draw(cube, map.cube.pos.x, map.cube.pos.y, 1.5f, 1.5f)
//        if (map.cube.state === Cube.FIXED)
//            batch.draw(cubeFixed.getKeyFrame(map.cube.stateTime, false), map.cube.pos.x, map.cube.pos.y, 1.5f, 1.5f)
//        if (map.cube.state === Cube.CONTROLLED) batch.draw(cubeControlled, map.cube.pos.x, map.cube.pos.y, 1.5f, 1.5f)
//    }
//
//    private fun renderRockets() {
//        for (i in 0 until map.rockets.size) {
//            val rocket = map.rockets.get(i)
//            if (rocket.state === Rocket.FLYING) {
//                val frame = this.rocket.getKeyFrame(rocket.stateTime, true)
//                batch.draw(frame, rocket.pos.x, rocket.pos.y, 0.5f, 0.5f, 1f, 1f, 1f, 1f, rocket.vel.angle())
//            } else {
//                val frame = this.rocketExplosion.getKeyFrame(rocket.stateTime, false)
//                batch.draw(frame, rocket.pos.x, rocket.pos.y, 1, 1)
//            }
//            batch.draw(rocketPad, rocket.startPos.x, rocket.startPos.y, 1, 1)
//        }
//    }
//
//    private fun renderDispensers() {
//        for (i in 0 until map.dispensers.size) {
//            val dispenser = map.dispensers.get(i)
//            batch.draw(this.dispenser, dispenser.bounds.x, dispenser.bounds.y, 1, 1)
//        }
//    }
//
//    private fun renderMovingSpikes() {
//        for (i in 0 until map.movingSpikes.size) {
//            val spikes = map.movingSpikes.get(i)
//            batch.draw(movingSpikes, spikes.pos.x, spikes.pos.y, 0.5f, 0.5f, 1f, 1f, 1f, 1f, spikes.angle)
//        }
//    }
//
//    private fun renderLasers() {
//        for (i in 0 until map.lasers.size) {
//            val laser = map.lasers.get(i)
//            batch.draw(this.laser, laser.pos.x, laser.pos.y, 0.5f, 0.5f, 1f, 1f, 1f, 1f, laser.angle)
//        }
//    }
//
//    private fun renderLaserBeams() {
//        cam.update(false)
//        renderer.begin(cam.combined, GL20.GL_LINES)
//        for (i in 0 until map.lasers.size) {
//            val laser = map.lasers.get(i)
//            val sx = laser.startPoint.x
//            val sy = laser.startPoint.y
//            val ex = laser.cappedEndPoint.x
//            val ey = laser.cappedEndPoint.y
//            renderer.color(0f, 1f, 0f, 1f)
//            renderer.vertex(sx, sy, 0f)
//            renderer.color(0f, 1f, 0f, 1f)
//            renderer.vertex(ex, ey, 0f)
//        }
//        renderer.end()
//    }
//
//    fun dispose() {
//        cache.dispose()
//        batch.dispose()
//        tile.texture.dispose()
//        cube.texture.dispose()
//    }
}
