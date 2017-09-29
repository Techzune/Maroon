package com.operontech.maroon.frag;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.MyLocationTracking;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationSource;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.offline.OfflineManager;
import com.mapbox.mapboxsdk.offline.OfflineRegion;
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.permissions.PermissionsListener;
import com.mapbox.services.android.telemetry.permissions.PermissionsManager;
import com.operontech.maroon.R;
import com.operontech.maroon.db.PlaceListing;
import kotlin.text.Charsets;
import org.json.JSONObject;

import java.util.List;

@SuppressLint("MissingPermission")
public class FragmentMap extends Fragment implements PermissionsListener {
	PermissionsManager permManager;
	LocationEngine locEngine;
	OfflineManager offlineManager;
	MapView mapView;
	MapboxMap mMap;

	@BindView(R.id.map_bottomsheet_layout)
	LinearLayout bottomSheetLayout;

	@BindView(R.id.map_bottomsheet_title)
	TextView bottomSheetTitle;

	@BindView(R.id.map_bottomsheet_subtitle)
	TextView bottomSheetSubtitle;

	private static final String TAG = "Maroon";
	private PlaceListing currentListing = null;

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_map, container, false);

		// Bind the views
		ButterKnife.bind(this, view);

		// Get the passed arguments for the current listing
		final Bundle args = getArguments();
		if (args != null && args.containsKey("target")) {
			currentListing = (PlaceListing) getArguments().getSerializable("target");
		}

		// Enable the location engine
		locEngine = new LocationSource(getContext());
		locEngine.activate();

		mapView = view.findViewById(R.id.map_mapview);
		permManager = new PermissionsManager(this);

		// Create the mapView
		mapView.onCreate(savedInstanceState);

		mapView.getMapAsync(new OnMapReadyCallback() {
			@Override
			public void onMapReady(final MapboxMap mapboxMap) {
				// Set up the OfflineManager
				offlineManager = OfflineManager.getInstance(getContext());

				// Create a bounding box for the offline region
				final LatLngBounds latLngBounds = new LatLngBounds.Builder().include(new LatLng(33.468301, -88.775908)) // NE
				                                                            .include(new LatLng(33.447141, -88.805906)) // SW
				                                                            .build();

				// Define the offline region
				final OfflineTilePyramidRegionDefinition definition = new OfflineTilePyramidRegionDefinition(mapboxMap.getStyleUrl(), latLngBounds, 10, 20, getActivity().getResources().getDisplayMetrics().density);

				// Complete the setup for the offline region
				final byte[] metadata;
				try {
					// Set the metadata
					final JSONObject jsonObject = new JSONObject();
					jsonObject.put("FIELD_REGION_NAME", "Mississippi State University");
					final String json = jsonObject.toString();
					metadata = json.getBytes(Charsets.UTF_8);

					// Create the region asynchronously
					offlineManager.createOfflineRegion(definition, metadata, new OfflineManager.CreateOfflineRegionCallback() {
						@Override
						public void onCreate(final OfflineRegion offlineRegion) {
							offlineRegion.setDownloadState(OfflineRegion.STATE_ACTIVE);
						}

						@Override
						public void onError(final String error) {
							Log.e(TAG, "Error: " + error);
						}
					});
				} catch (final Exception exception) {
					Log.e(TAG, "Failed to encode metadata: " + exception.getMessage());
				}
			}
		});

		mapView.getMapAsync(new OnMapReadyCallback() {
			@Override
			public void onMapReady(final MapboxMap mapboxMap) {
				mMap = mapboxMap;

				// Visual adjustments
				mMap.getMyLocationViewSettings().setAccuracyAlpha(0);

				// If a target has been determined, go to it
				if (currentListing != null) {
					showPlaceOnMap(true);
				} else {
					showDefaultMap(true);
				}
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
			enableLocationFollowing();
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Enables tracking by following the user's location
	 */
	public void enableLocationFollowing() {
		verifyGPSPermissions();
		final Location lastLocation = locEngine.getLastLocation();
		if (lastLocation != null) {
			mMap.getTrackingSettings().setMyLocationTrackingMode(MyLocationTracking.TRACKING_FOLLOW);
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
			locEngine.requestLocationUpdates();
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

		// Hide the bottom sheet
		bottomSheetLayout.setVisibility(View.GONE);
	}

	public void showPlaceOnMap(final boolean animateCamera) {
		// Move the camera to the place
		moveCamera(animateCamera, currentListing.getLatLng(), 17f);

		// Add the marker to the screen
		final Marker mark = mMap.addMarker(new MarkerOptions().position(currentListing.getLatLng()).title(currentListing.getTitle()).snippet(currentListing.getDescription()));

		// Display the marker's info window
		mMap.selectMarker(mark);

		// Display the bottom sheet
		bottomSheetLayout.setVisibility(View.VISIBLE);
	}

	@Override
	public void onExplanationNeeded(final List<String> list) {
		Toast.makeText(getContext(), "This app needs location permissions to enable mapping functionality", Toast.LENGTH_LONG).show();
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