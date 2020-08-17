package com.example.gallerypicker.view

import com.example.gallerypicker.model.GalleryAlbums

interface OnPhoneImagesObtained {
    fun onComplete(albums: ArrayList<GalleryAlbums>)
    fun onError()
}