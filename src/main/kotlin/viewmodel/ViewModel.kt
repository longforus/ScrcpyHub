package viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import org.koin.core.component.KoinComponent

abstract class ViewModel : KoinComponent {
    val coroutineScope: CoroutineScope = MainScope()

    open fun onStarted() {}

    open fun onCleared() {
        coroutineScope.cancel("coroutine Canceled")
    }
}