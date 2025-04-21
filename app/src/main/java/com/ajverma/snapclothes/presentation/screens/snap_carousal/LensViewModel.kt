package com.ajverma.snapclothes.presentation.screens.snap_carousal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ajverma.snapclothes.data.network.models.LensData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LensViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LensUiState())
    val uiState: StateFlow<LensUiState> = _uiState.asStateFlow()

    // --- Repository Interaction (Simulated) ---
    private suspend fun fetchLensesFromRepository(): List<LensData> {
        delay(500)
        return listOf(
            LensData("id_1", "Dog Ears", "https://picsum.photos/id/10/200"),
            LensData("id_2", "Hearts", "https://picsum.photos/id/20/200"),
            LensData("id_3", "Glasses", "https://picsum.photos/id/30/200"),
            LensData("id_4", "Clown Nose", "https://picsum.photos/id/40/200"),
            LensData("id_5", "Flower Crown", "https://picsum.photos/id/50/200"),
            LensData("id_6", "No Filter", "https://picsum.photos/id/60/200"),
            LensData("id_7", "Vintage", "https://picsum.photos/id/70/200"),
            LensData("id_8", "Sparkles", "https://picsum.photos/id/80/200"),
            LensData("id_9", "Big Eyes", "https://picsum.photos/id/90/200"),
            LensData("id_10", "Rainbow", "https://picsum.photos/id/100/200")
        )
    }
    // --- End Simulation ---

    init {
        loadLenses()
    }

    fun loadLenses() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val fetchedLenses = fetchLensesFromRepository()
                val initialSelectedIndex = if (fetchedLenses.isNotEmpty()) fetchedLenses.size / 2 else -1

                _uiState.update {
                    it.copy(
                        lenses = fetchedLenses,
                        isLoading = false,
                        selectedLensIndex = initialSelectedIndex,
                        currentlyAppliedLensId = fetchedLenses.getOrNull(initialSelectedIndex)?.id
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    // Called when a user explicitly taps a lens item
    fun onLensTapped(index: Int) {
        if (index >= 0 && index < _uiState.value.lenses.size) {
            val tappedLens = _uiState.value.lenses[index]
            _uiState.update {
                it.copy(
                    selectedLensIndex = index,
                    currentlyAppliedLensId = tappedLens.id
                )
            }
        }
    }
}

data class LensUiState(
    val lenses: List<LensData> = emptyList(),
    val isLoading: Boolean = false,
    val selectedLensIndex: Int = -1,
    val currentlyAppliedLensId: String? = null
)