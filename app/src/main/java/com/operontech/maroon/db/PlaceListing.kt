package com.operontech.maroon.db

data class PlaceListing(var id: String, var title: String, var description: String) {
    constructor() : this("", "", "")
}