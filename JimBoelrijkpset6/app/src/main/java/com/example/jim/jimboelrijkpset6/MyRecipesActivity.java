package com.example.jim.jimboelrijkpset6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jim.jimboelrijkpset6.models.Post;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.FirebaseDatabase;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class MyRecipesActivity extends AppCompatActivity {

//    private static final String TAG = "PostListFragment";
    private DatabaseReference mDatabase;
    FirebaseHelper helper;
    ArrayAdapter<String> mAdapter;
    ListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipes);

        lv = (ListView) findViewById(R.id.myrecipes);

        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        helper = new FirebaseHelper(mDatabase);
        // [END create_database_reference]

        //Adapter
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, helper.retrieve());
        lv.setAdapter(mAdapter);
   }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
