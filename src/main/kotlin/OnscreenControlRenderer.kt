import com.badlogic.gdx.Application.ApplicationType
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Align.left
import com.badlogic.gdx.utils.Align.right

class OnscreenControlRenderer(internal var map: Map) {
//    internal var batch: SpriteBatch
//    internal var dpad: TextureRegion
//    internal var left: TextureRegion
//    internal var right: TextureRegion
//    internal var jump: TextureRegion
//    internal var cubeControl: TextureRegion
//    internal var cubeFollow: TextureRegion

    init {
        loadAssets()
    }

    private fun loadAssets() {
//        val texture = Texture(Gdx.files.internal("data/controls.png"))
//        val buttons = TextureRegion.split(texture, 64, 64)[0]
//        left = buttons[0]
//        right = buttons[1]
//        jump = buttons[2]
//        cubeControl = buttons[3]
//        cubeFollow = TextureRegion.split(texture, 64, 64)[1][2]
//        dpad = TextureRegion(texture, 0, 64, 128, 128)
//        batch = SpriteBatch()
//        batch.projectionMatrix.setToOrtho2D(0f, 0f, 480f, 320f)
    }

    fun render() {
//        if (Gdx.app.type != ApplicationType.Android && Gdx.app.type != ApplicationType.iOS) return
//        if (map.cube.state !== Cube.CONTROLLED) {
//            batch.begin()
//            batch.draw(left, 0f, 0f)
//            batch.draw(right, 70f, 0f)
//            batch.draw(cubeControl, (480 - 64).toFloat(), (320 - 64).toFloat())
//            batch.draw(cubeFollow, (480 - 64).toFloat(), (320 - 138).toFloat())
//            batch.draw(jump, (480 - 64).toFloat(), 0f)
//            batch.end()
//        } else {
//            batch.begin()
//            batch.draw(dpad, 0f, 0f)
//            batch.draw(cubeFollow, (480 - 64).toFloat(), (320 - 138).toFloat())
//            batch.draw(cubeControl, (480 - 64).toFloat(), (320 - 64).toFloat())
//            batch.end()
//        }
    }

    fun dispose() {
//        dpad.texture.dispose()
//        batch.dispose()
    }
}
