package com.example.jim.jimboelrijkpset6.models;

/**
 * Created by Jim on 12-12-2016.
 */

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;


// [START post_class]
@IgnoreExtraProperties
public class Post {

        private static String userId;
        private static String username;
        private static String Recipename;
        private static String image_url;
        private static String Source_url;

        public Post() {
            // Default constructor required for calls to DataSnapshot.getValue(Post.class)
        }

        public Post(String userId, String username, String Recipename, String image_url, String Source_url) {
            Post.userId = userId;
            Post.username = username;
            Post.Recipename = Recipename;
            Post.image_url = image_url;
            Post.Source_url = Source_url;
        }

        // [START post_to_map]
        @Exclude
        public Map<String, Object> toMap() {
            HashMap<String, Object> result = new HashMap<>();
            result.put("uid", userId);
            result.put("author", username);
            result.put("title", Recipename);
            result.put("image", image_url);
            result.put("source", Source_url);

            return result;
        }

    public String getuserID() {
        return userId;
    }

    public static String getRecipename() {
        return Post.Recipename;
    }
    public String getImage_url() {
        return image_url;
    }
    public String getSource_url() {
        return Source_url;
    }


        // [END post_to_map]

    }
// [END post_class]


