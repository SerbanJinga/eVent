package com.roh44x.eVent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PostDetailActivity extends AppCompatActivity {
    private static final String TAG = "PostDetailActivity";

    public static final String EXTRA_POST_KEY = "post_key";


    private DatabaseReference mPostReference;
    private ValueEventListener mPostListener;
    private String mPostKey;

    private TextView mAuthorView;
    private TextView mTitleView;
    private TextView mBodyView;
    public Button btnGoing;
    public Button btnInterested;
    public TextView noGoing;
    public TextView noIntersted;
    public ImageView imagePost;
    public TextView dateEvent;

    public DatabaseReference userDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        userDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        mPostKey = getIntent().getStringExtra(EXTRA_POST_KEY);
        if (mPostKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }

        // Initialize Database
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("posts").child(mPostKey);
        mAuthorView = findViewById(R.id.postAuthor);
        mTitleView = findViewById(R.id.postTitle);
        mBodyView = findViewById(R.id.postDescription);
        btnGoing = findViewById(R.id.goingBtn);
        btnInterested = findViewById(R.id.interestedBtn);
        noGoing = findViewById(R.id.goingNo);
        noIntersted = findViewById(R.id.interestedNo);
        imagePost = findViewById(R.id.imagePost);
        dateEvent = findViewById(R.id.eventDate);

        btnGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoingClicked(mPostReference);
            }
        });

        btnInterested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onInterestedClicked(mPostReference);
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();

        // Add value event listener to the post
        // [START post_value_event_listener]
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Post post = dataSnapshot.getValue(Post.class);
                // [START_EXCLUDE]
                mAuthorView.setText(post.author);
                mTitleView.setText(post.title);
                mBodyView.setText(post.description);
                noGoing.setText(Integer.toString(post.goingToNumber));
                noIntersted.setText(Integer.toString(post.interestedInNumber));
                final String imageStoragePath = "images/" + dataSnapshot.getKey() + ".jpg";
                StorageReference imgref = FirebaseStorage.getInstance().getReference().child(imageStoragePath);
                imgref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(imagePost.getContext())
                                .load(uri.toString())
                                .into(imagePost);
                        imagePost.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("mydebug", "couldn't access imageStoragePath: " + imageStoragePath);
                        e.printStackTrace();
                        imagePost.setImageResource(R.drawable.fui_ic_googleg_color_24dp);
                    }
                });
                dateEvent.setText(post.dateEvent);


                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(PostDetailActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        mPostReference.addValueEventListener(postListener);
        // [END post_value_event_listener]

        // Keep copy of post listener so we can remove it when app stops
        mPostListener = postListener;


    }
    // [END post_stars_transaction]
    // [START post_stars_transaction]
    private void onGoingClicked(final DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Post p = mutableData.getValue(Post.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }
                final String postUid = postRef.getKey();
                if (p.going.containsKey(getUid())) {
                    // Unstar the post and remove self from stars
                    p.goingToNumber = p.goingToNumber - 1;
                    p.going.remove(getUid());
                    userDatabase.child("going_posts_id_list").child(postUid).removeValue();
                } else {
                    // Star the post and add self to stars
                    p.goingToNumber = p.goingToNumber + 1;
                    p.going.put(getUid(), true);
                    userDatabase.child("going_posts_id_list").child(postUid).setValue(true);
                }


                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }

    private void onInterestedClicked(final DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Post p = mutableData.getValue(Post.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }
                final String postUid = postRef.getKey();

                if (p.interested.containsKey(getUid())) {
                    // Unstar the post and remove self from stars
                    p.interestedInNumber = p.interestedInNumber - 1;
                    p.interested.remove(getUid());

                    userDatabase.child("interested_posts_id_list").child(postUid).removeValue();

                } else {
                    // Star the post and add self to stars
                    p.interestedInNumber = p.interestedInNumber + 1;
                    p.interested.put(getUid(), true);
                    userDatabase.child("interested_posts_id_list").child(postUid).setValue(true);

                }


                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mPostListener != null) {
            mPostReference.removeEventListener(mPostListener);
        }

    }
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

}
