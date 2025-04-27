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
            LensData("d1218e18-2e0c-4366-abc9-2245e3a270a9", "puffer jacket", "https://res.cloudinary.com/dthxd4dah/image/upload/v1745780687/vest_extracted_centered_200x200_ch0mum.png", "6805797ae1032701b069f8c9"),
            LensData("dd16a951-2a07-4f0e-8869-f3565c9fa76f", "coat", "https://res.cloudinary.com/dthxd4dah/image/upload/v1745780995/new_coat_extracted_centered_200x200_qmhjfd.png", "680e2d6db3d69b4292da6bd7"),
            LensData("9ef9bd38-623c-450b-85ce-456ad17181b5", "purple full", "https://res.cloudinary.com/dthxd4dah/image/upload/v1745781823/new_bodysuit_extracted_centered_200x200_wy4j9f.png", "680e3f7eef3c5d99a202ffcf"),
            LensData("942e242a-bf2a-4875-b585-75b4fae6ba6d", "pinky", "https://res.cloudinary.com/dthxd4dah/image/upload/v1745781601/isolated_dress_extracted_centered_200x200_htkrac.png", "680e34cead3bf40722770ebd"),
            LensData("b5ca0c29-db60-43b9-b203-ca4430c7dd5a", "red dress", "https://res.cloudinary.com/dthxd4dah/image/upload/v1745782354/red_dress_white_bg_clean_200x200_y93xgo.png", "680c2128027567c359766d7f"),
            LensData("a53e8ad9-4239-4a6b-87af-e71bda9b25a9", "skirt", "https://res.cloudinary.com/dthxd4dah/image/upload/v1745782669/skirt_extracted_centered_200x200_clnljk.png", "680bc73ab8e52633248db841"),
            LensData("db79d777-42a8-48dd-90f8-d36f3b615cc1", "white dress", "https://res.cloudinary.com/dthxd4dah/image/upload/v1745782959/tight_dress_extracted_centered_200x200_aq1wqx.png", "680bb59adbf4940218976169"),
            LensData("9b7188b6-6768-42af-8d10-ece8d304af07", "hoodie", "https://res.cloudinary.com/dthxd4dah/image/upload/v1745783434/hoodie_extracted_centered_200x200_xdlffh.png", "680b9b5dc7a86cfa2b6c1bd6"),
            LensData("196b1566-2372-4d8a-8cf4-253a3fd3797c", "superman t-shirt", "https://res.cloudinary.com/dthxd4dah/image/upload/v1745783753/superman_tee_extracted_centered_200x200_jksslj.png", "67e0a260b659996e8d23c4e6")
        )
    }
    // --- End Simulation ---

    init {
        loadLenses()
    }

    private fun loadLenses() {
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