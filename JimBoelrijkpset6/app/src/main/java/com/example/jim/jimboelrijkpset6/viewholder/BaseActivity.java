/**
 * Created by Jim Boelrijk
 * Student of UvA
 * Student number: 1045216
 *
 * This code was inspired by google tutorial code for FireBase (see URL below)
 *  https://github.com/firebase/quickstart-android
 */
package com.example.jim.jimboelrijkpset6.viewholder;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class BaseActivity extends AppCompatActivity {
    /** Inflates Progressdialog, and dismisses when done.*/
    private ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
    /** gets User id from FireBase database.*/
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}

