package com.operontech.maroon.db;

import com.google.firebase.database.*;

import java.util.ArrayList;

public class DatabasePlaces {
	private static DatabasePlaces databaseInstance;
	private final FirebaseDatabase firebase;

	private ArrayList<Place> placeList;

	public static synchronized DatabasePlaces getInstance() {
		if (databaseInstance == null) {
			databaseInstance = new DatabasePlaces();
		}
		return databaseInstance;
	}

	public DatabasePlaces() {
		firebase = FirebaseDatabase.getInstance();
	}

	public void pullData() {
		final DatabaseReference database = firebase.getReference();
		database.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(final DataSnapshot dataSnapshot) {

			}

			@Override
			public void onCancelled(final DatabaseError databaseError) {

			}
		});
	}

	public ArrayList<Place> getListOfPlaces() {
		if (placeList == null) {
			pullData();
		}
		return placeList;
	}
}
