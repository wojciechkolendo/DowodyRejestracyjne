package wkolendo.dowodyrejestracyjne.utils.ui

interface OnItemClickListener<T> {
    fun onItemClick(item: T)
}

fun <T> onItemClickListener(listener: (T) -> Unit): OnItemClickListener<T> = object :
    OnItemClickListener<T> {
    override fun onItemClick(item: T) = listener(item)
}