package com.operontech.maroon;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import com.mapbox.mapboxsdk.Mapbox;
import com.operontech.maroon.frag.FragmentHome;
import com.operontech.maroon.frag.FragmentMap;

public class ActivityMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

	public final static String MAROONTAG = "Maroon";
	private boolean backButtonExitTriggered = false;
	FragmentManager fManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set up the main content (fragment)
		fManager = getSupportFragmentManager();
		fManager.beginTransaction().add(R.id.main_fragment, new FragmentHome()).commit();

		setContentView(R.layout.activity_main);

		// Set up the toolbar
		Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
		setSupportActionBar(toolbar);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.main_fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
			}
		});

		// Set up the navigation drawer
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawerLayout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();

		// Set up navigation view listener
		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);

		// Set up Mapbox
		Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawerLayout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		int id = item.getItemId();

		switch (id) {
		case R.id.nav_home:
			showFragment(new FragmentHome());
			break;
		case R.id.nav_map:
			showFragment(new FragmentMap());
			break;
		}

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawerLayout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	private void showFragment(final Fragment fragment) {
		fManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).replace(R.id.main_fragment, fragment).commit();
	}
}
