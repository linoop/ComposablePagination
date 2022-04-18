package com.example.pagination

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val repository = Repository()

    var state by mutableStateOf(ScreenState())

    private val paginate = DefaultPagination(
        initialKey = state.page,
        onLoadUpdated = {
            state = state.copy(isLoading = it)
        },
        onRequest = { nextKey -> repository.getItems(nextKey, 20) },
        getNextKey = { state.page + 1 },
        onError = { state = state.copy(error = it?.localizedMessage) },
        onSuccess = { items, newKey ->
            state =
                state.copy(items = state.items + items, page = newKey, endReached = items.isEmpty())
        }
    )

    init {
        loadNextItems()
    }

    fun loadNextItems() {
        viewModelScope.launch {
            paginate.loadNextItem()
        }
    }
}

data class ScreenState(
    val isLoading: Boolean = false,
    val items: List<ListItem> = emptyList(),
    val error: String? = null,
    val endReached: Boolean = false,
    val page: Int = 0
)