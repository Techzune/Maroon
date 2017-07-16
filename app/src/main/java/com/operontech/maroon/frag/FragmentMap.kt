package com.operontech.maroon.frag

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.constants.MyLocationTracking
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationSource
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.services.android.telemetry.location.LocationEngine
import com.mapbox.services.android.telemetry.permissions.PermissionsListener
import com.mapbox.services.android.telemetry.permissions.PermissionsManager
import com.operontech.maroon.R

@SuppressLint("MissingPermission")
class FragmentMap : Fragment(), PermissionsListener {
    lateinit var permManager: PermissionsManager
    lateinit var locEngine: LocationEngine
    lateinit var mMap: MapboxMap

    @BindView(R.id.map_mapview)
    var mapView: MapView? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_map, container, false)

        // ButterKnife Binding
        ButterKnife.bind(this, view)

        // Enable the location engine
        locEngine = LocationSource.getLocationEngine(context)
        locEngine.activate()

        permManager = PermissionsManager(this)

        // Create the mapView
        mapView!!.onCreate(savedInstanceState)
        mapView!!.getMapAsync { mapboxMap ->
            mMap = mapboxMap

            // Visual adjustments
            mMap.myLocationViewSettings.accuracyAlpha = 0
            showDefaultMap(false)
        }

        // Enable the MyLocation button
        setHasOptionsMenu(true)
        return view
    }

    override fun onStart() {
        super.onStart()
        if (mapView != null) {
            mapView!!.onStart()
        }
    }

    override fun onResume() {
        super.onResume()
        if (mapView != null) {
            if (!isHidden) {
                activity.setTitle(R.string.title_campusMap)
            }
            mapView!!.onResume()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        if (mapView != null) {
            mapView!!.onSaveInstanceState(outState!!)
        }
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_map, menu)
    }

    override fun setHasOptionsMenu(hasMenu: Boolean) {
        super.setHasOptionsMenu(hasMenu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.action_mylocation) {
            enableLocationFollowing()
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Enables tracking by following the user's location
     */
    fun enableLocationFollowing() {
        verifyGPSPermissions()
        val lastLocation = locEngine.lastLocation
        if (lastLocation != null) {
            mMap.trackingSettings.myLocationTrackingMode = MyLocationTracking.TRACKING_FOLLOW
        }
    }

    /**
     * Moves the camera to a specified LatLng with zoom
     * @param animateCamera if true, the camera will be animated
     * @param latLng the desired center of the camera
     * @param zoom the desired zoom for the camera
     */
    private fun moveCamera(animateCamera: Boolean, latLng: LatLng, zoom: Float) {
        if (animateCamera) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom.toDouble()))
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom.toDouble()))
        }
    }

    /**
     * Verifies if permissions for location are granted
     * If not granted, request the permissions
     * If granted, enable MyLocation
     */
    fun verifyGPSPermissions() {
        if (!PermissionsManager.areLocationPermissionsGranted(context)) {
            permManager.requestLocationPermissions(activity)
        } else {
            locEngine.requestLocationUpdates()
            mMap.isMyLocationEnabled = true
        }

    }

    /**
     * Shows the default map for the view
     * Enabled GPS Permissions, then centers the camera on Mississippi State University's Drill Field
     * @param animateCamera if true, the camera will be animated
     */
    fun showDefaultMap(animateCamera: Boolean) {
        // Verify that GPS permissions are granted
        verifyGPSPermissions()

        // Center the camera on Mississippi State University's Drill Field
        moveCamera(animateCamera, LatLng(33.45405991916038, -88.78927499055862), 15.5f)
    }

    override fun onExplanationNeeded(list: List<String>) {
        Toast.makeText(context, "This app needs location permissions to enable mapping functionality", Toast.LENGTH_LONG).show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            verifyGPSPermissions()
        } else {
            Toast.makeText(context, "Location permissions not granted", Toast.LENGTH_LONG).show()
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            activity.setTitle(R.string.title_campusMap)
            showDefaultMap(true)
        }
    }
}
