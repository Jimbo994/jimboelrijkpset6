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

public class RecipesActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    ListView theListView;
    private String TAG = RecipeSearchActivity.class.getSimpleName();

    String URL1 = "http://www.recipepuppy.com/api/?i=";
    String URL2 = "&p=3";
    String CompleteURL;
    String Search;

    ArrayList<String> Recipes;

//    http://www.recipepuppy.com/api/?i=onions,garlic&q=omelet&p=3

//    Optional Parameters:
//    i : comma delimited ingredients
//    q : normal search query
//    p : page
//            format=xml : if you want xml instead of json

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_app_recipe);

        Recipes = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        Search = extras.getString("Search");

        CompleteURL = URL1 + Search + URL2;
        Log.e(TAG, "entered: " + Search);
        new GetRecipe().execute();

        }

    private class GetRecipe extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RecipesActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Http_Helper helper = new Http_Helper();
            String jsonStr = helper.makeServiceCall(CompleteURL);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray jsonArr = jsonObj.getJSONArray("results");
                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject recipes = jsonArr.getJSONObject(i);
                        String recipe = recipes.getString("title");
                        Recipes.add(recipe);
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

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(RecipesActivity.this, android.R.layout.simple_list_item_1, Recipes);


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
