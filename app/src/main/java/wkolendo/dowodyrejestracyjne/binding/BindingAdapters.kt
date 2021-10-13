package wkolendo.dowodyrejestracyjne.binding

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import org.w3c.dom.Text
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

@BindingAdapter("fuelType")
fun TextView.fuelType(fuelType: String? = null) {
    text = when (fuelType) {
        "P" -> "Benzyna"
        "D" -> "Olej napędowy"
        "M" -> "Mieszanka (paliwo-olej)"
        "LPG" -> "Gaz płynny (propan-butan)"
        "CNG" -> "Gaz ziemny sprężony (metan)"
        "H" -> "Wodór"
        "LNG" -> "Gaz ziemny skroplony (metan)"
        "BD" -> "Biodiesel"
        "E85" -> "Etanol"
        "EE" -> "Energia elektryczna"
        "999" -> "Inne"
        else -> fuelType
    }
}