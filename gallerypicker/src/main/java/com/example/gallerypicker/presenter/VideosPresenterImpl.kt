package com.example.gallerypicker.presenter

import com.example.gallerypicker.model.interactor.VideosInteractorImpl
import com.example.gallerypicker.view.VideosFragment

class VideosPresenterImpl(var videosFragment: VideosFragment): VideosPresenter {
    var interactor = VideosInteractorImpl(this)
    override fun getPhoneAlbums() {
        interactor.getPhoneAlbums()
    }
}