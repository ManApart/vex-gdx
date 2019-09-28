import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.files.FileHandle

fun main() {
    val config = LwjglApplicationConfiguration()
    LwjglApplication(Vex(), config)
}

object Utilities {
    fun loadFile(path: String): FileHandle {
        return Gdx.files.internal(this::class.java.getResource(path).path)
    }
}