package com.ajverma.snapclothes.presentation.screens.snap_carousal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ajverma.snapclothes.data.network.models.LensData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LensViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(LensUiState())
    val uiState: StateFlow<LensUiState> = _uiState.asStateFlow()

    // --- Repository Interaction (Simulated) ---
    private suspend fun fetchLensesFromRepository(): List<LensData> {
        delay(500)
        return listOf(
            LensData("d1218e18-2e0c-4366-abc9-2245e3a270a9", "Dog Ears", "https://picsum.photos/id/10/200"),
            LensData("196b1566-2372-4d8a-8cf4-253a3fd3797c", "Hearts", "https://picsum.photos/id/20/200"),
            LensData("1a2dd1a4-ae08-42bb-b6ab-7ef35f349bdd", "No Filter", "https://picsum.photos/id/60/200"),
            LensData("9faa4c06-3a15-48f4-ac60-2d3ae0423fab", "Rainbow", "https://picsum.photos/id/100/200")
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

    fun appendLensesFromGroup(newLenses: List<LensData>) {
        _uiState.update { current ->
            val combined = (current.lenses + newLenses).distinctBy { it.id } // avoid duplicates
            val initialIndex = if (combined.isNotEmpty()) combined.size / 2 else -1

            current.copy(
                lenses = combined,
                isLoading = false,
                selectedLensIndex = if (current.selectedLensIndex == -1) initialIndex else current.selectedLensIndex,
                currentlyAppliedLensId = if (current.currentlyAppliedLensId == null && initialIndex != -1) combined[initialIndex].id else current.currentlyAppliedLensId
            )
        }
    }

}

data class LensUiState(
    val lenses: List<LensData> = emptyList(),
    val isLoading: Boolean = false,
    val selectedLensIndex: Int = -1,
    val currentlyAppliedLensId: String? = null
)