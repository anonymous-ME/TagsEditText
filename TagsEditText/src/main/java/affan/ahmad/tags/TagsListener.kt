package affan.ahmad.tags


interface TagsListener {
    fun onTagCreated(tag: String)
    fun onTagRemoved(index: Int)
}