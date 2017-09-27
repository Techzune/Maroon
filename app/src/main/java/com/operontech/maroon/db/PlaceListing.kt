package com.operontech.maroon.db

import com.mapbox.mapboxsdk.geometry.LatLng
import java.io.Serializable


data class PlaceListing(var id: String, var title: String, var description: String, var latitude: String, var longitude: String) : Serializable {
    constructor() : this("", "", "", "", "")

    fun getLatLng(): LatLng {
        return LatLng(latitude.toDouble(), longitude.toDouble())
    }
}