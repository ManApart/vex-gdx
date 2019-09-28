import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import kotlin.math.min

class GameScreen(private val game: Game) : Screen {
    override fun pause() {}
    override fun resume() {}
    override fun resize(width: Int, height: Int) {}
    override fun dispose() {}

    private var map = Map()
    private var renderer = MapRenderer(map)
    private var controlRenderer = OnscreenControlRenderer(map)


    override fun show() {
        map = Map()
        renderer = MapRenderer(map)
        controlRenderer = OnscreenControlRenderer(map)
    }

    override fun render(delta: Float) {
        val minDelta = min(0.06f, Gdx.graphics.deltaTime)
        map.update(minDelta)
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        renderer.render(minDelta)
        controlRenderer.render()

//        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {}
    }

    override fun hide() {
        Gdx.app.debug("Vex", "dispose game screen")
        renderer.dispose()
        controlRenderer.dispose()
    }
}