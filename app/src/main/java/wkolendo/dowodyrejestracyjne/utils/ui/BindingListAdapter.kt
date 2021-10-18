package wkolendo.dowodyrejestracyjne.utils.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import wkolendo.dowodyrejestracyjne.BR

abstract class BindingListAdapter<T, VDB : ViewDataBinding>(diffCallback: DiffUtil.ItemCallback<T>, @LayoutRes private val layout: Int? = null) :
    ListAdapter<T, BindingListAdapter<T, VDB>.ViewHolder>(diffCallback) {

    protected open val executeBindingsOnBind: Boolean = true

    var onItemClickListener: OnItemClickListener<T>? = null

    protected open val internalOnItemClickListener = onItemClickListener<T> { onItemClickListener?.onItemClick(it) }

    protected open fun getItemType(position: Int): Int = layout!!

    final override fun getItemViewType(position: Int): Int = getItemType(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        DataBindingUtil.inflate(LayoutInflater.from(parent.context), viewType, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = with(holder.binding) {
        val item = getItem(position)
        setVariable(BR.clickListener, internalOnItemClickListener)
        setVariable(BR.item, item)
        onBindView(this, item, position)
        if (executeBindingsOnBind) executePendingBindings()
    }

    protected open fun onBindView(binding: VDB, item: T?, position: Int) {}

    inner class ViewHolder(val binding: VDB) : RecyclerView.ViewHolder(binding.root)
}
