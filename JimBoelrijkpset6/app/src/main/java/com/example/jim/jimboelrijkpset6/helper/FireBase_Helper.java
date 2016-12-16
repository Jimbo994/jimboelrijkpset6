/**
 * Created by Jim Boelrijk
 * Student of UvA
 * Studentnumber: 10452516
 * */

package com.example.jim.jimboelrijkpset6.helper;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Helper class that reads data from Firebase database from a Databasereference.
 * It takes the following events in consideration: onChildadded, onChildChanged, onChildRemoved, onChildMoved, onCancelled.
 * Because of this the arraylist is kept up to date when the database is changed.
 * Although removing, changing and moving of the database are not yet supported in this version, Firebase helper can easily
 * be adjusted to do so.
 */
public class FireBase_Helper{

    DatabaseReference db;
    ArrayList<String> myrecipes;

    public FireBase_Helper(DatabaseReference db) {
        this.db = db;
        myrecipes = new ArrayList<>();
    }

    /** Retrieves Arraylist, on Eventlisteners fetchdata is called which fills Arraylist
    Not all Eventlisteners used yet.*/
    public ArrayList<String> retrieve()
    {
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return myrecipes;
    }


    /** fetches data from database and adds it to Arraylist*/
    private void fetchData(DataSnapshot dataSnapshot)
    {
            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                myrecipes.add(String.valueOf(ds.getValue()));
            }
    }
}


