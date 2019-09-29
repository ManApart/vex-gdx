import Utilities.loadFile
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle

class PlayerDrawable {
    private val playerTexture = Texture(loadFile("/data/bob.png"))
    private val split = TextureRegion(playerTexture).split(20, 20)[0]
    private val mirror = TextureRegion(playerTexture).split(20, 20)[0]

    init {
        for (region in mirror)
            region.flip(true, false)
    }

    private val leftAnim: Animation<TextureRegion> = Animation(0.1f, mirror[0], mirror[1])
    private val rightAnim: Animation<TextureRegion> = Animation(0.1f, split[0], split[1])
    private val jumpLeftAnim: Animation<TextureRegion> = Animation(0.1f, mirror[2], mirror[3])
    private val jumpRightAnim: Animation<TextureRegion> = Animation(0.1f, split[2], split[3])
    private val idleLeftAnim: Animation<TextureRegion> = Animation(0.5f, mirror[0], mirror[4])
    private val idleRightAnim: Animation<TextureRegion> = Animation(0.5f, split[0], split[4])

    fun draw(batch: SpriteBatch, bounds: Rectangle, state: PlayerState, facing: PlayerDirection, stateTime: Float) {
        val anim = determineAnimation(state, facing)
        batch.draw(anim.getKeyFrame(stateTime, true), bounds.x - 0.2f, bounds.y, 1.toFloat(), 1.toFloat())
    }

    private fun determineAnimation(state: PlayerState, facing: PlayerDirection): Animation<TextureRegion> {
        return when {
            state == PlayerState.RUN && facing == PlayerDirection.LEFT -> leftAnim
            state == PlayerState.RUN && facing == PlayerDirection.RIGHT -> rightAnim
            state == PlayerState.IDLE && facing == PlayerDirection.LEFT -> idleLeftAnim
            state == PlayerState.IDLE && facing == PlayerDirection.RIGHT -> idleRightAnim
            state == PlayerState.JUMP && facing == PlayerDirection.LEFT -> jumpLeftAnim
            state == PlayerState.JUMP && facing == PlayerDirection.RIGHT -> jumpRightAnim
            else -> idleLeftAnim
        }
    }

}