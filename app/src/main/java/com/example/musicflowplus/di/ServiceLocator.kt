package com.example.musicflowplus.di

import android.content.Context

import com.example.musicflowplus.data.local.MusicFlowDatabase
import com.example.musicflowplus.data.repository.HistoryRepositoryImpl
import com.example.musicflowplus.data.repository.TrackRepositoryImpl

import com.example.musicflowplus.domain.repository.HistoryRepository
import com.example.musicflowplus.domain.repository.TrackRepository

import com.example.musicflowplus.domain.usecase.AddFavoriteTrackUseCase
import com.example.musicflowplus.domain.usecase.DeleteFavoriteTrackUseCase
import com.example.musicflowplus.domain.usecase.GetFavoriteTracksUseCase

import com.example.musicflowplus.domain.usecase.AddHistoryEntryUseCase
import com.example.musicflowplus.domain.usecase.GetHistoryEntriesUseCase
import com.example.musicflowplus.domain.usecase.DeleteHistoryEntryUseCase
import com.example.musicflowplus.domain.usecase.ClearHistoryUseCase

object ServiceLocator {

    // ==============================
    // ИЗБРАННОЕ
    // ==============================

    private val trackRepository: TrackRepository =
        TrackRepositoryImpl()

    val getFavoriteTracksUseCase =
        GetFavoriteTracksUseCase(trackRepository)

    val addFavoriteTrackUseCase =
        AddFavoriteTrackUseCase(trackRepository)

    val deleteFavoriteTrackUseCase =
        DeleteFavoriteTrackUseCase(trackRepository)


    // ==============================
    // ИСТОРИЯ ПРОСЛУШИВАНИЙ
    // ==============================

    private lateinit var historyRepository: HistoryRepository

    lateinit var addHistoryEntryUseCase: AddHistoryEntryUseCase
        private set

    lateinit var getHistoryEntriesUseCase: GetHistoryEntriesUseCase
        private set

    lateinit var deleteHistoryEntryUseCase: DeleteHistoryEntryUseCase
        private set

    lateinit var clearHistoryUseCase: ClearHistoryUseCase
        private set


    // ==============================
    // ИНИЦИАЛИЗАЦИЯ
    // ==============================

    @Synchronized
    fun initialize(context: Context) {

        if (::historyRepository.isInitialized) {
            return
        }

        val database = MusicFlowDatabase.getInstance(
            context.applicationContext
        )

        val trackDao = database.trackDao()

        historyRepository = HistoryRepositoryImpl(trackDao)

        addHistoryEntryUseCase =
            AddHistoryEntryUseCase(historyRepository)

        getHistoryEntriesUseCase =
            GetHistoryEntriesUseCase(historyRepository)

        deleteHistoryEntryUseCase =
            DeleteHistoryEntryUseCase(historyRepository)

        clearHistoryUseCase =
            ClearHistoryUseCase(historyRepository)
    }
}