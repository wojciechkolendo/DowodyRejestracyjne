package wkolendo.dowodyrejestracyjne.utils.ui

import androidx.recyclerview.widget.DiffUtil
import kotlin.reflect.KClass

class TypeDiffCallback<T : Any> : DiffUtil.ItemCallback<T>() {
    val callbacks: List<Pair<KClass<Any>, DiffUtil.ItemCallback<Any>>> = mutableListOf()

    override fun areItemsTheSame(oldItem: T, newItem: T) =
        (oldItem::class == newItem::class) && (callbacks.find { it.first == oldItem::class }?.second?.areItemsTheSame(oldItem, newItem) ?: false)

    override fun areContentsTheSame(oldItem: T, newItem: T) =
        (oldItem::class == newItem::class) && (callbacks.find { it.first == oldItem::class }?.second?.areContentsTheSame(oldItem, newItem) ?: false)

    @Suppress("UNCHECKED_CAST")
    inline fun <reified S : Any> add(callback: DiffUtil.ItemCallback<S>): TypeDiffCallback<T> = this.also {
        (callbacks as MutableList).add((S::class to callback) as Pair<KClass<Any>, DiffUtil.ItemCallback<Any>>)
    }

    inline fun <reified S : Any> add(noinline item: Pair<S, S>.() -> Boolean, noinline content: Pair<S, S>.() -> Boolean) = add(
        diffCallback(item, content)
    )
}

fun <T : Any> diffCallback(item: Pair<T, T>.() -> Boolean, content: Pair<T, T>.() -> Boolean) = object : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T) = item(oldItem to newItem)

    override fun areContentsTheSame(oldItem: T, newItem: T) = content(oldItem to newItem)
}

fun <T : Any> typeDiffCallback() = TypeDiffCallback<T>()

fun <T, U> Pair<T?, T?>.property(keyExtractor: T.() -> U?): Pair<U?, U?> = first?.let(keyExtractor) to second?.let(keyExtractor)

fun <T, U> Pair<T?, T?>.hasSame(keyExtractor: T.() -> U?): Boolean = hasNulls() || (hasNotNulls() && keyExtractor(first!!) == keyExtractor(second!!))

fun <T> Pair<T?, T?>.equal(compare: (o1: T, o2: T) -> Boolean): Boolean = hasNulls() || (hasNotNulls() && compare(first!!, second!!))

fun <T, U> Pair<T?, T?>.hasSame(vararg keyExtractors: T.() -> U?): Boolean = hasNulls() || (hasNotNulls() && keyExtractors.all { it(first!!) == it(second!!) })

private fun <T> Pair<T?, T?>.hasNulls(): Boolean = first == null && second == null

private fun <T> Pair<T?, T?>.hasNotNulls(): Boolean = first != null && second != null
