package com.example.musicflowplus.di

import com.example.musicflowplus.data.repository.TrackRepositoryImpl
import com.example.musicflowplus.domain.repository.TrackRepository
import com.example.musicflowplus.domain.usecase.AddFavoriteTrackUseCase
import com.example.musicflowplus.domain.usecase.DeleteFavoriteTrackUseCase
import com.example.musicflowplus.domain.usecase.GetFavoriteTracksUseCase

object ServiceLocator {

    private val trackRepository: TrackRepository = TrackRepositoryImpl()

    val getFavoriteTracksUseCase = GetFavoriteTracksUseCase(trackRepository)

    val addFavoriteTrackUseCase = AddFavoriteTrackUseCase(trackRepository)

    val deleteFavoriteTrackUseCase = DeleteFavoriteTrackUseCase(trackRepository)
}