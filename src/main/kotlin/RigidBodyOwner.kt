interface RigidBodyOwner {
    fun onCollided(direction: Direction)
    fun onNoLongerCollided(direction: Direction)
}