package wkolendo.dowodyrejestracyjne.utils.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers

abstract class BindingViewModel(val app: Application, val state: SavedStateHandle) : AndroidViewModel(app) {

    @Suppress("PropertyName")
    val Dispatchers.ViewModelIO
        get() = viewModelScope.coroutineContext + IO

    abstract fun onSaveState()
}