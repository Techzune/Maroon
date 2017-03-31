package com.operontech.maroon;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;
import com.mapbox.mapboxsdk.Mapbox;
import com.operontech.maroon.frag.FragmentHome;
import com.operontech.maroon.frag.FragmentMap;

public class ActivityMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

	public final static String MAROONTAG = "Maroon";
	private boolean backButtonExitTriggered = false;
	private FragmentManager fManager;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set up the main content (fragment)
		fManager = getSupportFragmentManager();
		fManager.beginTransaction().add(R.id.main_fragment, new FragmentHome()).commit();

		setContentView(R.layout.activity_main);

		// Set up the toolbar
		final Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
		setSupportActionBar(toolbar);

		// Set up the navigation drawer
		final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawerLayout);
		final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		toggle.syncState();

		// Set up navigation view listener
		final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);

		// Set up Mapbox
		Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
	}

	@Override
	public void onBackPressed() {
		final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawerLayout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else if (fManager.getBackStackEntryCount() == 0 && !backButtonExitTriggered) {
			backButtonExitTriggered = true;
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					backButtonExitTriggered = false;
				}
			}, 2000);
			Toast.makeText(this, getString(R.string.back_button_exit), Toast.LENGTH_SHORT).show();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onNavigationItemSelected(@NonNull
	final MenuItem item) {
		final int id = item.getItemId();

		switch (id) {
		case R.id.nav_home:
			showFragment(new FragmentHome());
			break;
		case R.id.nav_map:
			showFragment(new FragmentMap());
			break;

		case R.id.nav_preferences:
			break;
		case R.id.nav_about:
			break;
		}

		final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawerLayout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	private void showFragment(final Fragment fragment) {
		fManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).replace(R.id.main_fragment, fragment).commit();
	}
}
