enum class PlayerState {
    IDLE,
    RUNING,
    JUMPING,
    FALLING,
    DASHING,
    GRAPPLING,
    WALL_RUNNING;

    fun isInState(vararg states: PlayerState): Boolean {
        return states.contains(this)
    }
}