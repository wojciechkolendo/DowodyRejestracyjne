package wkolendo.dowodyrejestracyjne.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import wkolendo.dowodyrejestracyjne.R
import wkolendo.dowodyrejestracyjne.utils.getAttrColor
import wkolendo.dowodyrejestracyjne.utils.stateList

@BindingAdapter("dayModeSelected")
fun ImageView.dayModeSelected(selected: Boolean? = null) {
    if (selected == true) {
        backgroundTintList = R.attr.colorSecondary.getAttrColor(context).stateList
        imageTintList = R.attr.colorOnSecondary.getAttrColor(context).stateList
    } else {
        backgroundTintList = R.attr.colorSurface.getAttrColor(context).stateList
        imageTintList = R.attr.colorOnSurface.getAttrColor(context).stateList
    }
}