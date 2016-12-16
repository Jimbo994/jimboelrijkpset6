/**
 * Created by Jim Boelrijk
 * Student of UvA
 * Student number: 1045216
 *
 * This code was inspired by google tutorial code for FireBase (see URL below)
 *  https://github.com/firebase/quickstart-android
 */

package com.example.jim.jimboelrijkpset6.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Model for signing up users to database.
 */

// [START blog_user_class]
@IgnoreExtraProperties
public class User {

    public String username;
    public String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

}
// [END blog_user_class]
