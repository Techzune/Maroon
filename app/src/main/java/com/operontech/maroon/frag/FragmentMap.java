package com.operontech.maroon.frag;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationSource;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.permissions.PermissionsListener;
import com.mapbox.services.android.telemetry.permissions.PermissionsManager;
import com.operontech.maroon.R;

import java.util.List;

@SuppressLint("MissingPermission")
public class FragmentMap extends Fragment implements PermissionsListener {
	PermissionsManager permManager;
	LocationEngine locEngine;
	MapView mapView;
	MapboxMap mMap;

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_map, container, false);

		locEngine = LocationSource.getLocationEngine(getContext());
		locEngine.activate();

		mapView = (MapView) view.findViewById(R.id.map_mapview);
		permManager = new PermissionsManager(this);

		// Create the mapView
		mapView.onCreate(savedInstanceState);
		mapView.getMapAsync(new OnMapReadyCallback() {
			@Override
			public void onMapReady(final MapboxMap mapboxMap) {
				mMap = mapboxMap;

				// Visual adjustments
				mMap.getMyLocationViewSettings().setAccuracyAlpha(0);

				showDefaultMap(false);
			}
		});
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		if (mapView != null) {
			mapView.onStart();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mapView != null) {
			mapView.onResume();
		}
	}

	@Override
	public void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mapView != null) {
			mapView.onSaveInstanceState(outState);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mapView != null) {
			mapView.onPause();
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		if (mapView != null) {
			mapView.onStop();
		}
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		if (mapView != null) {
			mapView.onLowMemory();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mapView != null) {
			mapView.onDestroy();
		}
	}

	private void moveCamera(final boolean animateCamera, final LatLng latLng, final float zoom) {
		if (animateCamera) {
			mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
		} else {
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
		}
	}

	public void verifyGPS() {
		// Check if location permissions are granted
		// if not, request them
		if (!PermissionsManager.areLocationPermissionsGranted(getContext())) {
			permManager.requestLocationPermissions(getActivity());
		} else {
			mMap.setMyLocationEnabled(true);
		}

	}

	public void showDefaultMap(final boolean animateCamera) {
		// Verify that GPS permissions are granted
		verifyGPS();

		if (PermissionsManager.areLocationPermissionsGranted(getContext())) {
			final Location lastLocation = locEngine.getLastLocation();
			if (lastLocation != null) {
				moveCamera(animateCamera, new LatLng(lastLocation), 17.5f);
			}
		} else {
			moveCamera(animateCamera, new LatLng(33.4537233, -88.7902384), 14f);
		}
	}

	public void setMap() {

	}

	@Override
	public void onExplanationNeeded(final List<String> list) {

	}

	@Override
	public void onPermissionResult(final boolean granted) {
		if (granted) {
			verifyGPS();
		} else {
			Toast.makeText(getContext(), "Location permissions not granted", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onHiddenChanged(final boolean hidden) {
		super.onHiddenChanged(hidden);
		if (!hidden) {
			showDefaultMap(false);
		}
	}
}
