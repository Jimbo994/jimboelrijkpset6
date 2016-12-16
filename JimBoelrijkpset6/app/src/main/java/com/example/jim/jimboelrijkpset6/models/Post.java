/**
 * Created by Jim Boelrijk
 * Student of UvA
 * Student number: 1045216
 *
 * This code was inspired by google tutorial code for FireBase (see URL below)
 *  https://github.com/firebase/quickstart-android
 */

package com.example.jim.jimboelrijkpset6.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Model for posts to database.
 */

// [START post_class]
@IgnoreExtraProperties
public class Post {
        private static String Recipename;

        public Post() {
            // Default constructor required for calls to DataSnapshot.getValue(Post.class)
        }

        public Post(String Recipename) {
            Post.Recipename = Recipename;
        }

        // [START post_to_map]
        @Exclude
        public Map<String, Object> toMap() {
            HashMap<String, Object> result = new HashMap<>();
              result.put("title", Recipename);

            return result;
        }

    public static String getRecipename() {
        return Post.Recipename;
    }
        // [END post_to_map]

    }
// [END post_class]


