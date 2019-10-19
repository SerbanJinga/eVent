package com.roh44x.eVent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    //code for signing in
    private static final int RC_SIGN_IN = 123;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference userDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFirebase();
        createSignInIntent();
    }

    public void initFirebase(){
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        userDatabase = mDatabase.getReference().child("Users");
    }



    // authentification system
    public void createSignInIntent(){

        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false)
                    .build(),
        RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode != RESULT_OK) {
                if (response == null) {
                    // User pressed the back button.
                    finish();
                } else if (response.getError() != null
                        && response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, response.getError().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, response.getError().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }else{
                userDatabase.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            startActivity(new Intent(MainActivity.this, HomeActivity.class));
                        }else{
                            startActivity(new Intent(MainActivity.this, TagChooser.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
//                startActivity(new Intent(MainActivity.this, TagChooser.class));
            }
        }
    }

}


