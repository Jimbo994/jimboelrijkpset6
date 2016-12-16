/**
 * Created by Jim Boelrijk
 * Student of UvA
 * Studentnumber: 10452516
 * */

package com.example.jim.jimboelrijkpset6.viewholder;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.jim.jimboelrijkpset6.helper.FireBase_Helper;
import com.example.jim.jimboelrijkpset6.R;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

/**
 * This class sets a view for a list of recipes stored by a user.
 * TODO: Make items clickable so another http request can be done to show recipedetails.
 * TODO: Make items deletable.
 * */
public class MyRecipesActivity extends BaseActivity {

    private DatabaseReference mDatabase;

    FireBase_Helper mhelper;
    ArrayAdapter<String> mAdapter;
    ListView mRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipes);
        // Displays app icon in Action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_app_recipe);

        mRecipes = (ListView) findViewById(R.id.myrecipes);

        // create database reference
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://native-app-studio.firebaseio.com/user-posts/" + getUid());
        // Call a helper and link to database reference
        mhelper = new FireBase_Helper(mDatabase);

        //Adapter, fill with data retrieved by mhelper
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mhelper.retrieve());

        //Set adapter to ListView
        mRecipes.setAdapter(mAdapter);
   }

    /** Inflate Menu */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

//     Make Menu items clickable and link to their respective views.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.search) {
            Intent search_activity = new Intent(this, RecipeSearchActivity.class);
            startActivity(search_activity);
            finish();
        }
        if (id == R.id.recipe_book) {
            Intent go_to_recipebook = new Intent(this, MyRecipesActivity.class);
            startActivity(go_to_recipebook);
            finish();
        }
        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
