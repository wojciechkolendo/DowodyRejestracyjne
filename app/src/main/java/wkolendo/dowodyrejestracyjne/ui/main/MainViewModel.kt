package wkolendo.dowodyrejestracyjne.ui.main

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import wkolendo.dowodyrejestracyjne.utils.ui.BindingViewModel

class MainViewModel(app: Application, state: SavedStateHandle) : BindingViewModel(app, state), MainView {

    override fun onSaveState() = Unit

}