package com.shiryaev.gallerypicker.view

import com.shiryaev.gallerypicker.model.GalleryAlbums

interface OnPhoneImagesObtained {
    fun onComplete(albums: ArrayList<GalleryAlbums>)
    fun onError()
}