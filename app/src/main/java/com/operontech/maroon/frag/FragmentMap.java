package com.operontech.maroon.frag;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
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

		// Enable the location engine
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

		// Enable the MyLocation button
		setHasOptionsMenu(true);
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
			if (!isHidden()) {
				getActivity().setTitle(R.string.title_campusMap);
			}
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

	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		inflater.inflate(R.menu.menu_map, menu);
	}

	@Override
	public void setHasOptionsMenu(final boolean hasMenu) {
		super.setHasOptionsMenu(hasMenu);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		if (item.getItemId() == R.id.action_mylocation) {
			goToMyLocation(true);
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Moves the camera to the user's current location
	 * @param animateCamera if true, the camera will be animated
	 */
	public void goToMyLocation(final boolean animateCamera) {
		verifyGPSPermissions();
		final Location lastLocation = locEngine.getLastLocation();
		if (lastLocation != null) {
			moveCamera(animateCamera, new LatLng(lastLocation), 16f);
		}
	}

	/**
	 * Moves the camera to a specified LatLng with zoom
	 * @param animateCamera if true, the camera will be animated
	 * @param latLng the desired center of the camera
	 * @param zoom the desired zoom for the camera
	 */
	private void moveCamera(final boolean animateCamera, final LatLng latLng, final float zoom) {
		if (animateCamera) {
			mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
		} else {
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
		}
	}

	/**
	 * Verifies if permissions for location are granted
	 * If not granted, request the permissions
	 * If granted, enable MyLocation
	 */
	public void verifyGPSPermissions() {
		if (!PermissionsManager.areLocationPermissionsGranted(getContext())) {
			permManager.requestLocationPermissions(getActivity());
		} else {
			mMap.setMyLocationEnabled(true);
		}

	}

	/**
	 * Shows the default map for the view
	 * Enabled GPS Permissions, then centers the camera on Mississippi State University's Drill Field
	 * @param animateCamera if true, the camera will be animated
	 */
	public void showDefaultMap(final boolean animateCamera) {
		// Verify that GPS permissions are granted
		verifyGPSPermissions();

		// Center the camera on Mississippi State University's Drill Field
		moveCamera(animateCamera, new LatLng(33.45405991916038, -88.78927499055862), 15.5f);
	}

	@Override
	public void onExplanationNeeded(final List<String> list) {
		Toast.makeText(getContext(), "This app needs location permissions to enable direction functionality", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onPermissionResult(final boolean granted) {
		if (granted) {
			verifyGPSPermissions();
		} else {
			Toast.makeText(getContext(), "Location permissions not granted", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onHiddenChanged(final boolean hidden) {
		super.onHiddenChanged(hidden);
		if (!hidden) {
			getActivity().setTitle(R.string.title_campusMap);
			showDefaultMap(true);
		}
	}
}
