package com.example.appfoodv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.example.appfoodv2.View.Account.SignInActivity;
import com.example.appfoodv2.View.HomeActivity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivityTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            // check ktra đã đăng nhập vào thẳng vào Home
//            public void run() {
//                if (firebaseAuth.getCurrentUser() != null) {
//                    if (firebaseAuth.getCurrentUser().getEmail().length() > 0) {
//                        startActivity(new Intent(MainActivity.this, HomeActivity.class));
//                    }
//                } else {
//                    startActivity(new Intent(MainActivity.this, SignInActivity.class));
//                }
//
//                finish();
//            }
//        }, 3500);
    }
}