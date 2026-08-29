package com.example.ui.series

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.EpisodeEntity
import com.example.data.SeriesEntity
import com.example.data.SeriesWithEpisodes
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MySeriesViewModel(application: Application) : AndroidViewModel(application) {
    private val seriesDao = AppDatabase.getDatabase(application).seriesDao()

    val seriesList: StateFlow<List<SeriesWithEpisodes>> = seriesDao.getAllSeriesWithEpisodes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun toggleEpisodeWatched(episodeId: String, isWatched: Boolean) {
        viewModelScope.launch {
            seriesDao.updateEpisodeWatchedStatus(episodeId, isWatched)
        }
    }

    fun addTestSeriesIfEmpty() {
        viewModelScope.launch {
            if (seriesList.value.isEmpty()) {
                val series = SeriesEntity(
                    id = "nexus_protocol",
                    title = "Nexus Protocol",
                    posterUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAj1UQrqFLRpGGVdXwPaJ4Q_xzij5XI4VF6lbxZZJZTKzlQ6EAnVKD7Y9D8M-d7zaD4w1-o0ZcWCRtXW8K8dtc0ZpqfZa7Shzatvc4CrKoyqMhNZXMWc8BsWY0uLCfBGnE3kx_ymxwRWv9hJ9ZzF8KLEkEsiCyYy0byDprBf_Ma8ENzdzRz_5gBXL3uWsNSd4okDkS6WueU6818OYk-6kxljk1w8grxq86h19gp4W_9MI1ZEyzv8i5O",
                    totalEpisodes = 5
                )
                val episodes = (1..5).map {
                    EpisodeEntity(
                        id = "nexus_protocol_S1_E$it",
                        seriesId = "nexus_protocol",
                        seasonNumber = 1,
                        episodeNumber = it,
                        title = "Episodio $it",
                        isWatched = it <= 2 
                    )
                }
                seriesDao.insertSeries(series)
                seriesDao.insertEpisodes(episodes)
            }
        }
    }
}
