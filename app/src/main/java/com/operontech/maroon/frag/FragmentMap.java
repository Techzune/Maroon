package com.operontech.maroon.frag;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
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
import com.mapbox.services.api.directions.v5.DirectionsCriteria;
import com.mapbox.services.api.directions.v5.MapboxDirections;
import com.mapbox.services.api.directions.v5.models.DirectionsResponse;
import com.mapbox.services.api.directions.v5.models.DirectionsRoute;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.models.Position;
import com.operontech.maroon.R;
import com.operontech.maroon.db.PlaceListing;
import kotlin.text.Charsets;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

import static com.mapbox.services.Constants.PRECISION_6;

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

	@BindView(R.id.map_bottomsheet_fab)
	FloatingActionButton bottomSheetFAB;

	private static final String TAG = "Maroon";
	private PlaceListing currentListing = null;
	private DirectionsRoute currentRoute;

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

		bottomSheetFAB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showRouteOnMap();
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
	 * Gets a route from Mapbox servers then draws on map
	 * @param origin the start of the route
	 * @param destination the end of the route
	 * @param profile the profile of the route (walking, cycling, etc.)
	 */
	public void getRoute(final Position origin, final Position destination, String profile) {
		MapboxDirections directions = new MapboxDirections.Builder()
				.setOrigin(origin)
				.setDestination(destination)
				.setOverview(DirectionsCriteria.OVERVIEW_FULL)
				.setProfile(profile)
				.setAccessToken(getString(R.string.mapbox_access_token))
				.build();
		directions.enqueueCall(new Callback<DirectionsResponse>() {
			@Override
			public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
				if (response.body()  == null) {
					Log.e(TAG, "ERROR: No route was received!");
					return;
				} else if (response.body().getRoutes().size() < 1) {
					Log.e(TAG, "ERROR: No route was received!");
					return;
				}

				// Set the currentRoute
				currentRoute = response.body().getRoutes().get(0);

				// Update the bottomSheet text
				final long routeDuration = Math.round(currentRoute.getDistance() / 60);
				if (routeDuration > 1) {
					bottomSheetSubtitle.setText(getString(R.string.map_duration_plural, routeDuration, Math.round(currentRoute.getDistance())));
				}

				// Draw the route
				drawRoute(currentRoute);

				// Deselect any selected markers on the map
				mMap.deselectMarkers();

				// Create a bounding box that contains the positions of both the start and end
				final LatLngBounds latLngBounds = new LatLngBounds.Builder().include(new LatLng(origin.getLatitude(), origin.getLongitude()))
				                                                            .include(new LatLng(destination.getLatitude(), destination.getLongitude()))
				                                                            .build();

				// Animate camera to that new bounding box
				mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 250), 2000);

			}

			@Override
			public void onFailure(Call<DirectionsResponse> call, Throwable t) {
				Log.e(TAG, "Error: " + t.getMessage());
				Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * Draws a route on the map using Polyline
	 * @param route the route to draw on the map
	 */
	private void drawRoute(DirectionsRoute route) {
		LineString lineString = LineString.fromPolyline(route.getGeometry(), PRECISION_6);
		List<Position> coordinates = lineString.getCoordinates();
		LatLng[] points = new LatLng[coordinates.size()];
		for (int i = 0; i < coordinates.size(); i++) {
			points[i] = new LatLng(
					coordinates.get(i).getLatitude(),
					coordinates.get(i).getLongitude());
		}

		// Draw Points on MapView
		mMap.addPolyline(new PolylineOptions()
				.add(points)
				.color(ContextCompat.getColor(getContext(), R.color.colorGold))
				.width(5));
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
		bottomSheetFAB.setVisibility(View.GONE);
	}

	public void showPlaceOnMap(final boolean animateCamera) {
		// Move the camera to the place
		moveCamera(animateCamera, currentListing.getLatLng(), 17f);

		// Add the marker to the screen
		final Marker mark = mMap.addMarker(new MarkerOptions().position(currentListing.getLatLng()).title(currentListing.getTitle()).snippet(currentListing.getDescription()));

		// Display the marker's info window
		mMap.selectMarker(mark);

		// Add information to the bottom sheet
		bottomSheetTitle.setText(currentListing.getTitle());
		bottomSheetSubtitle.setText("");

		// Show the bottom sheet and FAB
		bottomSheetLayout.setVisibility(View.VISIBLE);
		bottomSheetFAB.setVisibility(View.VISIBLE);
	}

	public void showRouteOnMap() {
		// Make sure we have a starting location
		final Location lastLocation = locEngine.getLastLocation();
		if (lastLocation != null) {
			// Hide the FAB so we don't spam it
			CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) bottomSheetFAB.getLayoutParams();
			p.setAnchorId(View.NO_ID);
			bottomSheetFAB.setLayoutParams(p);
			bottomSheetFAB.hide();

			// Show the user's location
			mMap.setMyLocationEnabled(true);

			// Get the route and such
			getRoute(Position.fromLngLat(lastLocation.getLongitude(), lastLocation.getLatitude()),
					Position.fromLngLat(Double.valueOf(currentListing.getLongitude()), Double.valueOf(currentListing.getLatitude())),
					DirectionsCriteria.PROFILE_WALKING);
		}
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