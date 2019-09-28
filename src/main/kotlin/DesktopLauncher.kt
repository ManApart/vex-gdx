import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration

fun main(arg: Array<String>) {
    val config = LwjglApplicationConfiguration()
    LwjglApplication(Vex(), config)
}