package com.example.gallerypicker.presenter

import com.example.gallerypicker.model.interactor.PhotosInteractorImpl
import com.example.gallerypicker.view.PhotosFragment

class PhotosPresenterImpl(var photosFragment: PhotosFragment): PhotosPresenter {
    val interactor: PhotosInteractorImpl = PhotosInteractorImpl(this)
    override fun getPhoneAlbums() {
        interactor.getPhoneAlbums()
    }
}