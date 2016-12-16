/**
 * Created by Jim Boelrijk
 * Student of UvA
 * Studentnumber: 10452516
 * */

package com.example.jim.jimboelrijkpset6.viewholder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jim.jimboelrijkpset6.helper.Http_Helper;
import com.example.jim.jimboelrijkpset6.R;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 *  This Class sets a view that shows all the recipes that were found by the search request from RecipeSearchActivity
 *  By initializing a ArrayList and calling GetRecipe.
 *  RecipePuppy API is used to get information. See Readme for more information.
 * */
public class RecipesActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    ListView theListView;
    private String TAG = RecipeSearchActivity.class.getSimpleName();

    public String url1 = "http://www.recipepuppy.com/api/?i=";
    private String mCompleteURL;

    private ArrayList<String> mRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_app_recipe);

        mRecipes = new ArrayList<>();

        //Get data from Bundle
        Bundle extras = getIntent().getExtras();
        String mSearch = extras.getString("Search");

        mCompleteURL = url1 + mSearch;
        Log.e(TAG, "entered: " + mSearch);
        new GetRecipe().execute();
        }

    /**
     * This class does a Http request using Http_Helper class and then parses the information that is wanted out of it.
     * It uses an AsyncTask
     * */
    private class GetRecipe extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RecipesActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * Does http-request and parsed JSONobject into strings and adds them to ArrayList
         * takes care of JSON exceptions.
         * */
        @Override
        protected Void doInBackground(Void... arg0) {
            Http_Helper helper = new Http_Helper();
            String jsonStr = helper.makeServiceCall(mCompleteURL);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray jsonArr = jsonObj.getJSONArray("results");
                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject recipes = jsonArr.getJSONObject(i);
                        String recipe = recipes.getString("title");
                        mRecipes.add(recipe);
                    }


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

        // Initializes Arrayadapter and links it to ListView.
        // Also makes items clickable and sends appropriate string to RecipeDetailActivity class and starts intent.
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(RecipesActivity.this, android.R.layout.simple_list_item_1, mRecipes);

            // Get the ListView so we can work with it
            theListView = (ListView) findViewById(R.id.list);

            // Connect the ListView with the Adapter that acts as a bridge between it and the array
            theListView.setAdapter(arrayAdapter);

            theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Object obj = theListView.getItemAtPosition(position);
                    String str_of_obj = obj.toString().replaceAll(" ", "%20");
                    Log.e(TAG, "Clicked item: " + str_of_obj);
                    Intent show_full_recipe = new Intent (getApplicationContext(), RecipeDetailActivity.class);
                    show_full_recipe.putExtra("Recipe_Search", str_of_obj);
                    startActivity(show_full_recipe);
                }
            });
        }

    }

    // Makes menu items clickable
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
