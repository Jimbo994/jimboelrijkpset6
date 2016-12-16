/**
 * Created by Jim Boelrijk
 * Student of UvA
 * Studentnumber: 10452516
 * */

package com.example.jim.jimboelrijkpset6.viewholder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.jim.jimboelrijkpset6.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 *  This class sets a view where can be searched for ingredients.
 *  Text can be entered in a edittext and this class makes string appropriate to send to RecipesActivity intent
 *  where a search request can be done.
 * */
public class RecipeSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_search);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_app_recipe);
    }

    // Takes in edittext and processes it to suitable string for next intent and starts next intent.
    public void do_search(View view) {
        EditText input = (EditText) findViewById(R.id.search_request);
        String search_request = input.getText().toString().replaceAll(" ", "");

        if (!(search_request.length() == 0)){
            Intent search_and_show = new Intent (this, RecipesActivity.class);
            search_and_show.putExtra("Search", search_request);
            startActivity(search_and_show);
        }
    }

    // Inflates menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    // Makes menu items clickable
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.recipe_book) {
            Intent go_to_recipebook = new Intent(this, MyRecipesActivity.class);
            startActivity(go_to_recipebook);
            finish();
        }
        if (id == R.id.search) {
            return true;
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




