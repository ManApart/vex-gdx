import com.badlogic.gdx.Game

class Vex : Game() {
    override fun create() {
        setScreen(GameScreen(this))
    }
}