/**
 * Created by Jim Boelrijk
 * Student of UvA
 * Studentnumber: 10452516
 * */
package com.example.jim.jimboelrijkpset6.viewholder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jim.jimboelrijkpset6.helper.Http_Helper;
import com.example.jim.jimboelrijkpset6.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.example.jim.jimboelrijkpset6.models.Post;
import com.example.jim.jimboelrijkpset6.models.User;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RecipeDetailActivity extends BaseActivity {

    private static String TAG = RecipeSearchActivity.class.getSimpleName();

    private DatabaseReference mDatabase;
    private ProgressDialog pDialog;

    private String mThumbnail;

    private String mComplete_Url;
    private String mIngredient;
    private String mRecipe;
    private String mSource_url;


    /**
     * This Class shows the details of a recipe that was clicked on in RecipesActivity
     * It does another HTTP-request to get more information.
     * In this Activity the Recipe can be saved as well so that it can be seen in MyRecipesActivity.
     * RecipePuppy API is used to get information. See Readme for more information.
     * TODO: getRecipe and getCompleteRecipe could be made more general and then moved to a helper class (no more time)
     * TODO: Save and writeNewPost functions could be added to FireBase_Helper (no more time)
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_app_recipe);

        //Get data from Bundle
        Bundle extras = getIntent().getExtras();
        String recipe_search = extras.getString("Recipe_Search");
        Log.e(TAG, "entered: " + recipe_search);

        String url = "http://www.recipepuppy.com/api/?q=";
        mComplete_Url = url + recipe_search;

        // initialize database reference
        mDatabase = FirebaseDatabase.getInstance().getReference();


        new GetCompleteRecipe().execute();
    }


    /**
     * This class does a Http request using Http_Helper class and then parses the information that is wanted out of it.
     * It uses an AsyncTask
     * */
    private class GetCompleteRecipe extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RecipeDetailActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * Does http-request and parsed JSONobject into strings.
         * takes care of JSON exceptions.
         * */
        @Override
        protected Void doInBackground(Void... arg0) {
            Http_Helper helper = new Http_Helper();
            String jsonStr = helper.makeServiceCall(mComplete_Url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray jsonArr = jsonObj.getJSONArray("results");
                    JSONObject recipes = jsonArr.getJSONObject(0);
                    mRecipe = recipes.getString("title");
                    mThumbnail = recipes.getString("thumbnail");
                    mIngredient = recipes.getString("ingredients");
                    mSource_url = recipes.getString("href");
                    Log.e(TAG, "String ingredient: " + mIngredient);

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
            return null;
        }
        //Strings Retrieved from AsyncTask are put into their respective Text and Image Views.
        // Image is loaded via Url into ImageView with use of Picasso Library
        // Information about Picasso can be found here: http://square.github.io/picasso/
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            TextView ingredients = (TextView) findViewById(R.id.ingredients);
            ingredients.setText(mIngredient);

            TextView url = (TextView) findViewById(R.id.url);
            url.setText(mSource_url);

            ImageView meal = (ImageView) findViewById(R.id.image);
            if (mThumbnail.length() != 0) {
                Picasso.with(getApplicationContext()).load(mThumbnail).into(meal);
            } else {
                meal.setImageDrawable(getDrawable(R.drawable.noimagea));
            }
        }
    }
    // OnClick listener for Save button, This function saves recipe into database using writeNewPost, takes care of exceptions.
    public void save(View view) {
        final String RecipeName = mRecipe;


        Toast.makeText(this, "Posting...", Toast.LENGTH_SHORT).show();

        //Start Database Read
        final String userId = getUid();
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        // Check if there is a user logged in or if user can be reached
                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(RecipeDetailActivity.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Write new post
                            writeNewPost(userId, RecipeName);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
    }

    //Write post to Database using Posts model
    private void writeNewPost(String userId, String Recipename) {
        String key = mDatabase.child("posts").push().getKey();
        Post post = new Post(Recipename);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }

    // Inflate Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // Make Menu items clickable and send to appropriate Activities
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


