package wkolendo.dowodyrejestracyjne.ui.start

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import wkolendo.dowodyrejestracyjne.utils.ui.BindingViewModel

class StartViewModel(app: Application, state: SavedStateHandle) : BindingViewModel(app, state), StartView {

    override fun onSaveState() = Unit
}