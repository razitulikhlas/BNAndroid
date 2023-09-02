package com.razitulikhlas.core.util.maps

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationsClient {

    fun getLocationUpdates(interval:Long):Flow<Location>

    class LocationExceptions(message:String): Exception()
}