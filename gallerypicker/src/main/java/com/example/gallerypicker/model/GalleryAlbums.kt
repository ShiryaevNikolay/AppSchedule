package com.example.gallerypicker.model

data class GalleryAlbums(var id: Int = 0, var name: String = "", var coverUri: String = "", var albumPhotos: ArrayList<GalleryData> = ArrayList())