package com.shiryaev.gallerypicker.presenter

import com.shiryaev.gallerypicker.model.interactor.PhotosInteractorImpl
import com.shiryaev.gallerypicker.view.PhotosFragment

class PhotosPresenterImpl(var photosFragment: PhotosFragment): PhotosPresenter {
    private val interactor: PhotosInteractorImpl = PhotosInteractorImpl(this)
    override fun getPhoneAlbums() {
        interactor.getPhoneAlbums()
    }
}