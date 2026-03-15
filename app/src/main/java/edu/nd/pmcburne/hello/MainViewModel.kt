package edu.nd.pmcburne.hello

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MainUIState(
    val placemarks: List<Placemark> = emptyList(),
    val tags: List<String> = emptyList(),
    val selectedTag: String = "core",
    val isLoading: Boolean = true
)

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val dao = db.placemarkDao()
    private val api = PlacemarkApi.create()

    private val _uiState = MutableStateFlow(MainUIState())
    val uiState: StateFlow<MainUIState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                val response = api.getPlacemarks()
                val entities = response.map { item ->
                    Placemark(
                        id = item.id,
                        name = item.name,
                        tags = item.tagList.joinToString(","),
                        description = item.description,
                        latitude = item.visualCenter.latitude,
                        longitude = item.visualCenter.longitude
                    )
                }
                dao.insertAll(entities)
            } catch (_: Exception) {
                // If network fails, we still have local data
            }

            val allPlacemarks = dao.getAll()
            val allTags = allPlacemarks
                .flatMap { it.tags.split(",") }
                .filter { it.isNotBlank() }
                .distinct()
                .sorted()

            _uiState.value = MainUIState(
                placemarks = allPlacemarks,
                tags = allTags,
                selectedTag = "core",
                isLoading = false
            )
        }
    }

    fun selectTag(tag: String) {
        _uiState.value = _uiState.value.copy(selectedTag = tag)
    }

    fun filteredPlacemarks(): List<Placemark> {
        val state = _uiState.value
        return state.placemarks.filter { placemark ->
            state.selectedTag in placemark.tags.split(",")
        }
    }
}
