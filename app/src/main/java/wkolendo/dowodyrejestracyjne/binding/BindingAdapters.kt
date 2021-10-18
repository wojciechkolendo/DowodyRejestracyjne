package wkolendo.dowodyrejestracyjne.binding

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import wkolendo.dowodyrejestracyjne.models.Certificate
import wkolendo.dowodyrejestracyjne.ui.start.StartCertificatesAdapter
import wkolendo.dowodyrejestracyjne.utils.getOrInstantiateAdapter
import wkolendo.dowodyrejestracyjne.utils.ui.OnItemClickListener

@BindingAdapter("certificates", "onItemClickListener", requireAll = false)
fun RecyclerView.certificates(
    items: List<Certificate>? = null,
    itemClickListener: OnItemClickListener<Certificate>? = null
) = getOrInstantiateAdapter<StartCertificatesAdapter>().apply {
    onItemClickListener = itemClickListener
    submitList(items)
}

@BindingAdapter("gone", "invisible", requireAll = false)
fun View.visibility(gone: Boolean? = null, invisible: Boolean? = null) {
    when {
        gone == true -> View.GONE
        invisible == true -> View.INVISIBLE
        else -> View.VISIBLE
    }.takeIf { it != visibility }?.also { visibility = it }
}

@BindingAdapter("goneIfEmpty")
fun View.goneIfEmpty(collection: Iterable<Any?>? = null) = visibility(gone = collection?.count() ?: 0 <= 0)

@BindingAdapter("goneIfNotEmpty")
fun View.goneIfNotEmpty(collection: Iterable<Any?>? = null) = visibility(gone = collection?.count() ?: 0 > 0)
